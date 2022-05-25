package board.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.model.dto.Attachment;
import board.model.service.BoardService;

/**
 * Servlet implementation class BoardFileDownloadServlet
 */
@WebServlet("/board/fileDownload")
public class BoardFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		
		// 2. 업무 로직
		Attachment attach = boardService.findAttachmentByNo(no);
		System.out.println(attach);
		
		// 3. 응답 처리
		// 헤더작성 - 메세지 작성보다 먼저 처리되어야 함
		response.setContentType("application/octet-stream"); // 응답데이터 타입(2진)
		
		// Content-Disposition : 첨부파일인 경우, 브라우저 다운로드(Save as) 처리 명시
		// 한글일 경우 파일명 깨지므로 처리해주어야 함!
		// Content-Disposition: attachment;filename=?? ??.png
		String resFilename = new String(attach.getOriginalFilename().getBytes("utf-8"), "iso-8859-1"); // tomcat 기본인코딩
		response.setHeader("Content-Disposition", "attachment;filename=" + resFilename);
		System.out.println("resFilename = " + resFilename); // resFilename = ì ëª© ìì.png
		
		// 파일을 읽어서(input) 응답메세지에 작성(output)
		String saveDirectory = getServletContext().getRealPath("/upload/board");
		File file = new File(saveDirectory, attach.getRenamedFilename());
		
		// 기본스트림 - 대상과 연결
		// 보조스트림 - 기본스트림 연결. 보조스트림을 제어
		try (
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
		){
			byte[] buffer = new byte[8192]; // 8kb까지의 내용을 모았다가 전달
			int len = 0; // 읽어낸 byte 수
			while((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len); // buffer의 0번지부터 len(읽은 개수)만큼 출력
			}
		}
	}

}
