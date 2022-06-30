package com.kh.student.model.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.kh.student.model.dto.Student;

public interface StudentDao {

	Student selectOne(SqlSession sqlSession, int no);

	int insertStudent(SqlSession sqlSession, Student student);

	int insertStudent(SqlSession sqlSession, Map<String, Object> studentMap);

	int getTotalCount(SqlSession sqlSession);

	Map<String, Object> selectOneMap(SqlSession sqlSession, int no);

	int updateStudent(SqlSession sqlSession, Student student);

	int deleteStudent(SqlSession sqlSession, int no);

}
