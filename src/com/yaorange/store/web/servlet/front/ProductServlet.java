package com.yaorange.store.web.servlet.front;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;
import com.yaorange.store.service.ProductService;
import com.yaorange.store.service.impl.ProductServiceImpl;
import com.yaorange.store.web.servlet.BaseServlet;

@WebServlet("/product")
public class ProductServlet extends BaseServlet {

	private ProductService productService = new ProductServiceImpl();
	public String productList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cid = req.getParameter("cid");
		String pageNo = req.getParameter("pageNo");

		Page page =  productService.findPage(cid,pageNo);
		req.setAttribute("page", page);
		req.setAttribute("cid", cid);
		return "product_list.jsp";
	}
	
	
	public String details(HttpServletRequest req, HttpServletResponse resp)
	{
		String pid = req.getParameter("pid");
		//根据pid查出对应产品
		Product product = productService.findById(pid);
		
		//传递model数据
		req.setAttribute("p", product);
		return "product_info.jsp";
	}
}
