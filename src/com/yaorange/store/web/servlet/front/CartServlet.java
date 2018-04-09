package com.yaorange.store.web.servlet.front;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yaorange.store.entity.Product;
import com.yaorange.store.service.ProductService;
import com.yaorange.store.service.impl.ProductServiceImpl;
import com.yaorange.store.web.servlet.BaseServlet;

@WebServlet("/cart")
public class CartServlet extends BaseServlet {
	
	private ProductService productService= new ProductServiceImpl();
	public String addProduct(HttpServletRequest req, HttpServletResponse resp) {
		//获取表单数据
		String pid = req.getParameter("pid");
		String num =req.getParameter("num");
		//查询出商品对象
		Product product = productService.findById(pid);
		product.setNum(Integer.valueOf(num));
		
		//判断session中是否有购物车
		Map<String,Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		
		if(cart!=null)
		{
			//说明已经有购物车
			//在购物车中判断该商品是否存在
			if(cart.containsKey(pid))
			{
				//在之前num基础上+num
				Product oldProduct = cart.get(pid);
				oldProduct.setNum(oldProduct.getNum()+Integer.valueOf(num));
			}
			else
			{
				//说明是第一次加入该商品
				cart.put(pid, product);
			}
		}
		else//之前没有操作过购物车
		{
			//new cart
			cart  = new HashMap<>();
			cart.put(pid, product);
			
			//存入session
			req.getSession().setAttribute("cart", cart);
		}
		
		return "redirect:cart.jsp";
	}
	
	
	public String deleteProduct(HttpServletRequest req,HttpServletResponse resp)
	{
		String pid = req.getParameter("pid");
		//从购物车中将该商品删除
		Map<String,Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		cart.remove(pid);
		
		return "cart.jsp";
	}
	
	public String clearCart(HttpServletRequest req,HttpServletResponse resp)
	{
		req.getSession().removeAttribute("cart");
		
		return "cart.jsp";
	}
	
	public String updateNum(HttpServletRequest req,HttpServletResponse resp) throws IOException
	{
		String pid = req.getParameter("pid");
		String num = req.getParameter("num");
		
		//根据pid到购物车找到对应商品
		Map<String, Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		Product product = cart.get(pid);
		product.setNum(Integer.valueOf(num));
		
		//计算小计，总金额
		Double total = 0d;
		for(String key : cart.keySet())
		{
			Product each = cart.get(key);
			total+=each.getSubTotal();
		}
		
		String result = product.getSubTotal()+"&"+total;
		resp.getWriter().write(result);
		return null;
	}
}
