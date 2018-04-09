package com.yaorange.store.dao;

import java.util.List;
import java.util.Map;

import com.yaorange.store.entity.Product;

public interface ProductDao {

	List<Product> findHotList();

	List<Product> findNewList();

	int getTotalCountByCid(String cid);

	List<Product> findListByCid(Map<String, Object> map);

	Product findById(String pid);

	int getTotalCount(Integer pflag);

	List<Product> findList(Map<String, Object> map);

	void save(Product product);

	void update(Product product);

	void updateState(Product product);
}
