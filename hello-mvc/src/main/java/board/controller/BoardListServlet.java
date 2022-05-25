package board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.dto.BoardExt;
import board.model.service.BoardService;
import common.HelloMvcUtils;

/**
 * Servlet implementation class BoardListServlet
 */
@WebServlet("/board/boardList")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 1. 사용자 입력값 처리
			int numPerPage = boardService.Num_PER_PAGE;
			int cPage = 1;
			
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch (NumberFormatException e) {
				// 예외발생시 현재 페이지는 1로 처리
			}
			
			Map<String, Object> param = new HashMap<>();
			int start = (cPage - 1) * numPerPage + 1;
			int end = cPage * numPerPage;
			param.put("start", start);
			param.put("end", end);
					
			// 1. 업무 로직
			// 2.a. content 영역
			List<BoardExt> boardList = boardService.findAllBoardList(param);
			System.out.println("boardList = " + boardList);

			// Board, Attachment join 처리!
			// List<Attachment> attachmentList = boardService.findAllAttachmentList(param);
			// System.out.println("attachmentList = " + attachmentList);
			// request.setAttribute("attachmentList", attachmentList);
			
			// 2.b. pagebar 영역
			int totalBoardContents = boardService.getTotalBoardContents();
			String url = request.getRequestURI();
			String pagebar = HelloMvcUtils.getPagebar(cPage, numPerPage, totalBoardContents, url);
			System.out.println("pagebar = " + pagebar);
			
			// 3. view단 처리
			request.setAttribute("boardList", boardList);
			request.setAttribute("pagebar", pagebar);
			
			request.getRequestDispatcher("/WEB-INF/views/board/boardList.jsp")
				.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace(); // 로깅하기 위함
			throw e;
		}
	}

}
