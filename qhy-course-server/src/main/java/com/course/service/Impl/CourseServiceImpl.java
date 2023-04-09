package com.course.service.Impl;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.course.client.CourseUploadClient;
import com.course.config.FileServerConfigProperties;
import com.course.config.RedisCache;
import com.course.entity.dao.Course;
import com.course.entity.dto.CourseDto;
import com.course.entity.vo.CourseVo;
import com.course.entity.vo.UpLoadFileVo;
import com.course.exception.CourseException;
import com.course.executor.ServiceExecutor;
import com.course.filter.VisitDto;
import com.course.mapper.CourseMapper;
import com.course.service.*;
import com.course.utils.FileSafeUploadUtil;
import com.easyCode.feature.mybaits.CustomPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.course.constants.ServiceConstants.*;
import static com.course.entity.factory.CourseFactory.COURSE_CONVERTER;

/**
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private IBigCourseTypeService iBigCourseTypeService;
    @Autowired
    private ISmallCourseTypeService iSmallCourseTypeService;
    @Autowired
    private CourseUploadClient courseUploadClient;
    @Autowired
    private FileServerConfigProperties fileServerConfigProperties;
    @Autowired
    private ICampusCourseService iCampusCourseService;
    @Autowired
    private ISubCampusCourseService iSubCampusCourseService;
    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public String saveCourse(CourseVo courseVo) {
        //判断课程是否重名
       Course.repeatNameCheck(courseVo.getCourseName(),this);
       //判断课程在大类型小类型是否存在，小类型是否在大类型之下
       courseVo.courseTypeCheck(iBigCourseTypeService,iSmallCourseTypeService);
       //设置任务id
        String taskId = UUID.randomUUID().toString();
        //改变这个任务的状态为上传状态（上传0，成功1，失败2）
        CourseVo.changeUploadState(redisTemplate,taskId,UPLOADING);
        //异步处理
        CompletableFuture.runAsync(()->SpringUtil.getBean(CourseServiceImpl.class).upLoadCourseAsync(courseVo,taskId)
        ,SpringUtil.getBean("asyncExecutor"));
        return taskId;
    }

    //实现本地事务和文件上传一致性

    /**
     * *        异步上传文件:
     *      * 1.利用redis生成唯一的任务序列号
     *      * 2.将任务序列号返回给前端
     *      * 3.前端不断轮询获取任务执行结果
     *      * 4.课程上传成功,将任务状态保存至redis,值为1
     *      * 5.课程上传失败,将任务状态保存至redis,值为0
     * @param courseVo
     * @param taskId
     */
    @Transactional
    public void upLoadCourseAsync(CourseVo courseVo, String taskId) {
        Course course;
        //创建一个空集合用来记录上传的文件
        List<Integer> uploadFileLogRecordList = new LinkedList<>();
        try{
            //上传压缩文件，解压目录为课程名
            UpLoadFileVo upLoadFileVo = FileSafeUploadUtil.uploadZipFile(courseVo.getCourseZipFile(), uploadFileLogRecordList
                    , fileServerConfigProperties.getFileBucket(), courseVo.getCourseName());
            //3.执行本地事务
            course = COURSE_CONVERTER.toCourse(courseVo);
            course.fill(upLoadFileVo);
            save(course);
            //执行成功删除日志
            FileSafeUploadUtil.deleteLogRecord(uploadFileLogRecordList);
            //修改发布处理状态为成功，过期时间为30分钟
            CourseVo.changeUploadState(redisTemplate,taskId,UPLOAD_SUCCESS);
        }catch (Exception e){
            log.error("课程发布出现错误",e);
            //失败就删除文件
            FileSafeUploadUtil.deleteFile(uploadFileLogRecordList);
            //修改发布处理为失败，过期时间为30分钟
            CourseVo.changeUploadState(redisTemplate,taskId,UPLOAD_FAILURE);
            throw new CourseException("课程发布失败");
        }
    }


    @Override
    public CourseDto getCourseById(Integer id) {
      return VisitDto.handle(
              visitDto -> doGetCourseById(id,false),
              visitDto -> {
                  if(iCampusCourseService.managerHasCourse(id,visitDto.getCId())){
                      return doGetCourseById(id,true);
                  }
                  return null;
              },
              visitDto -> {
                  if(iSubCampusCourseService.userHasCourse(id,visitDto.getCId())){
                      return doGetCourseById(id,true);
                  }
                  return null;
              }
      );
    }

    private CourseDto doGetCourseById(Integer id,Boolean filterEnable){
        Course course = getById(id);
        if(filterEnable&&course!=null&course.getEnable()!=1){
            return null;
        }
        return COURSE_CONVERTER.toCourseDto(course);
    }
    @Override
    public List<CourseDto> listCourse(CustomPage customPage) {
          return VisitDto.handle(
                  visitDto -> {
                      List<Course> result = CustomPage.getResult(customPage, new Course(), this, null);
                      return COURSE_CONVERTER.toListCourseDto(result);
                  },
                  visitDto -> {
                      List<Integer> idList = iCampusCourseService.getCourseIdListOfManager(customPage, visitDto.getCId());
                       if(idList==null)return null;

                       return COURSE_CONVERTER.toListCourseDto(list(new QueryWrapper<Course>()
                               .in("id",idList).eq("enable",1)));
                  },
                  visitDto -> {
                      List<Integer> courseIdListOfUser = iSubCampusCourseService.getCourseIdListOfUser(customPage, visitDto.getCId());
                      if(courseIdListOfUser==null)return null;
                      return COURSE_CONVERTER.toListCourseDto(list(new QueryWrapper<Course>()
                              .in("id",courseIdListOfUser).eq("enable",1)));

                  }
          );
    }

    //删除课程时，删除文件
    @Override
    public void delCourseById(Integer id) {
        Course course = getById(id);
        //检查当前课程是否还和主校区或者分校区相互关联
        iCampusCourseService.checkCourseReleationShip(id);
        iSubCampusCourseService.checkCourseReleationShip(id);
        removeById(id);
        CompletableFuture.runAsync(()->{
            try{
                courseUploadClient
                        .deleteFile(fileServerConfigProperties.getFileBucket(),course.getCourseDir());
            }catch (Exception e){
                log.error("课程相关文件异步删除过程中出现异常: ", e);
            }
        }, ServiceExecutor.COURSE_THREAD_POOL);

    }

    @Override
    public void enableOrDisableCourse(Integer id, Integer enable) {
        updateById(Course.builder().id(id).enable(enable).build());
    }

    @Override
    public Map<String, String> checkCourseUpload(List<String> taskId) {
     return CourseVo.getUploadState(redisTemplate,taskId);
    }
}
