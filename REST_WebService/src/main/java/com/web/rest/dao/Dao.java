package com.web.rest.dao;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.rest.dao.exception.DaoException;
import com.web.rest.model.Entity;
import com.web.rest.model.SearchParam;

public interface Dao<T extends Entity, R extends Entity> {

	static final Logger LOGGER = LoggerFactory.getLogger(Dao.class);

	public R add(T t, Consumer<T> consumer) throws DaoException;

	public R update(String Id, T t, Consumer<T> consumer) throws DaoException;

	public R get(String Id, Function<Document, R> function) throws DaoException;

	public List<R> getAll(Function<Document, R> function) throws DaoException;

	public List<R> search(SearchParam param, Function<Document, R> function) throws DaoException;

	public boolean delete(String Id) throws DaoException;

	default public <T1, R1> R1 execute(T1 t, DaoFunction<T1, R1> function) throws DaoException {
		R1 r = null;
		try {
			r = function.execute(t);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DaoException(e.getMessage(), e.getCause());
		}
		return r;
	}
}
