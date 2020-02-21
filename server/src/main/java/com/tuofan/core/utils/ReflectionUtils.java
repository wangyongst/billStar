package com.tuofan.core.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "unchecked"})
public class ReflectionUtils {
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    public ReflectionUtils() {
    }

    public static <T> T newInstance(final Class clazz) {
        try {
            return (T)
                    clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException var2) {
            throw convertReflectionExceptionToUnchecked(var2);
        }
    }

    public static Object invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        String[] var3 = StringUtils.split(propertyName, ".");
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            String name = var3[var5];
            String getterMethodName = "get" + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
        }

        return object;
    }

    public static void invokeSetter(Object obj, String propertyName, Object value) {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");

        for (int i = 0; i < names.length; ++i) {
            String getterMethodName;
            if (i < names.length - 1) {
                getterMethodName = "get" + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
            } else {
                getterMethodName = "set" + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, getterMethodName, new Object[]{value});
            }
        }

    }

    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            Object result = null;

            try {
                result = field.get(obj);
            } catch (IllegalAccessException var5) {
                logger.error("不可能抛出的异常{}", var5.getMessage());
            }

            return result;
        }
    }

    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            try {
                field.set(obj, value);
            } catch (IllegalAccessException var5) {
                logger.error("不可能抛出的异常:{}", var5.getMessage());
            }

        }
    }

    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (Exception var6) {
                throw convertReflectionExceptionToUnchecked(var6);
            }
        }
    }

    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (Exception var5) {
                throw convertReflectionExceptionToUnchecked(var5);
            }
        }
    }

    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(fieldName, "fieldName can't be blank", new Object[0]);
        Class superClass = obj.getClass();

        while (superClass != Object.class) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(methodName, "methodName can't be blank", new Object[0]);
        Class searchType = obj.getClass();

        while (searchType != Object.class) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException var5) {
                searchType = searchType.getSuperclass();
            }
        }

        return null;
    }

    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can't be null", new Object[0]);
        Validate.notBlank(methodName, "methodName can't be blank", new Object[0]);

        for (Class searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            Method[] var4 = methods;
            int var5 = methods.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                Method method = var4[var6];
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }

        return null;
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }

    }

    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }

    }

    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    public static Class getClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class) params[index];
                }
            } else {
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    public static Class getInterfaceGenricType(final Class clazz, final int index, Class interfaceClass) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        Type[] var4 = genericInterfaces;
        int var5 = genericInterfaces.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            Type type = var4[var6];
            if (!(type instanceof ParameterizedType)) {
                logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
                return Object.class;
            }

            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (interfaceClass.isAssignableFrom((Class) parameterizedType.getRawType())) {
                Type[] params = parameterizedType.getActualTypeArguments();
                if (index < params.length && index >= 0) {
                    if (!(params[index] instanceof Class)) {
                        logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                        return Object.class;
                    }

                    return (Class) params[index];
                }

                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }

        return Object.class;
    }

    public static Class<?> getUserClass(Object instance) {
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains("$$")) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }

        return clazz;
    }

    public static Class<?> getUserClass(Class clazz) {
        if (clazz != null && clazz.getName().contains("$$")) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }

        return clazz;
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)) {
            if (e instanceof InvocationTargetException) {
                return new RuntimeException(((InvocationTargetException) e).getTargetException());
            } else {
                return e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException("Unexpected Checked Exception.", e);
            }
        } else {
            return new IllegalArgumentException(e);
        }
    }

    public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f = handler.getClass().getDeclaredField("memberValues");
        f.setAccessible(true);
        Map<String, Object> memberValues = (Map) f.get(handler);
        Object oldValue = memberValues.get(key);
        if (oldValue != null && oldValue.getClass() == newValue.getClass()) {
            memberValues.put(key, newValue);
            return oldValue;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static Annotation getAnnotationByMethod(Method method, Class annoClass) {
        Annotation[] all = method.getAnnotations();
        Annotation[] var3 = all;
        int var4 = all.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Annotation annotation = var3[var5];
            if (annotation.annotationType() == annoClass) {
                return annotation;
            }
        }

        return null;
    }

    public static boolean hasAnnotationByMethod(Method method, Class annoClass) {
        return getAnnotationByMethod(method, annoClass) != null;
    }

    public static Method getMethodByClassAndName(Class c, String methodName) {
        Method[] methods = c.getDeclaredMethods();
        Method[] var3 = methods;
        int var4 = methods.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];
            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
    }

    public static List<Field> getClassField(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList(Arrays.asList(declaredFields));
        Class superclass = clazz.getSuperclass();
        if (superclass != null) {
            fields.addAll(getClassField(superclass));
        }

        return fields;
    }
}

