package com.yaorange.store.service;

import java.util.List;

import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;

public interface ProductService {

	List<Product> findHotList();

	List<Product> findNewList();

	Page findPage(String cid, String pageNo);

	Product findById(String pid);

	Page findPage(String currPage, Integer pflag);

	void save(Product product);

	void update(Product product);

	/**
	 * 更新产品下架状态
	 * @param pid 产品id
	 * @param i     状态  0:上架 1:下架
	 */
	void updateState(String pid, int i);

}
