package com.test;

import com.base.DaoFactory;
import com.dao.impl.UserDaoImpl;
import com.entity.User;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class test {
    public static void main(String[] args) throws SQLException, IllegalAccessException, IntrospectionException, InvocationTargetException, ClassNotFoundException, IOException, InstantiationException {
//        System.out.println(DruidUtil.getConnection());
//        System.out.println(new UserDaoImpl().findAll());
//        System.out.println(new UserDaoImpl().findById(1));
//        System.out.println(new UserDaoImpl().add(new User(101, "name", "petName", "password", "imgUrl", "realName", "sex", "address", 100, "phone")));
//        System.out.println(new UserDaoImpl().update(new User(100, "tangxz", "petName", "password", "imgUrl", "realName", "sex", "address", 60, "phone")));
//        System.out.println(new UserDaoImpl().delete(20));
//        new UserDaoImpl();
//        System.out.println(new UserDaoImpl().findById(1));
//        System.out.println(new UserDaoImpl().add(new User(8,"zz","445")));

        System.out.println(new UserDaoImpl().update(new User(8,"txzaaa","5511616")));

        System.out.println(DaoFactory.getInstance().getUserDao().findAll());
    }
}
