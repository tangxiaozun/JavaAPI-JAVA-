package com.company;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//不直接使用String的切片方法，使用切片方法
public class fanShe4 {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String str = "asdasasfsafsafsafas";
        String subString = str.substring(3,7);
        System.out.println(subString);

        getSub(str,3,7,String.class);
    }
    static void getSub(String str, int begin, int end, Class clazz) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        //不出现String类
        Method[] methods = clazz.getMethods();
        Object obj = null;
        for (Method m:methods){
            if (m.getName().equals("substring")){
                if (m.getParameterCount()==2){
                    obj = m.invoke(str,begin,end);
                }
            }
        }
        System.out.println(obj);

        Method m = clazz.getDeclaredMethod("substring",int.class,int.class);
        obj = m.invoke(str,begin,end);
        System.out.println(obj);

    }
}
