package com.company;

import java.lang.reflect.Field;

public class fanShe3 {
    public static void main(String[] args) {

    }

    void test(Class clazz) throws IllegalAccessException, InstantiationException {
        Object obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();

        //得到该属性的类型
        Class fType = fields[0].getType();
        fields[0].set(obj, getValue(fType));
    }

    Object getValue(Class fType) {
        if (fType.getClass().getName().equals("java.lang.String")) {
            return "张三";
        } else if (fType.getClass().getName().equals("java.lang.Integer")) {
            return 10;
        }
        return null;
    }
}
