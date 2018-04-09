package com.yaorange.store.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yaorange.store.entity.Category;
import com.yaorange.store.entity.User;
import com.yaorange.store.service.CategoryService;
import com.yaorange.store.service.impl.CategoryServiceImpl;

public class BaseServlet extends HttpServlet {
	
	protected static final long serialVersionUID = 1L;
	private CategoryService categoryService =new CategoryServiceImpl();
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		initData(req);
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		String method = req.getParameter("method");
		if(null!=method && !method.isEmpty()){
			//通过反射调用对应方法
			Class classez = this.getClass();
			try {
				Method methodName = classez.getMethod(method, HttpServletRequest.class,HttpServletResponse.class);
				String result = (String) methodName.invoke(this, req,resp);
				//请求转发
				if(null!=result && !result.isEmpty())
				{
					if(result.contains("redirect:"))
					{
						resp.sendRedirect(result.replaceAll("redirect:", ""));
					}
					else
					{
						req.getRequestDispatcher(result).forward(req, resp);
					}
					
				}
//				else
//				{
//					resp.setContentType("text/html;charset=utf-8");
//					resp.getWriter().println("servlet 没有返回jsp视图!");
//				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				req.setAttribute("msg", "没有找到对应的方法"+classez.getName()+"."+method);
				req.getRequestDispatcher("msg.jsp").forward(req, resp);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		else
		{
			doGet(req,resp);
		}
		
	}

	private void initData(HttpServletRequest req) {
		List<Category> cList = categoryService.findAll();
		req.setAttribute("cList", cList);
	}

	
	protected User getUser(HttpServletRequest req) {
		return ((User)(req.getSession().getAttribute("user")));
	}
}
