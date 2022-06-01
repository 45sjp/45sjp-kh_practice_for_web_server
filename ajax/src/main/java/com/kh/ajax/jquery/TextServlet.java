package com.kh.ajax.jquery;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class TextServlet
 */
@WebServlet("/jquery/text")
public class TextServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		String q = request.getParameter("q");
		int mode = Integer.parseInt(request.getParameter("mode"));
		boolean isFinal = Boolean.parseBoolean(request.getParameter("isFinal"));
		System.out.println("q = " + q + ", mode = " + mode + ", isFinal = " + isFinal);
		
		// 2. 업무 로직
		// (생략)
		
		// 3. view단 처리 - text 처리 방식
		response.setContentType("text/plain; charset=utf-8");
		response.getWriter()
			.append("q= " + q + ", mode = " + mode + ", isFinal = " + isFinal)
			.append("\n")
			.append("데이터 처리 완료!");
	}

}
