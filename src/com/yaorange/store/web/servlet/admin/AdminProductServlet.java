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
	 * ��ѯ�ϼ���Ʒ
	 */
	public String findByPage(HttpServletRequest req, HttpServletResponse resp) {
		String currPage = req.getParameter("currPage");
		Page page = productService.findPage(currPage, 0);

		req.setAttribute("pageBean", page);
		return "admin/product/list.jsp";
	}
	/**
	 * ��ѯ�¼���Ʒ
	 */
	public String findByPushDown(HttpServletRequest req, HttpServletResponse resp) {
		String currPage = req.getParameter("currPage");
		Page page = productService.findPage(currPage,1);

		req.setAttribute("pageBean", page);
		return "admin/product/pushDown_list.jsp";
	}
	
	@Override
	public String saveUI(HttpServletRequest req, HttpServletResponse resp) {
		// ׼������б�
		List<Category> cList = categoryService.findAll();
		req.setAttribute("list", cList);
		return "admin/product/add.jsp";
	}

	@Override
	public String save(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// ��װproduct
		Product product = new Product();
		try {
			BeanUtils.populate(product, req.getParameterMap());
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		// ����ͼƬ
		Part part = req.getPart("upload");
		// form-data; name="upload"; filename="a.jpg"
		String ContentDisposition = part.getHeader("Content-Disposition");
		int beginIndex = ContentDisposition.lastIndexOf("=") + 2;
		// Ϊ�˱������� �ļ���������ʽ = uuid+�ļ���
		String filename = "/products/1/" + UUIDUtils.getUUID()
				+ ContentDisposition.substring(beginIndex, ContentDisposition.length() - 1);
		// ��ȡ�ļ��������·��
		String filePath = req.getServletContext().getRealPath(filename);

		// ��ȡ�ļ�������
		InputStream in = part.getInputStream();
		OutputStream output = new FileOutputStream(filePath);
		// copy
		IOUtils.copy(in, output);
		output.close();
		in.close();

		// ����ͼƬ·��
		product.setPimage(filename.replaceFirst("/", ""));

		productService.save(product);
		return "redirect:AdminProductServlet?method=findByPage";
	}

	@Override
	public String edit(HttpServletRequest req, HttpServletResponse resp) {

		String pid = req.getParameter("pid");
		// ����pid�����Ҫ�޸ĵ�product����
		Product product = productService.findById(pid);
		req.setAttribute("product", product);
		// ����б�
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
		// �Ƿ����ϴ��µ�ͼƬ
		// �ϴ���,����ͼƬ·��
		// ����ͼƬ
		Part part = req.getPart("upload");
		// form-data; name="upload"; filename="a.jpg"
		String ContentDisposition = part.getHeader("Content-Disposition");
		int beginIndex = ContentDisposition.lastIndexOf("=") + 2;
		// Ϊ�˱������� �ļ���������ʽ = uuid+�ļ���
		String shortFileName = ContentDisposition.substring(beginIndex, ContentDisposition.length() - 1);
		//�ļ�����Ϊ���ַ���˵�����µ�ͼƬ��������Ҫ�����µ�ͼƬ���Լ��������ݿ�ͼƬ·��
		if (!shortFileName.isEmpty()) {
			String filename = "/"+product.getPimage();
			// ��ȡ�ļ��������·��
			String filePath = req.getServletContext().getRealPath(filename);

			// ��ȡ�ļ�������
			InputStream in = part.getInputStream();
			OutputStream output = new FileOutputStream(filePath);
			// copy
			IOUtils.copy(in, output);
			output.close();
			in.close();
		}
		
		//�������ݿ�
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
