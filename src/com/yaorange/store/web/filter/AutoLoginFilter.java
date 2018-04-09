package com.yaorange.store.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.yaorange.store.entity.User;
import com.yaorange.store.service.UserServcie;
import com.yaorange.store.service.impl.UserServiceImpl;
import com.yaorange.store.utils.CookieUtils;

@WebFilter("/*")
public class AutoLoginFilter implements Filter {

	public AutoLoginFilter() {
	}

	@Override
	public void destroy() {
	}

	private UserServcie userService = new UserServiceImpl();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 1，检查是否已经登录
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		HttpSession session = httpRequest.getSession();
		User user = (User) session.getAttribute("user");
		// 1,1 没有登录
		if (user == null) {
			// 1,1,1 去cookie中找用户信息
			Cookie[] cookies = httpRequest.getCookies();
			user = new User();

			if (cookies != null) {
				Cookie username = CookieUtils.findCookie(cookies, "username");
				Cookie password = CookieUtils.findCookie(cookies, "password");
				Cookie autoLogin =  CookieUtils.findCookie(cookies, "autoLogin");
				if (autoLogin != null && username != null && username != null) {
					user = userService.login(username.getValue(), password.getValue());
					session.setAttribute("user", user);
				}
			}
		}
		// 放行
		chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
