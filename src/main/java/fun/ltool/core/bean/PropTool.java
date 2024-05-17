package fun.ltool.core.bean;

import fun.ltool.core.exceptions.BeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * JavaBean 属性工具类
 *
 * @author huangrongsong
 * @since 1.1
 */
public class PropTool {
    /**
     * get方法key
     */
    public static final String GETTER = "getter";
    /**
     * set方法key
     */
    public static final String SETTER = "setter";

    /**
     * 获取所有父类和子类field
     *
     * @param beanClass 需要获取的类
     * @return 返回field集合
     */
    public static List<Field> getAllFields(Class<?> beanClass) {
        // 获取当前类的所有字段
        List<Field> fields = new ArrayList<>(Arrays.asList(beanClass.getDeclaredFields()));

        // 获取父类的所有字段，直到Object类为止
        if (beanClass.getSuperclass() != null) {
            fields.addAll(getAllFields(beanClass.getSuperclass()));
        }
        return fields;
    }

    /**
     * 获取属性描述信息
     *
     * @param beanClass 需要获取的类
     * @return 返回描述信息数组
     */
    public static PropertyDescriptor[] getPropertyDesc(Class<?> beanClass) {
        try {
            return Arrays.stream(java.beans.Introspector.getBeanInfo(beanClass, Object.class).getPropertyDescriptors())
                    .filter(pd -> pd.getReadMethod() != null && pd.getWriteMethod() != null).toArray(PropertyDescriptor[]::new);
        } catch (Exception e) {
            throw new BeanException("获取属性描述失败", e);
        }
    }

    /**
     * 获取类所有的get和set方法
     *
     * @param beanClass 需要获取的对象
     * @return 返回map key对应propName，value对应(Map get和set方法)
     */
    public static Map<String, Map<String, Method>> getGetterAndSetterMethod(Class<?> beanClass) {
        if (beanClass == null) {
            throw new BeanException("类为null", new NullPointerException());
        }
        PropertyDescriptor[] propertyDesc = getPropertyDesc(beanClass);
        if (propertyDesc.length == 0) {
            return new HashMap<>();
        }
        Map<String, Map<String, Method>> map = new HashMap<>();
        for (PropertyDescriptor pd : propertyDesc) {
            Map<String, Method> getterSetterMap = new HashMap<>();
            Method getter = pd.getReadMethod();
            if (getter != null) {
                getterSetterMap.put(GETTER, getter);
            }
            Method setter = pd.getWriteMethod();
            if (setter != null) {
                getterSetterMap.put(SETTER, setter);
            }
            map.put(pd.getName(), getterSetterMap);
        }
        return map;
    }

    /**
     * 获取对象的值
     *
     * @param source 取值对象
     * @param method 方法名
     * @param <T>    对象泛型
     * @return 值
     */
    public static <T> Object getValue(T source, Method method) {
        if (source == null) {
            throw new BeanException("类为null", new NullPointerException());
        }
        try {
            method.setAccessible(true);
            return method.invoke(source);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanException("bean 对象取值失败， " + source, e);
        }
    }

    /**
     * 设置对象值
     *
     * @param source 需要设值的对象
     * @param method 方法
     * @param value  值
     * @param <T>    对象泛型
     */
    public static <T> void setValue(T source, Method method, Object... value) {
        if (source == null) {
            throw new BeanException("类为null", new NullPointerException());
        }
        try {
            method.setAccessible(true);
            method.invoke(source, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanException("bean 对象赋值失败， " + source, e);
        }
    }

    /**
     * 设置对象值
     *
     * @param source       需要设值的对象
     * @param propertyName 属性名称
     * @param value        值
     * @param <T>          对象泛型
     */
    public static <T> void setValue(T source, String propertyName, Object value) {
        if (source == null) {
            throw new BeanException("类为null", new NullPointerException());
        }
        try {
            Field field = source.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(source, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new BeanException("bean 对象赋值失败， " + source, e);
        }
    }
}
