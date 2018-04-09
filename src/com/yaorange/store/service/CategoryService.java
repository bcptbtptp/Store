package com.yaorange.store.service;

import java.util.List;

import com.yaorange.store.entity.Category;

public interface CategoryService {

	List<Category> findAll();

	void save(Category category);

	Category findById(String cid);

	void update(Category category);

	void delete(String cid);

}
