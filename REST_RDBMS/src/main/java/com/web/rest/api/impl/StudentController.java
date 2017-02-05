package com.web.rest.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.rest.api.Response;
import com.web.rest.api.exception.RESTException;
import com.web.rest.model.Student;
import com.web.rest.service.Service;

@RestController
@RequestMapping(value = "/rest/students")
public class StudentController extends RESTController<Student> {

	@Autowired
	public StudentController(@Qualifier("StudentService") Service<Student> aService) {
		super(aService);
	}

	@Override
	public ResponseEntity<Response<List<Student>>> getResponseEntity(List<Student> data, String message, HttpStatus status) throws RESTException {
		Response<List<Student>> response = new Response<>();
		response.setData(data);
		response.setStatus(status.toString());
		response.setMessage(message);

		return new ResponseEntity<Response<List<Student>>>(response, status);
	}
}
