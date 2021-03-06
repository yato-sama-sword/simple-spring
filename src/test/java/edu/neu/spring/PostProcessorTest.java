package edu.neu.spring;

import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import edu.neu.spring.beans.factory.support.DefaultListableBeanFactory;
import edu.neu.spring.beans.factory.xml.XmlBeanDefinitionReader;
import edu.neu.spring.test.common.CustomBeanFactoryPostProcessor;
import edu.neu.spring.test.common.CustomerBeanPostProcessor;
import edu.neu.spring.test.entity.Pet;
import edu.neu.spring.test.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class PostProcessorTest {
    @Test
    public void testBeanFactoryPostProcessor() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //在所有BeanDefintion加载完成后，但在bean实例化之前，修改BeanDefinition的属性值
        CustomBeanFactoryPostProcessor beanFactoryPostProcessor = new CustomBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        User user = (User) beanFactory.getBean("user");
        Assert.equals(user.getName(), "zy");
    }

    @Test
    public void testBeanPostProcessor() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        beanDefinitionReader.loadBeanDefinitions("classpath:spring.xml");

        //添加bean实例化后的处理器
        CustomerBeanPostProcessor customerBeanPostProcessor = new CustomerBeanPostProcessor();
        beanFactory.addBeanPostProcessor(customerBeanPostProcessor);

        Pet pet = (Pet) beanFactory.getBean("pet");
        log.info(JSONUtil.toJsonStr(pet));

        //brand属性在CustomerBeanPostProcessor中被修改为wang
        Assert.equals(pet.getName(), "Wang");
    }
}
