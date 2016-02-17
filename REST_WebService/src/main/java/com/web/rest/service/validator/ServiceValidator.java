package com.web.rest.service.validator;

import java.util.function.Predicate;

import com.web.rest.service.exception.ServiceException;

public interface ServiceValidator<T> {

	public void validateAdd(T t, Predicate<T> predicate) throws ServiceException;

	public void validateUpdate(String areaId, T t, Predicate<T> predicate) throws ServiceException;

	public void validateGet(String Id, Predicate<String> predicate);
	
	public void validateDelete(String Id, Predicate<String> predicate);

}
