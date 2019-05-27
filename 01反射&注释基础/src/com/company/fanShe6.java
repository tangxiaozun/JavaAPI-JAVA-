package com.company;

import java.lang.reflect.Field;

/**
 * 创建一个工具类，为对象的属性赋值
 */

public class fanShe6 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Person p = new Person();
        Student stu = new Student();
//        setProperty(p, "name", "张三");
        System.out.println("---------");
        setProperty(stu, "name", "高峰");
    }

    //给任一对象的任意属性赋值
    //给oj的pName赋值为value
    static void setProperty(Object obj, String pName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class clazz = obj.getClass();

        Field field = clazz.getDeclaredField(pName);//根据属性名得到属性信息

        field.set(obj, value);

    }
}
