package member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.HelloMvcUtils;
import member.model.dto.Member;
import member.model.service.MemberService;

/**
 * Servlet implementation class PasswordUpdateServlet
 */
@WebServlet("/member/passwordUpdate")
public class PasswordUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * 패스워드 수정폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/member/passwordUpdate.jsp")
		.forward(request, response);
	}

	/**
	 * 패스워드 수정 요청
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자입력값 처리
			String memberId = request.getParameter("memberId"); // session 이용 안 하고 처리해봄
			String oldPassword = HelloMvcUtils.encrypt(request.getParameter("oldPassword"), memberId);
			String newPassword = HelloMvcUtils.encrypt(request.getParameter("newPassword"), memberId);
			
			System.out.println("memberId = " + memberId);
			System.out.println("oldPassword = " + oldPassword);
			System.out.println("newPassword = " + newPassword);
			
			// 2. 업무 로직 - 기존 비밀번호가 일치하는 경우에만 비밀번호를 수정
			Member member = memberService.findByMemberId(memberId);
			String msg = "";
			String location = request.getContextPath();
			if(member != null && oldPassword.equals(member.getPassword())) {
				// 비밀번호 수정
				Member updateMember = new Member();
				updateMember.setMemberId(memberId);
				updateMember.setPassword(newPassword);
				int result = memberService.updatePassword(updateMember);
				msg = "비밀번호를 성공적으로 변경했습니다.";
				location += "/member/memberView";
			}
			else {
				// 비밀번호 수정 금지
				msg = "기존 비밀번호가 일치하지 않습니다.";
				location += "/member/passwordUpdate";
			}
			
			// 3. 리다이렉트 처리
			request.getSession().setAttribute("msg", msg);
			response.sendRedirect(location);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
