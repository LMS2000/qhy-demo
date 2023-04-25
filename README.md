# qhy-demo
培训平台项目后端

# 奇幻鱼项目笔记


## 使用MapStruct来处理传递数据不需要过多参数问题



MapStruct依赖包：

MapStruct一般和lombok搭配使用

```xml
        
 <!--      <mapstruct.version>1.4.1.Final</mapstruct.version>-->
<!--        <lombok.version>1.18.24</lombok.version>-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
```



```java
package com.auth.entity.factory;

import com.auth.entity.dao.Authority;
import com.auth.entity.dto.AuthorityDto;
import com.auth.entity.vo.AuthorityVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

public class AuthorityFactory {
    public static final AuthorityConverter AUTHORITY_CONVERTER = Mappers.getMapper(AuthorityConverter.class);

    @Mapper
    public interface AuthorityConverter {
        @Mappings({
                @Mapping(target = "id", ignore = true),
        })
        Authority toAuthority(AuthorityVo authorityVo);

        AuthorityDto toAuthorityDto(Authority authority);

        List<AuthorityDto> toListAuthorityDto(List<Authority> authority);
    }
}

```

   这样我们在需要的使用去调用这些方法就会将原来的对象映射为目标对象，如toAuthority方法将vo对象转换为Authroty对象，并且不会映射id。

token生成服务（配置bean）

:奇幻鱼项目采用的是独立一个模块，最后通过一个叫JwtAutoconfiguratioin类完成配置。

```java
package com.jwtAutoConfiguration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt", name = "open", havingValue = "true")
public class JwtAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public Jwt jwt(JwtProperties jwtProperties){
       return new JwtImpl(jwtProperties);
    }
}

```

`@ConditionalOnProperty` 注解中的`prefix` 属性指定配置的前缀，`name` 属性指定配置的名称，`havingValue`  属性指定只有在为其值的情况下才会去装配这个配置类中的bean。 

`@EnableConfigurationProperties`    注解需要搭配   `@ConditionalOnProperty` 注解使用，作用是将配置类（如JwtProperties）实现属性绑定。我们在使用的时候就可以在配置文件里写入如jwt.open =true jwt.expiration=30 等等操作。



登录的流程图





![](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_area未命名文件 (9)(1).png)







## FileServer文件管理



基于S3协议改的本地存储服务，将SpringMVC默认访问静态路径的处理修改（不然用户访问请求带中文会因为编码解码的缘故导致访问不到本来存在的文件）



基于MINIO中间件starter的内容修改的OssProperties

```java
package com.fileServer.config;

import com.fileServer.exception.FileException;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oss")
@Data
public class OssProperties implements InitializingBean {
    private String accessKey;

    private String accessSecret;

    /**
     * endpoint 配置格式为
     * 通过外网访问OSS服务时，以URL的形式表示访问的OSS资源，详情请参见OSS访问域名使用规则。OSS的URL结构为[$Schema]://[$Bucket].[$Endpoint]/[$Object]
     * 。例如，您的Region为华东1（杭州），Bucket名称为examplebucket，Object访问路径为destfolder/example.txt，
     * 则外网访问地址为https://examplebucket.oss-cn-hangzhou.aliyuncs.com/destfolder/example.txt
     * https://help.aliyun.com/document_detail/375241.html
     */
    private String endpoint;
    /**
     * refer com.amazonaws.regions.Regions;
     * 阿里云region 对应表
     * https://help.aliyun.com/document_detail/31837.htm?spm=a2c4g.11186623.0.0.695178eb0nD6jp
     */
    private String region;

    private boolean pathStyleAccess = true;
    /**
     * 默认启用本地oss作为文件存储
     */
    private boolean localOssClient=true;
    /**
     * LocalOssClient需要将文件存放的本地根目录路径
     */
    private String rootPath="";


    @Override
    public void afterPropertiesSet() throws Exception {
        if(localOssClient&&ObjectUtils.isEmpty(rootPath)){
            throw new FileException("文件存储根路径未进行设置");
        }
    }
}
```

本地的oss操作

```java
package com.fileServer.client;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.fileServer.utils.FileUtil;
import com.oss.config.OssProperties;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
//本地文件存储服务
@RequiredArgsConstructor
public class LocalClient implements OssClient{

    private final OssProperties ossProperties;
    @Override
    public void createBucket(String bucketName) {
        FileUtil.createDir(ossProperties.getRootPath(),bucketName);
    }

    /**
     * 上传文件
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contextType
     * @return
     * @throws IOException
     */
    @Override
    public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws IOException {
          //创建服务端存储的相对路径
          String fileName = FileUtil.pathMerge(ossProperties.getRootPath(),bucketName,objectName);
          //存储文件（写入）
          FileUtil.transFileTo(stream,fileName);
          return null;
    }

    /**
     * 删除文件
     * @param bucketName
     * @param objectName
     */
    @Override
    public void deleteObject(String bucketName, String objectName) {
        String fileName= FileUtil.pathMerge(ossProperties.getRootPath(),bucketName,objectName);
        FileUtil.deleteFile(fileName);
    }

    @Override
    public void createBucket(String bucketName, CannedAccessControlList bucketAccess) {

    }

    @Override
    public void deleteBucket(String bucketName) {

    }

    @Override
    public String getObjectURL(String bucketName, String objectName) {
        return null;
    }

    @Override
    public S3Object getObjectInfo(String bucketName, String objectName) {
        return null;
    }

    @Override
    public AmazonS3 getS3Client() {
        return null;
    }
}

```



**为什么中文资源会访问404？**



文件服务最初基于S3协议,配合MimIO使用，但是项目中需要大量的路径替换，所以使用使用springmvc提供的访问静态资源的处理器。

Springmvc访问静态资源默认使用SimpleUrlHandlerMapping作为处理器请求映射的HandlerMapping,

使用的Handler为ResourceHttpRequestHandler,对应的HandlerAdaptor为HttpRequestHandlerAdapter。



首先在AbstractUrlHandlerMapping中使用了UrlPathHelper对url进行第一次的解码处理

![image-20230327183706746](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20230327183706746.png)

UrlPathHelper这里对请求路径解码

![image-20230327185050519](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20230327185050519.png)



从请求中获取编码，如果说为null就使用默认的编码WebUtils.DEFAULT_CHARACTER_ENCODING其实就是public static final String DEFAULT_CHARACTER_ENCODING = "ISO-8859-1";



![image-20230327185811731](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20230327185811731.png)



第二次路径编码位置在ResourceHttpRequestHandler的handleRequest方法中

![image-20230327193421740](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20230327193421740.png)



对url进行了第二次的解码



![image-20230327193138243](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20230327193138243.png)

已经解码过的请求路径再进行编码,不就又变成乱码了吗? 当然,这里主要针对中文字符存在这个问题,英文字符没有这个问题。



魔改思路：

我们只需要禁止PathResourceResolver对资源路径进行编码即可,核心思路就是自定义一个CustomPathResourceResolver,重新getResource方法,替换默认的实现:

```java
public class CustomPathResourceResolver extends PathResourceResolver {
    @Override
    protected Resource getResource(String resourcePath, Resource location) throws IOException {
        Resource resource=new FileSystemResource(location.getFile().getAbsoluteFile()+ File.separator+resourcePath);
        if(resource.isReadable()){
            return resource;
        }
        return null;
    }
}
```



自定义一个CustomFileResourceHttpRequestHandler替换默认的实现:

```java
//自定义的mapping参考自SimpleUrlMapping源码
public class CustomFileHandlerMapping extends AbstractUrlHandlerMapping implements ApplicationContextAware {

    private final Map<String, Object> urlMap = new LinkedHashMap<>();
    private final CustomFileResourceHttpRequestHandler fileResourceHttpRequestHandler;

    public CustomFileHandlerMapping(CustomFileResourceHttpRequestHandler fileResourceHttpRequestHandler) {
        this.fileResourceHttpRequestHandler = fileResourceHttpRequestHandler;
        //设置映射表所有的/static/**的请求都是由fileResourceHttpRequestHandler去处理
        this.urlMap.put(CourseFileConstants.STATIC_REQUEST_PREFIX_PATTERN, fileResourceHttpRequestHandler);
    }

    @Override
    public void initApplicationContext() throws BeansException {
        super.initApplicationContext();
        registerHandlers(this.urlMap);
    }
      //registerHandlers是SimpleUrlMapping的源码
    private void registerHandlers(Map<String, Object> urlMap) {

        if (urlMap.isEmpty()) {
            logger.trace("No patters in" + formatMappingName());

        } else {
            urlMap.forEach((url, handler) -> {
                if (!url.startsWith("/")) {
                    url = "/" + url;
                }
                //如果是String类型就是beanName方式注册
                if(handler instanceof String ){
                     handler=((String)handler).trim();
                }
                //注册处理器
                registerHandler(url,handler);
            });
            if (logger.isDebugEnabled()) {
                List<String> patterns = new ArrayList<>();
                if (getRootHandler() != null) {
                    patterns.add("/");
                }
                if (getDefaultHandler() != null) {
                    patterns.add("/**");
                }
                patterns.addAll(getHandlerMap().keySet());
                logger.debug("Patterns " + patterns + " in " + formatMappingName());
            }
        }

    }

    @Override
    public int getOrder() {
        return 1;
    }
}
```

装配我们自定义的bean

```java
@Configuration
public class CustomMvcConfig {

    @Bean
    public CustomFileHandlerMapping customFileHandlerMapping(CustomFileResourceHttpRequestHandler customFileResourceHttpRequestHandler) {
        return new CustomFileHandlerMapping(customFileResourceHttpRequestHandler);
    }


    @Bean
    public CustomFileResourceHttpRequestHandler customFileResourceHttpRequestHandler(OssProperties ossProperties) {
        CustomFileResourceHttpRequestHandler customFileResourceHttpRequestHandler = new CustomFileResourceHttpRequestHandler();
       //设置的location会在访问静态资源的时候由CustomPathResourceResolver的getResource方法的localtion获取实际存储的rootpath
        //如D:/qhy-demo/file_home 这样我们就可以去映射我们的本地资源
        customFileResourceHttpRequestHandler.setLocationValues(List.of("file:" + ossProperties.getRootPath()));
        return customFileResourceHttpRequestHandler;
    }
}

```



## 实现本地事务和文件上传一致性思路



**思路**：

```
二阶段提交: 预写日志

1.准备日志表,日志表记录文件名,bucket,md5等信息 </br>
2.上传文件前,记录日志 </br>
3.上传文件 </br>
4.上传成功,执行本地事务,本地事务执行成功,删除日志 </br>
4.上传失败,删除日志,结束请求。 本地事务执行失败,删除日志 </br>

如果上传文件结束后,系统奔溃,那么记录依然存在。

因此,系统重启时,需要扫描日志表,利用其中的记录,去删除对应的文件。

文件删除操作具备幂等性,因此,即使文件上传失败了,删除也不会产生影响。
```

  



**qhy-course前提了解**



课程的设计上有分大课程类型和小课程类型，大课程类型如编程类，艺术类，创新类等。 小课程类型就是具体的课程类型。

并且课程的封面和课程的文件存储在不同的bucket中。为了实现文件上传的回滚事务，添加一张课程上传的日志表来记录课程的bucket名,文件名，文件MD5加密



模块中关于文件回滚的工具类

```java
@Slf4j
public class FileSafeUploadUtil {


    /**
     * 上传文件，过程中异常就回滚
     * @param file
     * @param uploadFileLogRecordList
     * @param bucket
     * @param dir
     * @param zipFile
     * @return
     */
    public static UpLoadFileVo doUpload(MultipartFile file, List<Integer> uploadFileLogRecordList,
                                        String bucket, String dir, boolean zipFile) {
        CourseUploadClient uploadClient = SpringUtil.getBean(CourseUploadClient.class);
        ICourserUploadLogService courserUploadLogService = SpringUtil.getBean(ICourserUploadLogService.class);
        Result result;
        if(zipFile){
          result = uploadClient.uploadZipFile(file,bucket,dir);
        }else{
          result  = uploadClient.uploadFile(file,bucket,dir);
        }
        UpLoadFileVo upLoadFileVo=null;
        try{
            upLoadFileVo = BeanUtil.toBean(result,UpLoadFileVo.class, CopyOptions.create().ignoreCase().ignoreError().ignoreNullValue());
            uploadFileLogRecordList.add(
                    courserUploadLogService
                            .saveCourserUploadLog(CourserUploadLogVo.builder()
                                    .fileMd5(upLoadFileVo.getFileMd5())
                                    .bucketName(bucket)
                                    .fileName(upLoadFileVo.getPath()).build()
                          ));
            return upLoadFileVo;
        }catch (Exception e){
            if (upLoadFileVo != null) {
                uploadClient.deleteFile(bucket, upLoadFileVo.getPath());
            }
            log.error("课程文件上传过程中出现异常: ", e);
            throw new CourseException("课程文件上传出现异常");
        }
    }


    /**
     * 上传文件
     * @param file
     * @param uploadFileLogRecordList
     * @param bucket
     * @param dir
     * @return
     */
    public static UpLoadFileVo uploadFile(MultipartFile file, List<Integer> uploadFileLogRecordList, String bucket, String dir) {
      return   doUpload(file,uploadFileLogRecordList,bucket,dir,false);
    }

    /**
     * 上传压缩文件
     * @param file
     * @param uploadFileLogRecordList
     * @param bucket
     * @param dir
     * @return
     */
    public static UpLoadFileVo uploadZipFile(MultipartFile file, List<Integer> uploadFileLogRecordList, String bucket, String dir){
        return doUpload(file,uploadFileLogRecordList,bucket,dir,true);
    }

    /**
     * 删除文件
     *
     * @param uploadFileLogRecordList
     */
    public static void deleteFile(List<Integer> uploadFileLogRecordList) {
        ICourserUploadLogService courserUploadLogService = SpringUtil.getBean(ICourserUploadLogService.class);
        courserUploadLogService.deleteUploadedFile(uploadFileLogRecordList);
    }

    /**
     * 删除上传文件日志
     *
     * @param uploadFileLogRecordList
     */
    public static void deleteLogRecord(List<Integer> uploadFileLogRecordList) {
        ICourserUploadLogService courserUploadLogService = SpringUtil.getBean(ICourserUploadLogService.class);
        courserUploadLogService.removeByIds(uploadFileLogRecordList);
    }
}

```



## 异步处理上传文件

**前提知识**



1.`CompletableFuture`的用法



首先看看一般用线程池`ExecutorService` 的`execute`方法 其执行异步的操作，但是`execute`方法 没有返回值，也就是说我们不知道是否执行完毕，也得不到反馈。 使用`submit`方法，`submit`方法有两个重载方法 。 

```java
    /**
     回调函数，执行结果可以通过get()方法获取
     */
    <T> Future<T> submit(Callable<T> task);

    /** 
     执行没有返回结果（因为时runable） 可以设置第二个参数指定返回结果，也是通过get()方法获取
     */
    <T> Future<T> submit(Runnable task, T result);

    /**
    执行没有返回结果（因为时runable）
     */
    Future<?> submit(Runnable task);
```

案例:

```java
 ExecutorService executorService = Executors.newSingleThreadExecutor();
        //submit方法返回执行结果，
        Future<?> submit = executorService.submit(() -> {
            System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (false) {
                throw new RuntimeException("test");
            } else {
                System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());

            }
        },12);
        System.out.println("main thread start,time->" + System.currentTimeMillis());
        try {
            System.out.println(submit.get());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        executorService.shutdown();
        System.out.println("main thread end,time->" + System.currentTimeMillis());
```



而`CompletableFuture` 的`supplyAsync`和`runAsync` 方法跟上面的`submit`方法类似  ，`supplyAsync`方法是带返回结果的,`runAsync` 则没有。

```java
//  supplyAsync表示创建带返回值的异步任务的，相当于ExecutorService submit(Callable<T> task) 方法
//  runAsync表示创建无返回值的异步任务，相当于ExecutorService submit(Runnable task)方法
CompletableFuture supplyAsync = CompletableFuture.runAsync(() -> {
    System.out.println(Thread.currentThread() + " start,time->" + System.currentTimeMillis());
    try {
        Thread.sleep(2000);
    } catch (InterruptedException e) {
    }
    if (false) {
        throw new RuntimeException("test");
    } else {
        System.out.println(Thread.currentThread() + " exit,time->" + System.currentTimeMillis());
    }
});
System.out.println("main thread start,time->"+System.currentTimeMillis());
try {
    System.out.println(supplyAsync.get());
} catch (InterruptedException e) {
    throw new RuntimeException(e);
} catch (ExecutionException e) {
    throw new RuntimeException(e);
}
System.out.println("main thread end,time->"+System.currentTimeMillis());
```

  

并且它还有重载方法，我们还可以传递一个`excutor`， 这里我们可以自定义一个excutor

```java
    public static void main(String[] args) {
        System.out.println("main-start");
        System.out.println(runThread());
        System.out.println("main-end");
    }


    public static String runThread(){
        CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "执行完毕");
        }, getExecutor());
        return "hallo";
    }

    public static Executor getExecutor(){
        return new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100), new ThreadFactory() {
            private AtomicInteger atomicInteger=new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"async-thread-pool-"+atomicInteger);
            }
        },new ThreadPoolExecutor.CallerRunsPolicy());
    }
结果：
main-start
hello
main-end
async-thread-pool-1执行完毕
```

我们发现调用方法可以更快的返回我们定义的结果，也就是更快地响应数据，提高我们的吞吐量。





然后我们看项目中的使用

```java
//定义线程池
@Configuration
public class ExecutorConfiguration {

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        //创建一个线程池，核心线程数为2，最大线程池大小为5，线程等待时间为60秒，创建线程用自定义的线程工厂
        //使用CallerRunsPolicy策略(线程打满和队列满之后的请求由main自调用方（main）处理)
       return new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS,
               new ArrayBlockingQueue<>(100), new ThreadFactory() {
           private AtomicInteger count=new AtomicInteger(1);
           @Override
           public Thread newThread(@NotNull Runnable r) {
               return new Thread(r,"async-thread-pool-"+count.getAndIncrement());
           }
       },new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
```

课程服务层接口

```java
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
```

 利用异步去处理上传业务，使用`redis`去记录任务的状态。这样任务的处理就变得透明了，而且加快了响应时间。





## 流处理不同角色返回不同结果





在VO类中有这几个方法

```java
   public static VisitDto getCurrentVisit() {
        return ((VisitDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    public boolean isManagerVisit() {
        return Objects.equals(serviceName, MANAGER_SERVICE_NAME);
    }

    public boolean isSuperManager() {
        return isManagerVisit() && extra.equals(SUPER_MANAGER);
    }

    public static <T> T handle(Handle<T> superMangerHandle
            , Handle<T> commonMangerHandle,
                        Handle<T> commonUserHandle) {
        VisitDto visitDto = VisitDto.getCurrentVisit();
        //管理员访问
        if (visitDto.isManagerVisit()) {
            //超级管理员访问
            if (visitDto.isSuperManager()) {
                if(superMangerHandle==null){
                    return null;
                }
                return superMangerHandle.doHandle(visitDto);
            } else {
                //普通管理员访问
                if(commonMangerHandle==null){
                    return null;
                }
                return commonMangerHandle.doHandle(visitDto);
            }
        } else {
            //普通用户访问
            if(commonUserHandle==null){
                return null;
            }
            return commonUserHandle.doHandle(visitDto);
        }
    }
 
    //内部接口
    public interface Handle<T> {
        T doHandle(VisitDto visitDto);
    }
```

其中的内部接口Handle接口，只有一个方法`doHandle`  所以这个接口是一个函数式接口。那么在调用的时候我们就可以使用lambda表达式去具体实现。

注意：handle方法中的每一个参数都是一个Handle的实现类。



使用案例：

```java
 @Override
    public CourseDto getCourseById(Integer id) {
        return VisitDto.
                handle(
                        //超级管理员访问
                        visitDto -> doGetCourseById(id, false),
                        visitDto -> {
                            //普通管理员访问
                            if (iCampusCourseService.managerHasCourse(id, visitDto.getCId())) {
                                return doGetCourseById(id, true);
                            }
                            return null;
                        }, visitDto -> {
                            //普通用户访问
                            if (iSubCampusCourseService.userHasCourse(id, visitDto.getCId())) {
                                return doGetCourseById(id, true);
                            }
                            return null;
                        });
    }


  private CourseDto doGetCourseById(Integer id, Boolean filterEnable) {
        Course course = getById(id);
        //课程的上架与下架只有超级管理员才有权管理
        if (filterEnable && course != null && course.getEnable() != 1) {
            return null;
        }
        return COURSE_CONVERTER.toCourseDto(course);
    }
```

  首先它调用handle方法时传入的是三个匿名的Handle实现类，然后进入它的handle方法去判断

如果是超级管理员就直接返回，如果是普通管理员或者是普通用户的话就判断下是否持有这个课程的访问权。


