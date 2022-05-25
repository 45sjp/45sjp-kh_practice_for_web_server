package board.model.dao;

import static common.JdbcTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import board.model.dto.Attachment;
import board.model.dto.Board;
import board.model.dto.BoardExt;
import board.model.exception.BoardException;

public class BoardDao {
	
	private Properties prop = new Properties();
	
	public BoardDao() {
		String fileName = BoardDao.class.getResource("/sql/board-query.properties").getPath();
		System.out.println("fileName@BoardDao = " + fileName);
		try {
			prop.load(new FileReader(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 게시판 목록 조회 - 페이징 처리
	 * @param conn
	 * @param param
	 * @return
	 */
	public List<BoardExt> findAllBoardList(Connection conn, Map<String, Object> param) {
		String sql = prop.getProperty("findAllBoardList");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<BoardExt> boardList = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			while(rset.next()) {
				BoardExt board = handleBoardResultSet(rset);
				board.setAttachCount(rset.getInt("attach_cnt")); // 첨부파일 개수
				boardList.add(board);
			}
		} catch (Exception e) {
			throw new BoardException("게시판 목록 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return boardList;
	}
	
	/**
	 * 게시판 ResultSet 관리
	 * @param rset
	 * @return
	 * @throws SQLException
	 */
	private BoardExt handleBoardResultSet(ResultSet rset) throws SQLException {
		BoardExt board;
		board = new BoardExt();
		board.setNo(rset.getInt("no"));
		board.setTitle(rset.getString("title"));
		board.setMemberId(rset.getString("member_id"));
		board.setContent(rset.getString("content"));
		board.setReadCount(rset.getInt("read_count"));
		board.setRegDate(rset.getDate("reg_date"));
		
		return board;
	}
	
	/**
	 * 전체 게시글 수 조회
	 * @param conn
	 * @return
	 */
	public int getTotalBoardContents(Connection conn) {
		String sql = prop.getProperty("getTotalBoardContents");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalBoardContents = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				totalBoardContents = rset.getInt(1);
			}
		} catch (Exception e) {
			throw new BoardException("전체 게시글 수 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return totalBoardContents;
	}
	
	/**
	 * 게시글 등록
	 * @param conn
	 * @param board
	 * @return
	 */
	public int insertBoard(Connection conn, Board board) {
		String sql = prop.getProperty("insertBoard");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getTitle());
			pstmt.setString(2, board.getMemberId());
			pstmt.setString(3, board.getContent());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new BoardException("게시글 등록 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * 게시글 번호 조회
	 * @param conn
	 * @return
	 */
	public int findCurrentBoardNo(Connection conn) {
		String sql = prop.getProperty("findCurrentBoardNo");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int no = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				no = rset.getInt(1);				
			}
		} catch (Exception e) {
			throw new BoardException("게시글 번호 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return no;
	}

	/**
	 * 첨부파일 등록
	 * @param conn
	 * @param attach
	 * @return 
	 */
	public int insertAttachment(Connection conn, Attachment attach) {
		String sql = prop.getProperty("insertAttachment");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, attach.getBoardNo());
			pstmt.setString(2, attach.getOriginalFilename());
			pstmt.setString(3, attach.getRenamedFilename());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new BoardException("첨부파일 등록 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * 게시글 한 건 조회 - board테이블의 no컬럼
	 * @param conn
	 * @param no
	 * @return
	 */
	public BoardExt findbyNo(Connection conn, int no) {
		String sql = prop.getProperty("findbyNo");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		BoardExt board = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				board = handleBoardResultSet(rset);
			}
		} catch (Exception e) {
			throw new BoardException("게시글 한 건 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return board;
	}

	/**
	 * 첨부파일 조회 - attachment테이블의 board_no 컬럼
	 * @param conn
	 * @param no
	 * @return
	 */
	public List<Attachment> findAttachmentByBoardNo(Connection conn, int no) {
		String sql = prop.getProperty("findAttachmentByBoardNo");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Attachment> attachments = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Attachment attach = handleAttachmentResultSet(rset);
				attachments.add(attach);
			}
		} catch (Exception e) {
			throw new BoardException("게시글 번호에 의한 첨부파일 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return attachments;
	}

	/**
	 * 첨부파일 ResultSet 관리
	 * @param rset
	 * @return
	 * @throws SQLException
	 */
	private Attachment handleAttachmentResultSet(ResultSet rset) throws SQLException {
		Attachment attach = new Attachment();
		attach.setNo(rset.getInt("no"));
		attach.setBoardNo(rset.getInt("board_no"));
		attach.setOriginalFilename(rset.getString("original_filename"));
		attach.setRenamedFilename(rset.getString("renamed_filename"));
		attach.setRegDate(rset.getDate("reg_date"));
		return attach;
	}

	/**
	 * 조회수 증가
	 * @param conn
	 * @param no
	 * @return
	 */
	public int updateReadCount(Connection conn, int no) {
		String sql = prop.getProperty("updateReadCount");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new BoardException("조회수 증가처리 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * 첨부파일 한 건 조회
	 * @param conn
	 * @param no
	 * @return
	 */
	public Attachment findAttachmentByNo(Connection conn, int no) {
		String sql = prop.getProperty("findAttachmentByNo");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Attachment attach = null;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			rset = pstmt.executeQuery();
			
			// 한 건일 경우에는 if 사용 가능!
			if(rset.next()) {
				attach = handleAttachmentResultSet(rset);
			}
		} catch (Exception e) {
			throw new BoardException("첨부파일 한 건 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}		
		return attach;
	}

	/**
	 * 게시글 삭제
	 * @param conn
	 * @param no
	 * @return
	 */
	public int deleteBoard(Connection conn, int no) {
		String sql = prop.getProperty("deleteBoard");
		PreparedStatement pstmt = null;
		int result = 0;
	
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, no);
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new BoardException("게시물 삭제 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
}
