package org.simpleframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.annotation.Component;
import org.simpleframework.annotation.Controller;
import org.simpleframework.annotation.Repository;
import org.simpleframework.annotation.Service;
import org.simpleframework.util.ClassUtil;
import org.simpleframework.util.ValidationUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 容器的组成部分
 * 1、保存Class对象及其实例的载体
 * 2、容器的加载
 *    思路：
 *       1) 配置的管理与获取
 *       2)获取指定范围内的Class对象
 *       3)依据配置提取Class对象，连同实例一并存入容器
 * 3、容器的操作方式
 *   涉及容器的增删改查
 * 4、实现容器的依赖注入
 *   1) 定义相关的注解标签
 *   2) 实现创建被注解标记的成员变量实例，并将其注入到成员变量里
 *   3)依赖注入的使用
 *
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    /**
     * 存放所有被配置标记
     */
    private final Map<Class<?>,Object> beanMap = new ConcurrentHashMap();

    /**
     * 加载bean的注解列表
     */
    private static final List<Class<? extends Annotation>> BEAN_ANNOTATION =
            Arrays.asList(Component.class, Controller.class, Service.class, Repository.class);

    /**
     * 容器是否已经被加载过bean
     *
     */
    private boolean loaded = false;

    public static BeanContainer getInstance(){
        return ContatinerHolder.HOLDER.instance;
    }

    private enum ContatinerHolder{
        HOLDER;
        private BeanContainer instance;
        ContatinerHolder(){
            instance = new BeanContainer();
        }
    }

    /**
     * 是否已经加载过bean
     * @return
     */
    public boolean isLoaded(){
        return loaded;
    }

    /**
     * Bean实例的数量
     *
     * @return
     */
    public int size(){
        return beanMap.size();
    }

    /**
     * 扫描加载所有的bean
     *
     * @param packageName
     */
    public synchronized void loadBeans(String packageName){
        //判断bean容器是否被加载过
        if (isLoaded()){
            log.warn("BeanContainer has been loaded.");
            return;
        }

        Set<Class<?>> classSet = ClassUtil.extractPackageClass(packageName);
        if (ValidationUtil.isEmpty(classSet)){
            log.warn("extract noting from packageName"+packageName);
            return;
        }

        for (Class<?> clazz : classSet) {
            for (Class<? extends Annotation> annotation : BEAN_ANNOTATION) {
                //如果类上面标记了定义的注解
                if (clazz.isAnnotationPresent(annotation)){
                    //将目标类本身作为键，目标类的实例作为值，放入到beanMap中
                    beanMap.put(clazz,ClassUtil.newInstance(clazz,true));
                }
            }
        }

        loaded = true;
    }


    /**
     * 添加一个class对象及其bean实例
     *
     * @param clazz   Class对象
     * @param bean    Bean实例
     * @return        原有的Bean实例，没有则返回null
     */
    public Object addBean(Class<?> clazz,Object bean) {
        return beanMap.put(clazz,bean);
    }

    /**
     * 移除一个IOC容器管理的对象
     *
     * @param clazz  Class对象
     * @return  删除的bean实例，没有则返回null
     */
    public Object removeBean(Class<?> clazz){
        return beanMap.remove(clazz);
    }

    /**
     * 根据Class对象获取bean实例
     * @param clazz
     * @return
     */
    public Object getBean(Class<?> clazz){
        return beanMap.get(clazz);
    }

    /**
     * 获取容器管理的所有Class对象集合
     *
     * @return
     */
    public Set<Class<?>> getClasses(){
        return beanMap.keySet();
    }

    /**
     * 获取的所有的bean
     */
    public Set<Object> getBeans(){
        return new HashSet<Object>(beanMap.values());
    }

    /**
     * 根据注解筛选出Bean的Class集合
     */
    public Set<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotation){
        //1、获取beanMap所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)){
            log.warn("noting in beanMap");
            return null;
        }
        ///2、通过注解筛选被注解标记的class对象，并添加到classSet里
        HashSet<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> clazz : keySet) {
            //类是否有相关的注解标记
            if (clazz.isAnnotationPresent(annotation)){
                classSet.add(clazz);
            }
        }
        return classSet.size() > 0?classSet:null;
    }

    /**
     * 通过接口或父类获取实现类或者子类的Class集合，不包括本身
     * @param interfaceOrClass  接口class或者父类class
     * @return  class集合
     */
    public Set<Class<?>> getClassesBySuper(Class<?> interfaceOrClass){
        //1、获取beanMap所有class对象
        Set<Class<?>> keySet = getClasses();
        if (ValidationUtil.isEmpty(keySet)){
            log.warn("noting in beanMap");
            return null;
        }
        ///2、判断keySet里的元素是否是传入的接口或者类的子类，如果是就将其添加到classSet里
        HashSet<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> clazz : keySet) {
            //判断keySet里的元素是否是传入的接口或者类的子类
            if (interfaceOrClass.isAssignableFrom(clazz) && !clazz.equals(interfaceOrClass)){
                classSet.add(clazz);
            }
        }
        return classSet.size() > 0?classSet:null;
    }
}




















