package com.dao.impl;

import com.base.impl.BaseDaoImpl;
import com.dao.UserDao;
import com.entity.User;
import com.util.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
    //连接
//    private Connection conn = DruidUtil.getConnection();
//    private QueryRunner qr = new QueryRunner();
//
//    @Override
//    public List<User> findAll() throws SQLException {
//        String sql = "select * from user";
//        List<User> list = qr.query(conn,sql,new BeanListHandler<>(User.class));
//        return list;
//    }
//
//    @Override
//    public User findById(int id) throws SQLException {
//        String sql = "select * from user where user_id = ?";
//        User user = qr.query(conn,sql,new BeanHandler<>(User.class),new Object[] {id});
//        System.out.println(user);
//        return user;
//    }
//
//    @Override
//    public int add(User user) throws SQLException {
//        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
//        int result = qr.update(conn,sql,new Object[] {user.getUser_id(),user.getUser_name(),user.getUser_petName(),user.getUser_password(),user.getUser_imgUrl(),user.getUser_realName(),user.getUser_sex(),user.getUser_address(),user.getUser_score(),user.getUser_phone()});
//        return result;
//    }
//
//    @Override
//    public int delete(int id) throws SQLException {
//        String sql = "delete from user where user_id=?";
//        int result = qr.update(conn,sql,new Object[] {id});
//        return result;
//    }
//
//    @Override
//    public int update(User user) throws SQLException {
//        String sql = "update user set user_name=?, user_petName=?, user_password=?, user_imgUrl=?, user_realName=?, user_sex=?, user_address=?, user_score=?, user_phone=? where user_id=?";
//        int result = qr.update(conn,sql,new Object[] {user.getUser_name(),user.getUser_petName(),user.getUser_password(),user.getUser_imgUrl(),user.getUser_realName(),user.getUser_sex(),user.getUser_address(),user.getUser_score(),user.getUser_phone(),user.getUser_id()});
//        return result;
//    }
}
