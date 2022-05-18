package member.controller;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.HelloMvcUtils;
import member.model.dto.Member;
import member.model.dto.MemberRole;
import member.model.service.MemberService;

/**
 * Servlet implementation class MemberEnrollServlet
 */
@WebServlet("/member/memberEnroll")
public class MemberEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();

	/**
	 * 회원가입폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request
			.getRequestDispatcher("/WEB-INF/views/member/memberEnroll.jsp")
			.forward(request, response);
	}

	/**
	 * 회원정보등록 요청
	 * 	- DML
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 인코딩
			request.setCharacterEncoding("utf-8");
			
			// 2. 사용자입력값 처리
			String memberId = request.getParameter("memberId");
			// 단방향 암호화 처리
			String password = HelloMvcUtils.encrypt(request.getParameter("password"), memberId);
			String memberName = request.getParameter("memberName");
			String gender = request.getParameter("gender");
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			String address = request.getParameter("address");
			
			// birthday
			String _birthday = request.getParameter("birthday");
			Date birthday = null;
			if (_birthday != null && !"".equals(_birthday)) {
				birthday = Date.valueOf(_birthday); // 사용자입력값이 없을 때 처리되지 않으므로 조건문 처리			
			}
			
			// hobby
			String[] _hobby = request.getParameterValues("hobby");
			String hobby = null;
			if (_hobby != null) {
				hobby = String.join(",", _hobby);
			}
			
			Member member = new Member(memberId, password, memberName, MemberRole.U, gender, birthday, email, phone,
					address, hobby, null);
			
			System.out.println("member@MemberEnrollServlet = " + member);
			
			// 3. 업무 로직 (db insert)
			// insert into member values()
			int result = memberService.insertMember(member);
			String msg = result > 0 ? "성공적으로 회원가입했습니다." : "회원가입에 실패했습니다.";
			
			// 4. 리다이렉트 (DML처리인 경우 url을 변경해서 새로고침오류를 방지함)
			// 사용자 경고장 처리
			// 성공적으로 회원가입했습니다. | 회원가입에 실패했습니다.
			HttpSession session = request.getSession();
			
			if (!(result == 1)) {
				session.setAttribute("msg", msg);
			} else if (result == 1) {
				session.setAttribute("msg", msg);
			}
			response.sendRedirect(request.getContextPath() + "/");
		} catch (Exception e) {
			// 1. 로깅 및 관리팀 알림
			e.printStackTrace();
			
			// 2. 예외던지기 - tomcat에 통보(tomcat이 정하는 에러페이지로 이동됨)
			// 실제 운영시에는 직접 만든 에러페이지를 제공하는 것이 좋음
			// 예외던지기를 통해 에러페이지로 넘어가게 되면, 경고창 확인 불가!
			throw e;
		}
	}

}
