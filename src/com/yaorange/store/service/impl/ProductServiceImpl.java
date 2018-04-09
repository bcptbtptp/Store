package com.yaorange.store.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.yaorange.store.dao.ProductDao;
import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;
import com.yaorange.store.service.ProductService;
import com.yaorange.store.utils.CacheUtils;
import com.yaorange.store.utils.MybatisUtil;
import com.yaorange.store.utils.UUIDUtils;

import net.sf.ehcache.Element;

public class ProductServiceImpl implements ProductService {

	@Override
	public List<Product> findHotList() {
		List<Product> result = null;
		Element element = CacheUtils.get("pHotList", "categoryCache");
		if (element != null) {
			result = (List<Product>) element.getObjectValue();
		} else {
			SqlSession session = MybatisUtil.getSessionFactory().openSession();
			ProductDao dao = session.getMapper(ProductDao.class);
			result = dao.findHotList();
			element = new Element("pHotList", result);
			CacheUtils.put(element, "categoryCache");
			session.close();
		}

		return result;
	}

	@Override
	public List<Product> findNewList() {
		List<Product> result = null;
		Element element = CacheUtils.get("pNewList", "categoryCache");
		if (element != null) {
			result = (List<Product>) element.getObjectValue();
		} else {
			SqlSession session = MybatisUtil.getSessionFactory().openSession();
			ProductDao dao = session.getMapper(ProductDao.class);
			result = dao.findNewList();
			element = new Element("pNewList", result);
			CacheUtils.put(element, "categoryCache");
			session.close();
		}

		return result;
	}

	@Override
	public Page findPage(String cid, String pageNo) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		ProductDao dao = session.getMapper(ProductDao.class);
		
		Page page = new Page();
		if(pageNo==null||pageNo.isEmpty())
		{
			pageNo="1";
		}
		if(Integer.valueOf(pageNo)<1)
		{
			pageNo="1";
		}
		
		int totalCount = dao.getTotalCountByCid(cid);
		page.setTotalCount(totalCount);
		
		page.setCurrPage(Integer.parseInt(pageNo));
		
		Map<String, Object> map = new HashMap<>();
		map.put("cid",cid);
		map.put("beginRows", page.getBeginRows());
		map.put("pageSize", page.getPageSize());
		
		List<Product> list = dao.findListByCid(map);
		page.setList(list);
		
		
		session.close();
		return page;
	}

	@Override
	public Page findPage(String pageNo, Integer pflag) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		ProductDao dao = session.getMapper(ProductDao.class);
		
		Page page = new Page();
		if(pageNo==null||pageNo.isEmpty())
		{
			pageNo="1";
		}
		if(Integer.valueOf(pageNo)<1)
		{
			pageNo="1";
		}
		
		int totalCount = dao.getTotalCount(pflag);
		page.setTotalCount(totalCount);
		
		page.setCurrPage(Integer.parseInt(pageNo));
		
		Map<String, Object> map = new HashMap<>();
		map.put("beginRows", page.getBeginRows());
		map.put("pageSize", page.getPageSize());
		map.put("pflag", pflag);
		List<Product> list = dao.findList(map);
		page.setList(list);
		
		
		session.close();
		return page;
	}

	
	
	@Override
	public Product findById(String pid) {
		
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		ProductDao dao = session.getMapper(ProductDao.class);
		
		Product result = dao.findById(pid);
		
		session.close();
		return result;
	}

	@Override
	public void save(Product product) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		ProductDao dao = session.getMapper(ProductDao.class);
		
		product.setPdate(new Date());
		product.setPflag(0);
		product.setPid(UUIDUtils.getUUID());
		dao.save(product);
		
		CacheUtils.removeCache("categoryCache", "pNewList");
		session.commit();
		session.close();
	}

	@Override
	public void update(Product product) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		ProductDao dao = session.getMapper(ProductDao.class);
		
		dao.update(product);
		
		CacheUtils.removeCache("categoryCache", "pNewList");
		session.commit();
		session.close();
		
	}

	@Override
	public void updateState(String pid, int i) {
		SqlSession session = MybatisUtil.getSessionFactory().openSession();
		ProductDao dao = session.getMapper(ProductDao.class);
		
		Product product = new Product();
		product.setPid(pid);
		product.setPflag(i);
		dao.updateState(product);
		
		CacheUtils.removeCache("categoryCache", "pNewList");
		session.commit();
		session.close();
	}

}
