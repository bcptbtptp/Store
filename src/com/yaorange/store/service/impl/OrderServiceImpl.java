package com.yaorange.store.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.session.SqlSession;

import com.yaorange.store.dao.OrderDao;
import com.yaorange.store.dao.OrderItemDao;
import com.yaorange.store.entity.Order;
import com.yaorange.store.entity.OrderItem;
import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;
import com.yaorange.store.service.OrderService;
import com.yaorange.store.utils.MybatisUtil;
import com.yaorange.store.utils.UUIDUtils;

public class OrderServiceImpl implements OrderService {

	@Override
	public void save(Order order, Map<String, Product> cart) {

		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		OrderItemDao oiDao = session.getMapper(OrderItemDao.class);
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {

			// （计算出总金额）
			Double totalPrice = 0d;
			Set<String> keySet = cart.keySet();
			
			for (String pid : keySet) {
				Product product = cart.get(pid);
				// 计算总金额
				totalPrice += product.getSubTotal();
			}

			// 保存订单
			order.setTotal(totalPrice);
			orderDao.save(order);
			
			
			//保存订单项
			for (String pid : keySet) {
				Product product = cart.get(pid);
				// 将product转成订单项
				OrderItem orderItem = new OrderItem();
				orderItem.setCount(product.getNum());
				orderItem.setItemid(UUIDUtils.getUUID());
				orderItem.setOid(order.getOid());
				orderItem.setPid(pid);
				orderItem.setSubtotal(product.getSubTotal());
				// 保存订单项
				oiDao.save(orderItem);
			}

			session.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}finally{
			session.close();
		}

	}

	@Override
	public Page findPage(String uid, String currPage) {
		Page result = new Page();
		
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		OrderItemDao oiDao = session.getMapper(OrderItemDao.class);
		OrderDao orderDao = session.getMapper(OrderDao.class);
		if(currPage==null)
		{
			currPage = "1";
		}
		Integer totalCount = orderDao.getTotalCount(uid);
		result.setTotalCount(totalCount);
		result.setCurrPage(Integer.valueOf(currPage));
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("uid", uid);
		paramMap.put("beginRows", result.getBeginRows());
		paramMap.put("pageSize", result.getPageSize());
		List<Order> list = orderDao.findListByUid(paramMap);
		result.setList(list);
		session.close();
		return result;
	}

	@Override
	public void updateState(String oid) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		orderDao.updateState(oid);
		session.commit();
		session.close();
	}

	@Override
	public Order findByOid(String oid) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		
		Order result= orderDao.findByOid(oid);
		session.close();
		return result;
	}
	
	
	
	
	

}
