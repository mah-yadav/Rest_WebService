package com.web.rest.service.validator.impl;

import org.springframework.stereotype.Component;

import com.web.rest.model.Student;
import com.web.rest.service.exception.ServiceException;
import com.web.rest.service.validator.Validator;

@Component("StudentValidator")
public class StudentValidator implements Validator<Student> {

	@Override
	public void validateAdd(Student t) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateUpdate(Integer Id, Student t) throws ServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateGet(Integer Id) {
		// TODO Auto-generated method stub

	}
}
