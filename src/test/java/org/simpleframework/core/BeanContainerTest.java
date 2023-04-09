package org.simpleframework.core;

import com.chuan.controller.MainPageController;
import org.junit.jupiter.api.*;
import org.simpleframework.annotation.Controller;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {
    private static BeanContainer beanContainer;

    @BeforeAll
    static void init(){
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("加载目标类及其实例到BeanContainer")
    @Test
    @Order(1)
    public void loadBeansTest(){
        Assertions.assertEquals(false,beanContainer.isLoaded());
        beanContainer.loadBeans("com.chuan");
        Assertions.assertEquals(2,beanContainer.size());
        Assertions.assertEquals(true,beanContainer.isLoaded());
    }

    @DisplayName("根据类获取其实例")
    @Test
    @Order(2)
    public void getBeanTest(){
        MainPageController controller = (MainPageController)beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true,controller instanceof MainPageController);
    }


    @DisplayName("根据注解获取对应的实例")
    @Test
    @Order(3)
    public void getClassesByAnnotationTest(){
        Assertions.assertEquals(true,beanContainer.isLoaded());
        Assertions.assertEquals(1,beanContainer.getClassesByAnnotation(Controller.class).size());
    }

}
