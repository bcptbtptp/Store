package com.yaorange.store.web.servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yaorange.store.web.servlet.BaseServlet;

/**
 * ר���û�crud��servletģ��
 * @author Administrator
 *
 */
public abstract class CRUDServlet extends BaseServlet {
	public abstract String saveUI(HttpServletRequest req, HttpServletResponse resp);

	public abstract String save(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

	public abstract String edit(HttpServletRequest req, HttpServletResponse resp);

	public abstract String update(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException;

	public abstract String delete(HttpServletRequest req, HttpServletResponse resp);
}
