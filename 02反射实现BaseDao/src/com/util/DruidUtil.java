package com.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DruidUtil {
    //FIXME 注意 静态属性的写法
    private final static Properties properties;
    //这是读取数据库数据源的线程副本集
    private static ThreadLocal<DruidDataSource> threadLocal = new ThreadLocal<DruidDataSource>();
    //工具类里面的属性是不对外暴露的，所以必须加上私有修饰符
    private static DruidDataSource dataSource;
    static {
        properties = new Properties();
        try {
            properties.load(DruidUtil.class.getClassLoader().getResourceAsStream("druid.properties"));
            dataSource=(DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //得到数据源
    public static DruidDataSource getDataSource(){
        if (threadLocal.get()!=null){
            return threadLocal.get();
        }
        threadLocal.set(dataSource);
        return dataSource;
    }

    //获取数据库连接
    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}
