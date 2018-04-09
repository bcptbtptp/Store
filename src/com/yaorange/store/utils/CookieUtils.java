package com.yaorange.store.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	/**
	 * ����cookie���ֻ�ȡ��Ӧ��cookie
	 * 
	 * @param cookies
	 * @param string
	 * @return
	 */
	public static Cookie findCookie(Cookie[] cookies, String name) {
		Cookie result = null;
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					result = cookie;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * ɾ��ĳһ��cookie
	 * 
	 * @param cookies
	 * @param name
	 */
	public static void removeCookie(HttpServletRequest req, HttpServletResponse resp, String name) {
		Cookie cookie = findCookie(req.getCookies(), name);
		if (cookie != null) {
			cookie.setMaxAge(0);
			resp.addCookie(cookie);
		}
	}
}
