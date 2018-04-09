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

		// ���жϻ������Ƿ��и�����
		if (element != null) {
			// ����У�ֱ���û�������
			result = (List<Category>) element.getObjectValue();
		} else {
			// ���û�У������ݡ��ٽ����ݷ��뻺��
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
		
		//��ջ���
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
		
		//��ջ���
		CacheUtils.removeCache("categoryCache","cList");
		
		session.commit();
		session.close();
	}

	@Override
	public void delete(String cid) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		CategoryDao dao = session.getMapper(CategoryDao.class);
		
		dao.delete(cid);
		
		//��ջ���
		CacheUtils.removeCache("categoryCache","cList");
		
		session.commit();
		session.close();
	}

}
