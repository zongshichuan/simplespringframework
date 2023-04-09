package org.simpleframework.inject;

import com.chuan.controller.MainPageController;
import com.chuan.service.impl.MainServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simpleframework.core.BeanContainer;

public class DependencyInjectorTest {
    @DisplayName("依赖注入doIoc")
    @Test
    public void doIocTest(){
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.chuan");
        Assertions.assertEquals(true, beanContainer.isLoaded());
        MainPageController mainPageController = (MainPageController)beanContainer.getBean(MainPageController.class);
        Assertions.assertEquals(true, mainPageController instanceof MainPageController);
        Assertions.assertEquals(null, mainPageController.getMainService());
        new DependencyInjector().doIoc();
        Assertions.assertNotEquals(null, mainPageController.getMainService());
        Assertions.assertEquals(true, mainPageController.getMainService() instanceof MainServiceImpl);

    }
}
