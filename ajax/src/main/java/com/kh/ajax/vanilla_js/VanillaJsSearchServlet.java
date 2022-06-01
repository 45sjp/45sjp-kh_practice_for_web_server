package com.kh.ajax.vanilla_js;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class VanillaJsSearchServlet
 */
@WebServlet("/vanilla-js/search")
public class VanillaJsSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 동기/비동기 요청 상관 없이 코드는 동일함
		
		// 1. 사용자 입력값 처리
		String q = request.getParameter("q");
		System.out.println("q = " + q);
		
		// 2. 업무 로직
		// (생략)
		
		// 3. view단 처리 - text | html | json | xml
		// 동기에서는 page상단 지시어로 처리되었기 때문에 생략했던 것임
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.append("<h3>요청한 검색어는 [" + q + "]입니다.</h3>");
	}

}
