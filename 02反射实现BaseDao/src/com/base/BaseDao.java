package com.base;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface BaseDao<T> {
    public List<T> findAll()throws SQLException;
    public T findById(int id)throws SQLException;
    public int add(T entity) throws SQLException, IntrospectionException, InvocationTargetException, IllegalAccessException;
    public int delete(int id)throws SQLException;
    public int update(T entity) throws SQLException, IntrospectionException, InvocationTargetException, IllegalAccessException;
}
