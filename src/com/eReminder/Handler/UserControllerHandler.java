package com.eReminder.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class UserControllerHandler implements HandlerInterceptor {
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		// to do.
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler, ModelAndView view)
			throws Exception {
		// to do.
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			String uri = request.getRequestURI();

			if (uri.contains("/home")) {
				if (request.getSession().getAttribute("UserId") != null) {

					return true;
				} else {
					redirect(request, response, "/");
					return false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	private void redirect(HttpServletRequest request,
			HttpServletResponse response, String path) throws ServletException {

		try {
			response.sendRedirect(request.getContextPath() + path);
		} catch (java.io.IOException e) {
			throw new ServletException(e);
		}
	}

}