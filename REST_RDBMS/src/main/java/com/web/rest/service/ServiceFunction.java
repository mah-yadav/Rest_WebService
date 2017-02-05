package com.web.rest.service;

import com.web.rest.service.exception.ServiceException;

@FunctionalInterface
public interface ServiceFunction<T, R> {
	public R execute(T t) throws ServiceException;
}
