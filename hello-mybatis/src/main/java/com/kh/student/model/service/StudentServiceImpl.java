package com.kh.student.model.service;

import static com.kh.common.SqlSessionTemplate.getSqlSession;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.kh.student.model.dao.StudentDao;
import com.kh.student.model.dto.Student;

public class StudentServiceImpl implements StudentService {
	
	private StudentDao studentDao;

	public StudentServiceImpl(StudentDao studentDao) {
		super();
		this.studentDao = studentDao;
	}

	@Override
	public Student selectOne(int no) {
		Student student = null;
		try(SqlSession sqlSession = getSqlSession()) {
			student = studentDao.selectOne(sqlSession, no);
		}
		return student;
	}
	
	@Override
	public Map<String, Object> selectOneMap(int no) {
		Map<String, Object> student = null;
		try(SqlSession sqlSession = getSqlSession()) {
			student = studentDao.selectOneMap(sqlSession, no);
		}
		return student;
	}
	

	@Override
	public int insertStudent(Student student) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = studentDao.insertStudent(sqlSession, student);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int insertStudent(Map<String, Object> studentMap) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = studentDao.insertStudent(sqlSession, studentMap);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int getTotalCount() {
		int totalCount = 0;
		try(SqlSession sqlSession = getSqlSession()) {
			totalCount = studentDao.getTotalCount(sqlSession);
		}
		return totalCount;
	}

	@Override
	public int updateStudent(Student student) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = studentDao.updateStudent(sqlSession, student);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} finally {
			sqlSession.close();
		}
		return result;
	}

	@Override
	public int deleteStudent(int no) {
		int result = 0;
		SqlSession sqlSession = getSqlSession();
		try {
			result = studentDao.deleteStudent(sqlSession, no);
			sqlSession.commit();
		} catch (Exception e) {
			sqlSession.rollback();
			throw e;
		} finally {
			sqlSession.close();
		}
		return result;
	}

}
