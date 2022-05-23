package board.model.service;

import static common.JdbcTemplate.close;
import static common.JdbcTemplate.getConnection;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import board.model.dao.BoardDao;
import board.model.dto.Attachment;
import board.model.dto.Board;

public class BoardService {

	public static final int Num_PER_PAGE = 10;
	private BoardDao boardDao = new BoardDao();

	/**
	 * 게시판 목록 조회 - 페이징 처리
	 * @param param
	 * @return
	 */
	public List<Board> findAllBoardList(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Board> boardList = boardDao.findAllBoardList(conn, param);
		close(conn);
		return boardList;
	}

	/**
	 * 전체 게시글 수 조회
	 * @return
	 */
	public int getTotalBoardContents() {
		Connection conn = getConnection();
		int totalBoardContents = boardDao.getTotalBoardContents(conn);
		close(conn);
		return totalBoardContents;
	}

// Board, Attachment join 처리!
//	/**
//	 * 첨부파일 포함 게시글 목록 조회
//	 * @param param
//	 * @return
//	 */
//	public List<Attachment> findAllAttachmentList(Map<String, Object> param) {
//		Connection conn = getConnection();
//		List<Attachment> AttachmentList = boardDao.findAllAttachmentList(conn, param);
//		close(conn);
//		return AttachmentList;
//	}

}
