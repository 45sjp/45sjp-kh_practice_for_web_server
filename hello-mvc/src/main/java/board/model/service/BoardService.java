package board.model.service;

import static common.JdbcTemplate.close;
import static common.JdbcTemplate.commit;
import static common.JdbcTemplate.getConnection;
import static common.JdbcTemplate.rollback;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import board.model.dao.BoardDao;
import board.model.dto.Attachment;
import board.model.dto.Board;
import board.model.dto.BoardComment;
import board.model.dto.BoardExt;

public class BoardService {

	public static final int Num_PER_PAGE = 10;
	private BoardDao boardDao = new BoardDao();

	/**
	 * 게시판 목록 조회 - 페이징 처리
	 * @param param
	 * @return
	 */
	public List<BoardExt> findAllBoardList(Map<String, Object> param) {
		Connection conn = getConnection();
		List<BoardExt> boardList = boardDao.findAllBoardList(conn, param);
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
	
	/**
	 * Transaction
	 * 	- all or none
	 * 	- 트랜잭션으로 처리되어야 할 것은 하나의 메소드로 작성되어야 함
	 * 
	 * 게시글 등록
	 * @param board
	 * @return
	 */
	public int insertBoard(Board board) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			// 1. board에 등록
			result = boardDao.insertBoard(conn, board); // pk no값 결정 : seq_board_no.nextval
			
			// 2. board pk 가져오기
			int no = boardDao.findCurrentBoardNo(conn); // seq_board_no.currval
			board.setNo(no); // 리턴은 result이지만 board객체의 주소를 알고 있기 때문에 가져올 수 있음
			System.out.println("방금 등록된 board.no = " + no);
			
			// 3. attachment에 등록
			List<Attachment> attachments = ((BoardExt) board).getAttachments();
			if(attachments != null && !attachments.isEmpty()) {
				for(Attachment attach : attachments) {
					attach.setBoardNo(no);
					result = boardDao.insertAttachment(conn, attach);
				}
			}
			
			commit(conn); // 예외가 발생하지 않으면 전체 commit
		} catch (Exception e) {
			rollback(conn); // 예외가 발생하면 전체 rollback
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}
	
	/**
	 * 게시글 상세 조회
	 * @param no
	 * @return
	 */
	public BoardExt findbyNo(int no) {
		Connection conn = getConnection();
		// join해서 처리할 수도 있음
		BoardExt board = boardDao.findbyNo(conn, no); // board테이블에서 조회
		List<Attachment> attachments = boardDao.findAttachmentByBoardNo(conn, no); // attachment테이블에서 조회. 0개 이상
		List<BoardComment> comments = boardDao.findBoardCommentByBoardNo(conn, no);
		board.setAttachments(attachments);
		board.setBoardComments(comments);
		close(conn);
		return board;
	}

	/**
	 * 조회수 증가
	 * @param no
	 * @return
	 */
	public int updateReadCount(int no) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = boardDao.updateReadCount(conn, no);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

	/**
	 * 첨부파일 한 건 조회
	 * @param no
	 * @return
	 */
	public Attachment findAttachmentByNo(int no) {
		Connection conn = getConnection();
		Attachment attach = boardDao.findAttachmentByNo(conn, no);
		close(conn);
		return attach;
	}

	/**
	 * 게시글 삭제
	 * @param no
	 * @return
	 */
	public int deleteBoard(int no) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = boardDao.deleteBoard(conn, no);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

	/**
	 * 게시글 수정
	 * @param board
	 * @return
	 */
	public int updateBoard(Board board) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			// 1. board 수정
			result = boardDao.updateBoard(conn, board); 
			
			// 2. attachment에 등록
			List<Attachment> attachments = ((BoardExt) board).getAttachments();
			if(attachments != null && !attachments.isEmpty()) {
				for(Attachment attach : attachments) {
					result = boardDao.insertAttachment(conn, attach);
				}
			}
			
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

	/**
	 * 첨부파일 삭제
	 * @param attachNo
	 * @return
	 */
	public int deleteAttachment(int no) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = boardDao.deleteAttachment(conn, no);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

	/**
	 * 댓글 등록
	 * @param bc
	 * @return
	 */
	public int insertBoardComment(BoardComment bc) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = boardDao.insertBoardComment(conn, bc);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

	/**
	 * 댓글 삭제
	 * @param no
	 * @return
	 */
	public int deleteBoardComment(int no) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = boardDao.deleteBoardComment(conn, no);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		
		return result;
	}

}
