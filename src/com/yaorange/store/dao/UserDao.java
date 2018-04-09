package com.yaorange.store.dao;

import com.yaorange.store.entity.User;

public interface UserDao {

	int getCountByUsername(String username);

	void save(User user);

	void active(String code);

	User findUserByUP(String username, String password);

}
