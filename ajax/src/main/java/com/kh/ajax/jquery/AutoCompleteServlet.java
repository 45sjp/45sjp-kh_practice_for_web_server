package com.kh.ajax.jquery;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.ajax.name.manager.NameManager;

/**
 * Servlet implementation class AutoCompleteServlet
 */
@WebServlet("/jquery/autoComplete")
public class AutoCompleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		String search = request.getParameter("search");
		System.out.println("search = " + search);
		
		// 2. 업무 로직
		List<String> names = NameManager.getInstance().getName();
		// Arrays.asList(String...)
		List<String> results = new ArrayList<>();
		
		for(String name : names) {
			// 이름이 검색어와 일치하면 결과에 추가!
			if(name.contains(search)) {
				results.add(name);
			}
		}
		
		// 3. 응답 처리
		response.setContentType("text/csv; charset=utf-8");
		PrintWriter out = response.getWriter();
		
		for(int i = 0; i < results.size(); i++) {
			out.append(results.get(i));
			
			if(i != results.size() - 1) out.append(",");
		}
	}

}
