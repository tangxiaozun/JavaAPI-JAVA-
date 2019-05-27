package com.company;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 在List<Integer>中，实现添加Sreing字符串对象
 */
public class fanShe5 {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //泛型是在运行的时候起作用给E赋值为Integer
        List<Integer> list = new ArrayList<Integer>();
        list.add(11);
        list.add(42);

        //从类中得到adc的信息，add(Object obj)
        Method m = list.getClass().getDeclaredMethod("add",Object.class);
        m.invoke(list,"abc");
        m.invoke(list,new Person());


    }
}
