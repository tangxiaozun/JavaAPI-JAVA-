# 反射&注解实现BaseDao

## 1. BaseDao是什么？

用来减少代码冗余，一般的数据库操作，每一个表对应的每一个实体类，都需要一个Dao层来做增删查改，这样的话，每一个实体类对应的Dao层都会有很多相似的代码，这样就会存在代码冗余的现象。BaseDao就是用来解决这种代码冗余的现象的，一个BaseDao兼容全部的实体类。后面会介绍反射在BaseDao中的应用。接下来就放代码吧。

## 2.BaseDao的实现代码

### 2.1 BaseDao接口

```java
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
```

### 2.2 BaseDaoImpl ：BaseDao接口的实现类



```java
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
    //我们要操作的表名
    private String tableName;
	
    public BaseDaoImpl() {
        conn = DruidUtil.getConnection();
        qr = new QueryRunner();

        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) type;
        clazz = (Class<T>) pt.getActualTypeArguments()[0];

        tableName = clazz.getSimpleName();
    }

	//查找全部
    @Override
    public List<T> findAll() throws SQLException {
        String sql = "select * from " + tableName;
        List<T> list = qr.query(conn, sql, new BeanListHandler<>(clazz));
        return list;
    }

	//查找一个
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

	//增加一个
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

	//删除一个
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

	//修改一个
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

```

### 2.3 UserDao接口

```Java
package com.dao;
import com.base.BaseDao;
import com.entity.User;

public interface UserDao extends BaseDao<User> {
}
```

### 2.4 UserDaoImpl：UserDao接口的实现类

```Java
package com.dao.impl;

import com.base.impl.BaseDaoImpl;
import com.dao.UserDao;
import com.entity.User;

public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
}
```

### 2.5 main方法使用：

```java
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
        System.out.println(new UserDaoImpl().update(new User(8,"txzaaa","5511616")));

    }
}
```

### 2.6 DruidUtil

```java
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

```

### 2.7 druid.properties

```properties
driverClassName=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/basedao?useUnicode=true&characterEncoding=utf-8
username=root
password=5516
maxActice=100
maxWait=10000
maxIdle=1
```

### 2.8 