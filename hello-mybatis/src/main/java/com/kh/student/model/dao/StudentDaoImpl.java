package com.kh.student.model.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.kh.student.model.dto.Student;

public class StudentDaoImpl implements StudentDao {

	@Override
	public Student selectOne(SqlSession sqlSession, int no) {
		// statement:String - (mapper의 namespace값).(쿼리태그의 id값)
		return sqlSession.selectOne("student.selectOne", no);
	}
	
	@Override
	public Map<String, Object> selectOneMap(SqlSession sqlSession, int no) {
		return sqlSession.selectOne("student.selectOneMap", no);
	}

	@Override
	public int insertStudent(SqlSession sqlSession, Student student) {
		return sqlSession.insert("student.insertStudent", student); // student의 자리는 딱 하나만 올 수 있음!
	}

	@Override
	public int insertStudent(SqlSession sqlSession, Map<String, Object> studentMap) {
		return sqlSession.insert("student.insertStudentMap", studentMap);
	}

	@Override
	public int getTotalCount(SqlSession sqlSession) {
		return sqlSession.selectOne("student.getTotalCount");
	}

	@Override
	public int updateStudent(SqlSession sqlSession, Student student) {
		return sqlSession.insert("student.updateStudent", student);
	}

	@Override
	public int deleteStudent(SqlSession sqlSession, int no) {
		return sqlSession.insert("student.deleteStudent", no);
	}

}
