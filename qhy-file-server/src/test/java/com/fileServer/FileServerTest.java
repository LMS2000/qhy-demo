package com.fileServer;


import com.fileServer.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@SpringBootTest(classes = FileServerTest.class)
public class FileServerTest {

//    @Resource
//   private S3OssClient ossClient;

    @Test
    public void fileTest() throws IOException {
//        ClassPathResource resource=new ClassPathResource("beans.xml");
//        DefaultListableBeanFactory factory=new DefaultListableBeanFactory();
//        XmlBeanDefinitionReader xmlBeanDefinitionReader=new XmlBeanDefinitionReader(factory);
//        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        FileSystemXmlApplicationContext fileSystemXmlApplicationContext=new FileSystemXmlApplicationContext("bean.xml");
        FileUtil bean = fileSystemXmlApplicationContext.getBean(FileUtil.class);

    }


}
