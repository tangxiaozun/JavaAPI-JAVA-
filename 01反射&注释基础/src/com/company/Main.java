package com.company;

/**
 * class对象
 * 一个类会有一个与之对应的Class类的实例，在类加载的时候诞生的对象，代表的是该类的信息
 * 获得一个类的Class实例的方式有三种
 * 通过对象
 * 通过.Class
 * 通过Class.forname()
 */
public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
//	    Class clazz1 = new Student().getClass();
//        Class clazz2 = new Student().getClass();
//
//        System.out.println(clazz1==clazz2);
//
//        Class clazz3 = Student.class;
//
//        Class clazz4 = Class.forName("com.company.Student");
//        System.out.println(clazz1==clazz3);
//        System.out.println(clazz1==clazz4);
//
//        clazz1.getConstructors();


//        System.out.println(Student.name);
//        Student.test();

        Class c = Student.class;
        System.out.println(c.getFields());

//        Class cc =Class.forName("com.company.Student");

//        new SubStudent();
    }

}
class Student{
    static {
        System.out.println("类加载");
    }
    public static void test(){

    }
    static String name;

    public static void setName(String name) {
        System.out.println(name);
        Student.name = name;
    }
    public static String getName() {
        System.out.println(name);
        return Student.name;
    }

}
class SubStudent extends Student{

}