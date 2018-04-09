package com.yaorange.store.service.impl;

import org.apache.ibatis.session.SqlSession;

import com.yaorange.store.dao.UserDao;
import com.yaorange.store.entity.User;
import com.yaorange.store.service.UserServcie;
import com.yaorange.store.utils.MailUtils;
import com.yaorange.store.utils.MybatisUtil;
import com.yaorange.store.utils.UUIDUtils;

public class UserServiceImpl implements UserServcie {

	
	@Override
	public boolean checkUsername(String username) {
		boolean result = false;
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		UserDao	userDao = session.getMapper(UserDao.class);
		int count = userDao.getCountByUsername(username);
		if(count>0)
		{
			result = true;
		}
		session.close();
		return result;
	}
	@Override
	public void save(User user) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		UserDao useDao = session.getMapper(UserDao.class);
		user.setUid(UUIDUtils.getUUID());
		
		if(checkUsername(user.getUsername()))
		{
			throw new RuntimeException("改用户名已经存在！");
		}
		//设置状态
		user.setState(0);//未激活
		//设置激活码
		user.setCode(UUIDUtils.getUUID());
		useDao.save(user);
		
		//发送激活码邮件
		MailUtils.sendMail(user.getEmail(), user.getCode());
		session.commit();
		session.close();
	}
	@Override
	public void active(String code) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		UserDao useDao = session.getMapper(UserDao.class);
		useDao.active(code);
		
		session.commit();
		session.close();
	}
	@Override
	public User login(String username, String password) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		UserDao userDao = session.getMapper(UserDao.class);
	
		User user = userDao.findUserByUP(username,password);
	
		session.close();
		return user;
	}
	
	
}
