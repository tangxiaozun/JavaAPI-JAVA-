package com.base.impl;

import com.annotation.Id;
import com.base.BaseDao;
import com.util.DruidUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BaseDaoImpl<T> implements BaseDao<T> {

    //得到数据库连接对象
    private Connection conn;
    //引入QueryRunner
    private QueryRunner qr;
    //我们要操作的是哪一个xxDAO的字节码对象
    private Class<T> clazz;
    //我们要操作的表名 通过注解获得
    private String tableName;
    //初始化
    public BaseDaoImpl() {
        conn = DruidUtil.getConnection();
        qr = new QueryRunner();

        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;
        clazz = (Class<T>) pt.getActualTypeArguments()[0];

        tableName = clazz.getSimpleName();
    }


    @Override
    public List<T> findAll() throws SQLException {
        String sql = "select * from " + tableName;
        List<T> list = qr.query(conn, sql, new BeanListHandler<>(clazz));
        return list;
    }

    @Override
    public T findById(int id) throws SQLException {
        String IdfileldName = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                IdfileldName = field.getName();
            }
        }
        String sql = "select * from " + tableName + " where " + IdfileldName + "=?";
        T entity = qr.query(conn, sql, new BeanHandler<>(clazz), new Object[]{id});
        return entity;
    }

    @Override
    public int add(Object entity) throws SQLException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        //1.拼接sql语句
        //insert into user(字段1,....) values(?,...)
        Class claz = entity.getClass();
        String sql = "insert into " + tableName + "(";// values(?,?,?)
        Field[] fields = claz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (i != fields.length - 1) {
                sql += field.getName() + ",";
            } else {
                sql += field.getName() + ")";
            }
        }
        sql += " values(";
        for (int i = 0; i < fields.length; i++) {
            if (i != fields.length - 1) {
                sql += " ?,";
            } else {
                sql += " ?)";
            }
        }
        //得到参数
        Object[] obj = new Object[fields.length];
        Object FieldValue = null;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), claz);
            FieldValue = pd.getReadMethod().invoke(entity, null);
            obj[i] = FieldValue;
        }
        int result = qr.update(conn, sql, obj);
        return result;
    }

    @Override
    public int delete(int id) throws SQLException {
        String IdfileldName = null;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                IdfileldName = field.getName();
            }
        }
        String sql = "delete * from " + tableName + " where " + IdfileldName + "=?";
        int result = qr.update(conn, sql, new Object[]{id});

        return result;
    }

    @Override
    public int update(Object entity) throws SQLException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        //update table set 字段名=?,....where id=?
        Class claz = entity.getClass();
        String sql = "update " + tableName + " set ";// values(?,?,?)
        Field[] fields = claz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.isAnnotationPresent(Id.class)) {
                sql += field.getName() + "=? ,";
            }
        }
        //去掉最后的一个问号
        sql = sql.substring(0, sql.length() - 1);
        sql += " where ";
        String IdfieldName = null;
        for (Field fields1 : fields) {
            if (fields1.isAnnotationPresent(Id.class)) {
                IdfieldName = fields1.getName();
            }
        }
        sql += IdfieldName + "=?";

        //得到参数
        Object[] obj = new Object[fields.length];
        Object FieldValue = null;
        int j = 0;//表示加到数组中的位置
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), claz);
            FieldValue = pd.getReadMethod().invoke(entity, null);
            if (!field.isAnnotationPresent(Id.class)) {
                obj[j] = FieldValue;
                j++;
            } else {
                obj[fields.length - 1] = FieldValue;
            }
        }
        int result = qr.update(conn, sql, obj);
        return result;
    }
}
