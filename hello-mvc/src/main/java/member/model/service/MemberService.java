package member.model.service;

import static common.JdbcTemplate.*;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import member.model.dao.MemberDao;
import member.model.dto.Member;

/**
 * Service
 * 	1. connection 생성
 * 	2. dao 요청(connection)
 * 	3. dml인 경우 transaction 처리
 *  4. connection 반환
 *  5. controller로 값 반환 처리
 */
public class MemberService {
	
	// 상수는 서비스단에 선언하는 것이 관례
	public static final int Num_PER_PAGE = 10; // 한 페이지에 표시할 컨텐츠 수
	private MemberDao memberDao = new MemberDao();
	
	/**
	 * 아이디 찾기
	 * @param memberId
	 * @return
	 */
	public Member findByMemberId(String memberId) {
		Connection conn = getConnection();
		Member member = memberDao.findByMemberId(conn, memberId);
		close(conn);
		return member;
	}
	
	/**
	 * 회원가입
	 * @param member
	 * @return
	 */
	public int insertMember(Member member) {
		int result = 0;
		Connection conn = getConnection();
		
		// try ~ catch 안에서 트랜잭션 처리
		try {
			result = memberDao.insertMember(conn, member);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e; // controller(servlet)에게 통보하기 위함
		} finally {
			close(conn);
		}
		
		return result;
	}
	
	/**
	 * 회원정보 수정
	 * @param member
	 * @return
	 */
	public int updateMember(Member member) {
		int result = 0;
		
		// 1. Connection객체 생성
		Connection conn = getConnection();
		
		try {
			// 2. dao 요청
			result = memberDao.updateMember(conn, member);
			
			// 3.1. 트랜잭션 처리
			commit(conn);
		} catch (Exception e) {
			// 3.2. 트랜잭션 처리
			rollback(conn);
			throw e; // controller 통보용
		} finally {
			// 4. Connection객체 반환
			close(conn);
		}

		return result;
	}

	/**
	 * 회원탈퇴
	 * @param member
	 * @return
	 */
	public int deleteMember(String loginMember) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = memberDao.deleteMember(conn, loginMember);
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
	 * 비밀번호 수정
	 * @param member
	 * @return
	 */
	public int updatePassword(Member member) {
		int result = 0;
		Connection conn = getConnection();
		try {
			result = memberDao.updatePassword(conn, member);
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
	 * 관리자 회원목록 조회
	 * @param param 
	 * @return
	 */
	public List<Member> findAll(Map<String, Object> param) {
		Connection conn = getConnection();
		List<Member> list = memberDao.findAll(conn, param);
		close(conn);
		return list;
	}
	
	/**
	 * 회원권한 변경
	 * @param member
	 * @return
	 */
	public int updateMemberRole(Member member) {
		int result = 0;
		Connection conn = getConnection();
		
		try {
			result = memberDao.updateMemberRole(conn, member);
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
	 * 회원정보 검색
	 * @param param
	 * @return
	 */
	public List<Member> findBy(Map<String, String> param) {
		Connection conn = getConnection();
		List<Member> list = memberDao.findBy(conn, param);
		close(conn);
		return list;
	}
	
	/**
	 * 관리자 회원목록 페이징
	 * @return
	 */
	public int getTotalContents() {
		Connection conn = getConnection();
		int totalContents = memberDao.getTotalContents(conn);
		close(conn);
		return totalContents;
	}

}
