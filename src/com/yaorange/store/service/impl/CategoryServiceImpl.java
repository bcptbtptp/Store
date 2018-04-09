package com.yaorange.store.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.yaorange.store.dao.CategoryDao;
import com.yaorange.store.entity.Category;
import com.yaorange.store.service.CategoryService;
import com.yaorange.store.utils.CacheUtils;
import com.yaorange.store.utils.MybatisUtil;
import com.yaorange.store.utils.UUIDUtils;

import net.sf.ehcache.Element;

public class CategoryServiceImpl implements CategoryService {

	@Override
	public List<Category> findAll() {
		List<Category> result = null;
		
		Element element = CacheUtils.get("cList","categoryCache");

		// 先判断缓存中是否有该数据
		if (element != null) {
			// 如果有，直接用缓存数据
			result = (List<Category>) element.getObjectValue();
		} else {
			// 如果没有，查数据。再将数据放入缓存
			SqlSession session = MybatisUtil.getSessionFactory().openSession();
			CategoryDao dao = session.getMapper(CategoryDao.class);
			result = dao.findAll();
			session.close();
			element = new Element("cList", result);
			CacheUtils.put(element,"categoryCache");
		}

		return result;
	}

	@Override
	public void save(Category category) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		CategoryDao dao = session.getMapper(CategoryDao.class);
		
		category.setCid(UUIDUtils.getUUID());
		dao.save(category);
		
		//清空缓存
		CacheUtils.removeCache("categoryCache","cList");
		
		session.commit();
		session.close();
	}

	@Override
	public Category findById(String cid) {
		
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		CategoryDao dao = session.getMapper(CategoryDao.class);
		Category category = dao.findById(cid);
		session.close();
		return category;
	}

	@Override
	public void update(Category category) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		CategoryDao dao = session.getMapper(CategoryDao.class);
		
		dao.update(category);
		
		//清空缓存
		CacheUtils.removeCache("categoryCache","cList");
		
		session.commit();
		session.close();
	}

	@Override
	public void delete(String cid) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		CategoryDao dao = session.getMapper(CategoryDao.class);
		
		dao.delete(cid);
		
		//清空缓存
		CacheUtils.removeCache("categoryCache","cList");
		
		session.commit();
		session.close();
	}

}
