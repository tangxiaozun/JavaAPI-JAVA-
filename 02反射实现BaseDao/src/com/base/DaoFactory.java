package com.base;

import com.dao.UserDao;
import com.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//一个产品为第四五条
/**
 * 工厂类，像service 层输送Dao的实现类
 * 必须是单例的（双重锁模式）
 */
public class DaoFactory {
    //1.私有化自身属性
    private static DaoFactory factory = null;

    //2.私有化构造函数
    private DaoFactory() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Properties prop = new Properties();
        InputStream is = DaoFactory.class.getClassLoader().getSystemResourceAsStream("daoConfig.properties");
        prop.load(is);

        String UserDaoClass = prop.getProperty("UserDaoClass");
        userDao = (UserDao) Class.forName(UserDaoClass).newInstance();
    }

    //3.提供一个共有的的得到实例的方法
    public static DaoFactory getInstance() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (factory == null) {
            synchronized (DaoFactory.class) {
                factory = new DaoFactory();
            }
        }
        return factory;
    }
    //4.提供我们需要的dao
    private static UserDao userDao = null;
    //5.共有得到的ado的方法
    public UserDao getUserDao(){
        return userDao;
    }
}
