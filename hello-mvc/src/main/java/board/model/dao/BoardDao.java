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
	public List<Board> findAllBoardList(Connection conn, Map<String, Object> param) {
		String sql = prop.getProperty("findAllBoardList");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Board> boardList = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Board board = handleBoardResultSet(rset);
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
	private Board handleBoardResultSet(ResultSet rset) throws SQLException {
		Board board;
		board = new Board();
		board.setNo(rset.getInt("no"));
		board.setTitle(rset.getString("title"));
		board.setMemberId(rset.getString("member_id"));
		board.setContent(rset.getString("content"));
		board.setReadCount(rset.getInt("read_count"));
		board.setRegDate(rset.getDate("reg_date"));
		
		if(rset.getInt("attachment_no") != 0) {
			Attachment attachment = new Attachment();
			attachment.setNo(rset.getInt("no"));
			attachment.setOriginalFilename(rset.getString("original_filename"));
			attachment.setRenamedFilename(rset.getString("renamed_filename"));
			board.setAttachment(attachment);
		}
		
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

}
