package board.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import board.model.dto.Attachment;
import board.model.service.BoardService;

/**
 * Servlet implementation class BoardDeleteServlet
 */
@WebServlet("/board/boardDelete")
public class BoardDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int no = Integer.parseInt(request.getParameter("no"));
			
			List<Attachment> attachments = boardService.findbyNo(no).getAttachments();
			String delDirectory = getServletContext().getRealPath("/upload/board");
			for(Attachment attach : attachments) {
				File delFilename = new File(delDirectory, attach.getRenamedFilename());
				if(delFilename.exists()) {
					delFilename.delete();
				}
			}
			
			int result = boardService.deleteBoard(no);
			
			HttpSession session = request.getSession();
			session.setAttribute("msg", "게시물 삭제가 완료되었습니다.");
			
			response.sendRedirect(request.getContextPath() + "/board/boardList");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
