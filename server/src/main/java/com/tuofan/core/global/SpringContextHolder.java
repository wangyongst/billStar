package com.tuofan.core.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Lazy(false)
@SuppressWarnings("unchecked")
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {


    private static ApplicationContext applicationContext = null;

    private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

    public SpringContextHolder() {
    }

    public static ApplicationContext getApplicationContext() {
        assertContextInjected();
        return applicationContext;
    }

    public static <T> T getBean(String name) {
        assertContextInjected();
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    public static void clearHolder() {
        if (logger.isDebugEnabled()) {
            logger.debug("清除SpringContextHolder中的ApplicationContext: {}", applicationContext);
        }

        applicationContext = null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public void destroy() throws Exception {
        clearHolder();
    }

    private static void assertContextInjected() {
        Assert.notNull(applicationContext, "applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBeansOfType(requiredType);
    }

    public static <T> List<T> getBeans(Class<T> requiredType) {
        assertContextInjected();
        Map<String, T> beansOfType = applicationContext.getBeansOfType(requiredType);
        return (List) (beansOfType.isEmpty() ? Collections.emptyList() : new ArrayList(beansOfType.values()));
    }
}