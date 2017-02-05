package com.web.rest.api;

import com.web.rest.api.exception.RESTException;
import com.web.rest.service.exception.ServiceException;

@FunctionalInterface
public interface ControllerFunction<T, R> {
	public R execute(T t) throws ServiceException,RESTException;
}
