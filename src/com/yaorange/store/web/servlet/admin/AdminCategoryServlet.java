package com.yaorange.store.web.servlet.admin;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.yaorange.store.entity.Category;
import com.yaorange.store.service.CategoryService;
import com.yaorange.store.service.impl.CategoryServiceImpl;
import com.yaorange.store.web.servlet.BaseServlet;

@WebServlet("/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryServiceImpl();

	public String findAll(HttpServletRequest req, HttpServletResponse resp) {
		List<Category> list = categoryService.findAll();
		req.setAttribute("list", list);
		return "admin/category/list.jsp";
	}
	
	
	public String saveUI(HttpServletRequest req, HttpServletResponse resp) 
	{
		return "admin/category/add.jsp";
	}
	
	public String save(HttpServletRequest req, HttpServletResponse resp) 
	{
		Category category = new Category();
		try {
			BeanUtils.populate(category, req.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		categoryService.save(category);
		
		return "redirect:AdminCategoryServlet?method=findAll";
	}
	
	
	public String edit (HttpServletRequest req, HttpServletResponse resp) 
	{
		String cid = req.getParameter("cid");
		Category category = categoryService.findById(cid);
		
		req.setAttribute("category", category);
		return "admin/category/edit.jsp";
	}
	
	
	public String update (HttpServletRequest req, HttpServletResponse resp) 
	{
	
		Category category =new Category();
		try {
			BeanUtils.populate(category, req.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		categoryService.update(category);
		return "redirect:AdminCategoryServlet?method=findAll";
	}
	
	
	public String delete(HttpServletRequest req,HttpServletResponse resp)
	{
		String cid = req.getParameter("cid");
		categoryService.delete(cid);
		return "redirect:AdminCategoryServlet?method=findAll";
	}
}
