package com.web.rest.web;

import com.web.rest.service.exception.ServiceException;

@FunctionalInterface
public interface ControllerFunction<T, R> {
	public R execute(T t) throws ServiceException;

}
