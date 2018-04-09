package com.yaorange.store.web.servlet.front;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;

import com.yaorange.store.entity.User;
import com.yaorange.store.service.UserServcie;
import com.yaorange.store.service.impl.UserServiceImpl;
import com.yaorange.store.utils.CookieUtils;
import com.yaorange.store.web.servlet.BaseServlet;

@WebServlet("/user")
public class UserServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private UserServcie userService = new UserServiceImpl();

	public String checkUsername(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// ��ȡ�û���
		String username = req.getParameter("username");

		// ��֤�û���
		boolean isOk = userService.checkUsername(username);
		PrintWriter out = resp.getWriter();
		// ��Ӧajax 1��ok 0����ok
		if (!isOk) {
			out.write("1");
		} else {
			out.write("0");
		}
		return null;
	}

	public String register(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		// ��֤��֤��
		String sCode = (String) req.getSession().getAttribute("code");
		String yzCode = req.getParameter("yzCode");
		if (yzCode != null && !yzCode.isEmpty() && sCode.equals(yzCode)) {
			// ��ȡ����Ϣ
			User user = new User();
			try {
				ConvertUtils.register(new DateLocaleConverter(), Date.class);
				BeanUtils.populate(user, req.getParameterMap());

				// �����û���Ϣ
				userService.save(user);

				req.setAttribute("msg", "ע��ɹ����뵽" + user.getEmail() + "ȥ������˺�!");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				req.setAttribute("errorMsg", e.getMessage());
				return "register.jsp";
			}
		} else {
			req.setAttribute("errorMsg", "��֤�����!");
			return "register.jsp";
		}

		return "msg.jsp";
	}

	public String active(HttpServletRequest req, HttpServletResponse resp) {
		String code = req.getParameter("code");
		// ���ݼ�����ȥ�����˺�
		userService.active(code);

		req.setAttribute("msg", "����ɹ�����<a href='login.jsp'>��¼</a>");
		return "msg.jsp";
	}

	public String login(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String yzCode = req.getParameter("yzCode");

		String autoLogin = req.getParameter("autoLogin");
		String remberUsername = req.getParameter("remberUsername");

		// ��֤��֤��
		String sCode = (String) req.getSession().getAttribute("code");
		if (yzCode != null && !yzCode.isEmpty() && sCode != null && sCode.equals(yzCode)) {
			User user = userService.login(username, password);
			if (user == null) {
				req.setAttribute("errorMsg", "�û���/�������!");
				return "login.jsp";
			}

			// ���û���Ϣ�浽session��
			req.getSession().setAttribute("user", user);

			if (autoLogin != null) {
				// ���û���Ϣ���浽cookie��
				Cookie uCookie = new Cookie("username", username);
				Cookie pCookie = new Cookie("password", password);
				Cookie alCookie = new Cookie("autoLogin", "true");

				uCookie.setMaxAge(60 * 60 * 24 * 7);
				pCookie.setMaxAge(60 * 60 * 24 * 7);
				alCookie.setMaxAge(60 * 60 * 24 * 7);
				resp.addCookie(uCookie);
				resp.addCookie(pCookie);
				resp.addCookie(alCookie);
			}

			if (remberUsername != null) {
				Cookie uCookie = new Cookie("username", username);
				uCookie.setMaxAge(60 * 60 * 24 * 7);
				resp.addCookie(uCookie);

				Cookie remberUsernameCookie = new Cookie("remberUsername", remberUsername);
				remberUsernameCookie.setMaxAge(60 * 60 * 24 * 7);
				resp.addCookie(remberUsernameCookie);
			}
		} else {
			req.setAttribute("errorMsg", "��֤�����!");
			return "login.jsp";
		}

		return "redirect:index";
	}

	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		// ���session
		req.getSession().invalidate();

		CookieUtils.removeCookie(req, resp, "username");
		CookieUtils.removeCookie(req, resp, "password");
		CookieUtils.removeCookie(req, resp, "antoLogin");
		CookieUtils.removeCookie(req, resp, "rememberUsername");

		return "redirect:index";
	}

}
