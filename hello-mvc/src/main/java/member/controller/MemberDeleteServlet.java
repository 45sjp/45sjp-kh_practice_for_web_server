package member.controller;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class MemberDeleteServlet
 */
@WebServlet("/member/memberDelete")
public class MemberDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request, response);
//		RequestDispatcher reqDispatcher = request.getRequestDispatcher("/WEB-INF/views/member/memberDelete.jsp");
//		reqDispatcher.forward(request, response);
//	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			
			// 사용자 입력값 처리
			Member loginMember = (Member) session.getAttribute("loginMember");
			
			// 서비스 로직 호출
			int result = memberService.deleteMember(loginMember.getMemberId());
			
			// 탈퇴 후 처리 - 세션폐기, 쿠키폐기
			// 쿠키폐기
			Cookie cookie = new Cookie("saveId", loginMember.getMemberId());
			cookie.setPath(request.getContextPath());
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			
			// 모든 세션 속성 제거(session.invalidate() 대신)
			Enumeration<String> names = session.getAttributeNames();
			while(names.hasMoreElements()) {
				String name = names.nextElement();
				session.removeAttribute(name);
			}
			
			// 리다이렉트 처리
			request.setAttribute("msg", "회원탈퇴가 완료되었습니다.");
			response.sendRedirect(request.getContextPath() + "/");
		} catch (Exception e) {
			e.getStackTrace();
			throw e;
		}
	}

}
