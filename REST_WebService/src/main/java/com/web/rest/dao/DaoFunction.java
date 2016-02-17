package com.web.rest.dao;

import com.web.rest.dao.exception.DaoException;

@FunctionalInterface
public interface DaoFunction<T, R> {
	public R execute(T t) throws DaoException;

}
