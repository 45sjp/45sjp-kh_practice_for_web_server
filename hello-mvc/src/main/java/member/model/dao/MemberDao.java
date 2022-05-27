package member.model.dao;

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

import common.HelloMvcUtils;
import common.JdbcTemplate;
import member.model.dto.Member;
import member.model.dto.MemberRole;
import member.model.exception.MemberException;

public class MemberDao {
	
	private Properties prop = new Properties();
	
	public MemberDao() {
		// buildpath의 sql/member-query.properties파일의 내용을 불러오기
		String fileName = MemberDao.class.getResource("/sql/member-query.properties").getPath();
		System.out.println("fileName@MemberDao = " + fileName);
		try {
			prop.load(new FileReader(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 아이디 찾기
	 * @param conn
	 * @param memberId
	 * @return
	 */
	public Member findByMemberId(Connection conn, String memberId) {
		String sql = prop.getProperty("findByMemberId");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		
		try {
			// 1. pstmt 객체 & 미완성쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			// 2. 실행 및 rset 처리
			rset = pstmt.executeQuery();
			while(rset.next()) {
				member = handleMemberResultSet(rset);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 3. 자원반납(rset, pstmt)
			close(rset);
			close(pstmt);
		}
		return member;
	}

	private Member handleMemberResultSet(ResultSet rset) throws SQLException {
		Member member;
		member = new Member();
		member.setMemberId(rset.getString("member_id"));
		member.setPassword(rset.getString("password"));
		member.setMemberName(rset.getString("member_name"));
		// "U" -> MemberRole.U, "A" -> MemberRole.A
		member.setMemberRole(MemberRole.valueOf(rset.getString("member_role")));
		member.setGender(rset.getString("gender"));
		member.setBirthday(rset.getDate("birthday"));
		member.setEmail(rset.getString("email"));
		member.setPhone(rset.getString("phone"));
		member.setAddress(rset.getString("address"));
		member.setHobby(rset.getString("hobby"));
		member.setEnrollDate(rset.getDate("enroll_date"));
		return member;
	}
	
	/**
	 * 회원가입
	 * @param conn
	 * @param member
	 * @return
	 */
	public int insertMember(Connection conn, Member member) {
		String sql = prop.getProperty("insertMember");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getMemberName());
			pstmt.setString(4, member.getMemberRole().toString()); // "U" / "A"
			pstmt.setString(5, member.getGender());
			pstmt.setDate(6, member.getBirthday());
			pstmt.setString(7, member.getEmail());
			pstmt.setString(8, member.getPhone());
			pstmt.setString(9, member.getAddress());
			pstmt.setString(10, member.getHobby());
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MemberException("회원가입 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	/**
	 * 회원정보 수정
	 * 
	 * set memberName = ?, gender = ?, birthday = ?, email = ?, phone = ?, address = ?, hobby = ?
	 * where id = ?
	 * 
	 * @param conn
	 * @param member
	 * @return
	 */
	public int updateMember(Connection conn, Member member) {
		String sql = prop.getProperty("updateMember");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			// 1. pstmt객체 생성 & 미완성쿼리 값대입
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberName());
			pstmt.setString(2, member.getGender());
			pstmt.setDate(3, member.getBirthday());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhone());
			pstmt.setString(6, member.getAddress());
			pstmt.setString(7, member.getHobby());
			pstmt.setString(8, member.getMemberId());
			
			// 2. 실행
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new MemberException("회원정보수정 오류", e);
		} finally {
			// 3. 자원반납 - pstmt
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * 회원탈퇴
	 * @param conn
	 * @param loginMember
	 * @return
	 */
	public int deleteMember(Connection conn, String loginMember) {
		String sql = prop.getProperty("deleteMember");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, loginMember);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new MemberException("회원탈퇴 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	/**
	 * 비밀번호 수정
	 * @param conn
	 * @param member
	 * @return
	 */
	public int updatePassword(Connection conn, Member member) {
		String sql = prop.getProperty("updatePassword");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getMemberId());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new MemberException("비밀번호변경 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/**
	 * 관리자 회원목록 조회 - 페이징 처리
	 * select * from member order by enroll_date desc
	 * @param conn
	 * @param param 
	 * @return
	 */
	public List<Member> findAll(Connection conn, Map<String, Object> param) {
		String sql = prop.getProperty("findAll");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, (int) param.get("start"));
			pstmt.setInt(2, (int) param.get("end"));
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
		} catch (Exception e) {
			throw new MemberException("관리자 회원목록 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}
	
	/**
	 * 회원권한 변경
	 * @param conn
	 * @param member
	 * @return
	 */
	public int updateMemberRole(Connection conn, Member member) {
		String sql = prop.getProperty("updateMemberRole");
		PreparedStatement pstmt = null;
		int result = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMemberRole().toString());
			pstmt.setString(2, member.getMemberId());
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			throw new MemberException("관리자 회원권한 변경 오류", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
	/**
	 * 회원정보 검색
	 * @param conn
	 * @param param 
	 * @return
	 */
	public List<Member> findBy(Connection conn, Map<String, String> param) {
		String sql = prop.getProperty("findBy");
		sql = sql.replace("#", param.get("searchType"));
		System.out.println("sql = " + sql);
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		List<Member> list = new ArrayList<>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + param.get("searchKeyword") + "%");
			rset = pstmt.executeQuery();
			while(rset.next()) {
				Member member = handleMemberResultSet(rset);
				list.add(member);
			}
		} catch (Exception e) {
			throw new MemberException("관리자 회원정보 검색 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}
	
//	public static void main(String[] args) {
//		new MemberDao().updatePasswordAll();
//	}
	
	/**
	 * 신규 회원 일괄 추가
	 */
	public void updatePasswordAll() {
		// 1. 회원 아이디 조회 및 신규 비번 설정
        Connection conn = JdbcTemplate.getConnection();
        String sql = prop.getProperty("findAll");
        List<Member> list = new ArrayList<>();
        
        try(
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rset = pstmt.executeQuery();
        ) {
        	while(rset.next()) {
                String memberId = rset.getString("member_id");
                Member member = new Member();
                member.setMemberId(memberId);
                member.setPassword(HelloMvcUtils.encrypt("1234", memberId));
                list.add(member);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list); 
        
        // 2. 비밀번호 업데이트
        // update member set password = ? where member_id = ?
        sql = prop.getProperty("updatePassword");
        
        try(
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            for(Member member : list) {
                pstmt.setString(1, member.getPassword());
                pstmt.setString(2, member.getMemberId());
                pstmt.executeUpdate();
                System.out.println("변경완료 : " + member.getMemberId() + " - " + member.getPassword());
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
	
	/**
	 * 관리자 전체 회원 수 조회
	 * @param conn
	 * @return
	 */
	public int getTotalContents(Connection conn) {
		String sql = prop.getProperty("getTotalContents");
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		int totalContents = 0;
		
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				totalContents = rset.getInt(1); // 컬럼 인덱스
			}
		} catch (Exception e) {
			throw new MemberException("전체 회원 수 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return totalContents;
	}

}
