package com.yaorange.store.service;

import com.yaorange.store.entity.User;

public interface UserServcie {

	boolean checkUsername(String username);

	void save(User user);

	void active(String code);

	User login(String username, String password);

}
