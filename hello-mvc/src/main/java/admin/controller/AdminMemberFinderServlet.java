package admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class AdminMemberFinderServlet
 */
@WebServlet("/admin/memberFinder")
public class AdminMemberFinderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * select * from member where member_id like '%abc%'
	 * select * from member where member_name like '%박%'
	 * select * from member where gender like 'M'
	 * 
	 * 	=> select * from member where # like ?
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		String searchType = request.getParameter("searchType"); // member_id member_name gender
		String searchKeyword = request.getParameter("searchKeyword");
		
		Map<String, String> param = new HashMap<>();
		param.put("searchType", searchType);
		param.put("searchKeyword", searchKeyword);
		System.out.println("param = " + param);
				
		// 2. 업무 로직
		List<Member> list = memberService.findBy(param);
		System.out.println("list = " + list);
		
		// 3. view단 처리
		request.setAttribute("list", list);
		request.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp")
			.forward(request, response);
	}

}
