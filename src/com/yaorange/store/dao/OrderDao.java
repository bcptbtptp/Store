package com.yaorange.store.dao;

import java.util.List;
import java.util.Map;

import com.yaorange.store.entity.Order;

public interface OrderDao {

	void save(Order order);

	Integer getTotalCount(String uid);

	List<Order> findListByUid(Map<String, Object> paramMap);

	void updateState(String oid);

	Order findByOid(String oid);
}
