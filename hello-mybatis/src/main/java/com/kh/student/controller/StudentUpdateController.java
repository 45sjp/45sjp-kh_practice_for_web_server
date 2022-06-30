package com.kh.student.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kh.common.AbstractController;
import com.kh.student.model.dto.Student;
import com.kh.student.model.service.StudentService;

public class StudentUpdateController extends AbstractController {
	private StudentService studentService;

	public StudentUpdateController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	@Override
	public String doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		String name = request.getParameter("name");
		String tel = request.getParameter("tel");
		Student student = new Student();
		student.setNo(no);
		student.setName(name);
		student.setTel(tel);
		System.out.println("student = " + student);
		
		// 2. 업무 로직
		int result = studentService.updateStudent(student);
		
		// 3. 응답작성 : 비동기 json 응답
		String msg = "학생정보 수정 성공!";
		response.setContentType("application/json;charset=utf-8");
		new Gson().toJson(msg, response.getWriter());
		
		return null;
	}
	
}
