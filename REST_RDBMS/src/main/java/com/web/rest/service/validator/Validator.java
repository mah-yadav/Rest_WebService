package com.web.rest.service.validator;

import com.web.rest.service.exception.ServiceException;

public interface Validator<T> {

	public void validateAdd(T t) throws ServiceException;

	public void validateUpdate(Integer Id, T t) throws ServiceException;

	public void validateGet(Integer Id);
}
