package com.kh.student.model.service;

import java.util.Map;

import com.kh.student.model.dto.Student;

public interface StudentService {
	
	Student selectOne(int no);

	int insertStudent(Student student);

	int insertStudent(Map<String, Object> studentMap);

	int getTotalCount();

	Map<String, Object> selectOneMap(int no);

	int updateStudent(Student student);

	int deleteStudent(int no);
	
}
