package com.yaorange.store.web.servlet.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;

import com.yaorange.store.entity.Category;
import com.yaorange.store.entity.Page;
import com.yaorange.store.entity.Product;
import com.yaorange.store.service.CategoryService;
import com.yaorange.store.service.ProductService;
import com.yaorange.store.service.impl.CategoryServiceImpl;
import com.yaorange.store.service.impl.ProductServiceImpl;
import com.yaorange.store.utils.UUIDUtils;

@WebServlet("/AdminProductServlet")
@MultipartConfig
public class AdminProductServlet extends CRUDServlet {

	private static final long serialVersionUID = -8994969512045000965L;
	private ProductService productService = new ProductServiceImpl();
	private CategoryService categoryService = new CategoryServiceImpl();

	/**
	 * 查询上架商品
	 */
	public String findByPage(HttpServletRequest req, HttpServletResponse resp) {
		String currPage = req.getParameter("currPage");
		Page page = productService.findPage(currPage, 0);

		req.setAttribute("pageBean", page);
		return "admin/product/list.jsp";
	}
	/**
	 * 查询下架商品
	 */
	public String findByPushDown(HttpServletRequest req, HttpServletResponse resp) {
		String currPage = req.getParameter("currPage");
		Page page = productService.findPage(currPage,1);

		req.setAttribute("pageBean", page);
		return "admin/product/pushDown_list.jsp";
	}
	
	@Override
	public String saveUI(HttpServletRequest req, HttpServletResponse resp) {
		// 准备类别列表
		List<Category> cList = categoryService.findAll();
		req.setAttribute("list", cList);
		return "admin/product/add.jsp";
	}

	@Override
	public String save(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// 封装product
		Product product = new Product();
		try {
			BeanUtils.populate(product, req.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// 保存图片
		Part part = req.getPart("upload");
		// form-data; name="upload"; filename="a.jpg"
		String ContentDisposition = part.getHeader("Content-Disposition");
		int beginIndex = ContentDisposition.lastIndexOf("=") + 2;
		// 为了避免重名 文件名命名方式 = uuid+文件名
		String filename = "/products/1/" + UUIDUtils.getUUID()
				+ ContentDisposition.substring(beginIndex, ContentDisposition.length() - 1);
		// 获取文件保存完成路径
		String filePath = req.getServletContext().getRealPath(filename);

		// 获取文件输入流
		InputStream in = part.getInputStream();
		OutputStream output = new FileOutputStream(filePath);
		// copy
		IOUtils.copy(in, output);
		output.close();
		in.close();

		// 设置图片路径
		product.setPimage(filename.replaceFirst("/", ""));

		productService.save(product);
		return "redirect:AdminProductServlet?method=findByPage";
	}

	@Override
	public String edit(HttpServletRequest req, HttpServletResponse resp) {

		String pid = req.getParameter("pid");
		// 根据pid查出需要修改的product对象
		Product product = productService.findById(pid);
		req.setAttribute("product", product);
		// 类别列表
		List<Category> cList = categoryService.findAll();
		req.setAttribute("list", cList);
		return "admin/product/edit.jsp";
	}

	@Override
	public String update(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Product product = new Product();
		try {
			BeanUtils.populate(product, req.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		// 是否有上传新的图片
		// 上传了,更新图片路径
		// 保存图片
		Part part = req.getPart("upload");
		// form-data; name="upload"; filename="a.jpg"
		String ContentDisposition = part.getHeader("Content-Disposition");
		int beginIndex = ContentDisposition.lastIndexOf("=") + 2;
		// 为了避免重名 文件名命名方式 = uuid+文件名
		String shortFileName = ContentDisposition.substring(beginIndex, ContentDisposition.length() - 1);
		//文件名不为空字符，说明有新的图片过来，需要保存新的图片，以及更新数据库图片路径
		if (!shortFileName.isEmpty()) {
			String filename = "/"+product.getPimage();
			// 获取文件保存完成路径
			String filePath = req.getServletContext().getRealPath(filename);

			// 获取文件输入流
			InputStream in = part.getInputStream();
			OutputStream output = new FileOutputStream(filePath);
			// copy
			IOUtils.copy(in, output);
			output.close();
			in.close();
		}
		
		//更新数据库
		productService.update(product);

		return "redirect:AdminProductServlet?method=findByPage";
	}

	@Override
	public String delete(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String pushDown(HttpServletRequest req, HttpServletResponse resp) {
		String pid = req.getParameter("pid");
		productService.updateState(pid,1);
		return "redirect:AdminProductServlet?method=findByPage";
	}

}
