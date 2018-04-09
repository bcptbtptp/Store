package com.yaorange.store.web.servlet.front;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yaorange.store.entity.Product;
import com.yaorange.store.service.CategoryService;
import com.yaorange.store.service.ProductService;
import com.yaorange.store.service.impl.CategoryServiceImpl;
import com.yaorange.store.service.impl.ProductServiceImpl;
import com.yaorange.store.web.servlet.BaseServlet;

@WebServlet("/index")
public class IndexServlet extends BaseServlet {

	private CategoryService categoryService = new CategoryServiceImpl();
	private ProductService productService = new ProductServiceImpl();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		List<Category> cList = categoryService.findAll();
//		req.setAttribute("cList", cList);
		
		List<Product> pHotList = productService.findHotList();
		req.setAttribute("pHotList", pHotList);
		
		List<Product> pNewList = productService.findNewList();
		req.setAttribute("pNewList", pNewList);
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	
	
}
