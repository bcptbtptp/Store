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
		//��ȡ������
		String pid = req.getParameter("pid");
		String num =req.getParameter("num");
		//��ѯ����Ʒ����
		Product product = productService.findById(pid);
		product.setNum(Integer.valueOf(num));
		
		//�ж�session���Ƿ��й��ﳵ
		Map<String,Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		
		if(cart!=null)
		{
			//˵���Ѿ��й��ﳵ
			//�ڹ��ﳵ���жϸ���Ʒ�Ƿ����
			if(cart.containsKey(pid))
			{
				//��֮ǰnum������+num
				Product oldProduct = cart.get(pid);
				oldProduct.setNum(oldProduct.getNum()+Integer.valueOf(num));
			}
			else
			{
				//˵���ǵ�һ�μ������Ʒ
				cart.put(pid, product);
			}
		}
		else//֮ǰû�в��������ﳵ
		{
			//new cart
			cart  = new HashMap<>();
			cart.put(pid, product);
			
			//����session
			req.getSession().setAttribute("cart", cart);
		}
		
		return "redirect:cart.jsp";
	}
	
	
	public String deleteProduct(HttpServletRequest req,HttpServletResponse resp)
	{
		String pid = req.getParameter("pid");
		//�ӹ��ﳵ�н�����Ʒɾ��
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
		
		//����pid�����ﳵ�ҵ���Ӧ��Ʒ
		Map<String, Product> cart = (Map<String, Product>) req.getSession().getAttribute("cart");
		Product product = cart.get(pid);
		product.setNum(Integer.valueOf(num));
		
		//����С�ƣ��ܽ��
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
