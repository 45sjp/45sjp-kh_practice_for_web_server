package com.kh.student.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kh.common.AbstractController;
import com.kh.student.model.service.StudentService;

public class StudentDeleteController extends AbstractController {
	private StudentService studentService;
	
	public StudentDeleteController(StudentService studentService) {
		super();
		this.studentService = studentService;
	}
	
	@Override
	public String doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1. 사용자 입력값 처리
		int no = Integer.parseInt(request.getParameter("no"));
		
		// 2. 업무 로직
		int result = studentService.deleteStudent(no);
		
		// 3. 응답작성 : 비동기 json 응답
		String msg = "학생정보 삭제 성공!";
		response.setContentType("application/json;charset=utf-8");
		new Gson().toJson(msg, response.getWriter());
		
		return null;
	}
			
}
