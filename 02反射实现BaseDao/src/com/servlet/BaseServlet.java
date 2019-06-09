package com.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class BaseServlet
 */
public class BaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //真正请求到达的servlet中没有提供服务的方法，所以进入父类中service方法
            System.out.println("请求过来了");

            //根据请求的url，分发请求：分发给对应的servlet的对应的方法——反射
            //UserServlet?method=login：UserServlet类中的login()方法
            //UserServlet?method=reg：UserServlet类中的reg()方法
            //EmailServlet?method=add：EmailSerlet类中的add()方法
            //写代码的时候，不知道我要执行哪个方法，运行的时候，要进入对应的方法


            //得到进入该请求的对应的servlet
            Class clazz = getClass();//得到当前真正请求的servlet的Class信息：UserServlet,EmailServlet
            //得到要调用对应方法的名字
            String methodName = request.getParameter("method");

            //得到要调用的方法执行
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            String result = (String) method.invoke(this, request, response);
            System.out.println(method);
            System.out.println(result);

            //------------------------------------------------------------------------------------

            //1.解析字符串，判断是请求转发还是重定向

            //2.解析字符串，根据地址返回响应

            //之前的增删改查，全部改成用BaseServlet

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求携带的method方法参数
        String methodName = req.getParameter("method");

        // 获取指定类的字节码对象
        Class<? extends BaseServlet> clazz = this.getClass();//这里的this指的是继承BaseServlet对象
        // 通过类的字节码对象获取方法的字节码对象
        Method method = null;
        try {
            method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if ("add".equals(methodName)) {
            add();
        } else if ("del".equals(methodName)) {
            del();
        } else if ("update".equals(methodName)) {
            update();
        } else if ("find".equals(methodName)) {
            find();
        }
    }

    //查询
    private void find() {
        //执行service层代码
    }

    //更新
    private void update() {
        //执行service层代码
    }

    //删除
    private void del() {
        //执行service层代码
    }

    //添加
    private void add() {
        //执行service层代码
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

}
