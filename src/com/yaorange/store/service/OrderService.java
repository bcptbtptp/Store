package com.yaorange.store.service;

import java.util.Map;

import com.yaorange.store.entity.Order;
import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;

public interface OrderService {

	/**
	 * 保存订单
	 * @param order  订单对象
	 * @param cart    购物车map
	 */
	void save(Order order, Map<String, Product> cart);

	Page findPage(String uid, String currPage);

	/**
	 * 根据订单号修改订单状态
	 * @param r6_Order
	 */
	void updateState(String r6_Order);

	/**
	 * 根据订单编号获取订单对象
	 * @param oid
	 * @return
	 */
	Order findByOid(String oid);

}
