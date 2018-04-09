package com.yaorange.store.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

/**
 * 佛祖保佑       永无BUG
 * Created by DELL on 2017/2/20.
 */
public class MybatisUtil {

    private static   SqlSessionFactory factory = getSessionFactory();

    public static  SqlSessionFactory getSessionFactory(){
        if(factory==null){
            try {
                factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("sqlMapConfig.xml"));
            } catch (IOException e) {
                System.out.println("初始化sessionFactory出异常:");
                e.printStackTrace();
            }
        }
        return factory;
    }
}
