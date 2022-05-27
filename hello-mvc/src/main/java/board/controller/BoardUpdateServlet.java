package board.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import board.model.dto.Attachment;
import board.model.dto.BoardExt;
import board.model.service.BoardService;
import common.HelloMvcFileRenamePolicy;

/**
 * Servlet implementation class BoardUpdateServlet
 */
@WebServlet("/board/boardUpdate")
public class BoardUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * 수정폼 요청
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		
		// 2. 업무 로직 - db에서 한 건 조회
		BoardExt board = boardService.findbyNo(no);
		
		// 3. view단 처리
		request.setAttribute("board", board);
		request.getRequestDispatcher("/WEB-INF/views/board/boardUpdate.jsp")
			.forward(request, response);
	}

	/**
	 * DB 수정 요청
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 2. MultipartRequest객체 생성		
			// b. 파일저장 경로
			String saveDirectory = getServletContext().getRealPath("/upload/board");
			// c. 최대 파일 업로드 크기 : 10MB
			int maxPostSize = 1024 * 1024 * 10;
			// d. 인코딩
			String encoding = "utf-8";
			// e. 파일명 재지정 정책 객체
			FileRenamePolicy policy = new HelloMvcFileRenamePolicy();
			MultipartRequest multiReq =
					new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
			
			// 3. 사용자 입력값 처리
			// ------------ BoardEnrollServlet 대비 달라진 부분 -------------
			int no = Integer.parseInt(multiReq.getParameter("no"));
			// ----------------------------------------------------------
			String title = multiReq.getParameter("title");
			String memberId = multiReq.getParameter("memberId");
			String content = multiReq.getParameter("content");
			
			BoardExt board = new BoardExt();
			// ------------------------ 달라진 부분 ------------------------
			board.setNo(no);
			// ----------------------------------------------------------
			board.setTitle(title);
			board.setMemberId(memberId);
			board.setContent(content);
			
			// 업로드 파일
			File upFile1 = multiReq.getFile("upFile1");
			File upFile2 = multiReq.getFile("upFile2");
			
			// 첨부파일 처리
			if(upFile1 != null || upFile2 != null) {
				List<Attachment> attachments = new ArrayList<>();
				if(upFile1 != null) {
					attachments.add(getAttachment(multiReq, no, "upFile1"));
				}
				if(upFile2 != null) {
					attachments.add(getAttachment(multiReq, no, "upFile2"));
				}
				// ------------------------ 달라진 부분 ------------------------
				board.setAttachments(attachments);
				// ----------------------------------------------------------
			}

			// 4. 업무 로직
			// db board(update), attachment(insert) 레코드 등록
			int result = boardService.updateBoard(board);
			String msg = result > 0 ? "게시글 수정에 성공했습니다." : "게시글 수정에 실패했습니다.";
						
			// 첨부 파일 삭제 처리
			String[] delFiles = multiReq.getParameterValues("delFile"); // 삭제하려는 첨부파일 pk
			if(delFiles != null) {
				for(String temp : delFiles) {
					int attachNo = Integer.parseInt(temp); // attachment pk
					Attachment attach = boardService.findAttachmentByNo(attachNo);
								
					// a. 저장된 파일에 대한 처리 - 파일 삭제
					File delFile = new File(saveDirectory, attach.getRenamedFilename());
					if(delFile.exists()) delFile.delete(); // 기존 파일 제거
								
					// b. db record에 대한 처리 - db record 삭제
					result = boardService.deleteAttachment(attachNo);
					System.out.println("> " + attachNo +"번 첨부파일 삭제!");
				}
			}
			
			// 5. 리다이렉트
			HttpSession session = request.getSession();
			session.setAttribute("msg", msg);
			response.sendRedirect(request.getContextPath() + "/board/boardView?no=" + no);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private Attachment getAttachment(MultipartRequest multiReq, int boardNo, String name) {
		Attachment attach = new Attachment();
		// ------------------------ 달라진 부분 ------------------------
		attach.setBoardNo(boardNo); // boarNo(fk)에 붙여넣기. 잊지 말 것!
		// ----------------------------------------------------------
		String originalFilename = multiReq.getOriginalFileName(name); // 업로드한 파일명
		String renamedFilename = multiReq.getFilesystemName(name); // 저장된 파일명
		attach.setOriginalFilename(originalFilename);
		attach.setRenamedFilename(renamedFilename);
		return attach;
	}

}
