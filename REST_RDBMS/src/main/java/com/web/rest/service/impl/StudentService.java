package com.web.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.web.rest.model.Student;
import com.web.rest.service.validator.Validator;

@Service("StudentService")
public class StudentService extends BaseService<Student> {
	
	@Autowired
	public StudentService(@Qualifier("StudentValidator")Validator<Student> aValidator) {
		super(aValidator);
	}
}
