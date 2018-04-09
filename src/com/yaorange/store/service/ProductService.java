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
	 * ���²�Ʒ�¼�״̬
	 * @param pid ��Ʒid
	 * @param i     ״̬  0:�ϼ� 1:�¼�
	 */
	void updateState(String pid, int i);

}
