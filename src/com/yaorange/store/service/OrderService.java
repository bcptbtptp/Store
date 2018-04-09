package com.yaorange.store.service;

import java.util.Map;

import com.yaorange.store.entity.Order;
import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;

public interface OrderService {

	/**
	 * ���涩��
	 * @param order  ��������
	 * @param cart    ���ﳵmap
	 */
	void save(Order order, Map<String, Product> cart);

	Page findPage(String uid, String currPage);

	/**
	 * ���ݶ������޸Ķ���״̬
	 * @param r6_Order
	 */
	void updateState(String r6_Order);

	/**
	 * ���ݶ�����Ż�ȡ��������
	 * @param oid
	 * @return
	 */
	Order findByOid(String oid);

}
