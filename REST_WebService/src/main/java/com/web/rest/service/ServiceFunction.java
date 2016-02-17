package com.web.rest.service;

import com.web.rest.dao.exception.DaoException;

@FunctionalInterface
public interface ServiceFunction<T, R> {
	public R execute(T t) throws DaoException;

}
