package com.web.rest.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.rest.dao.exception.DaoException;
import com.web.rest.model.Entity;
import com.web.rest.model.SearchParam;
import com.web.rest.service.exception.ServiceException;

public interface ServiceInterface<T extends Entity, R extends Entity> {

	static final Logger LOGGER = LoggerFactory.getLogger(ServiceInterface.class);

	public R add(T t) throws ServiceException;

	public R update(String Id, T t, long unmodifiedSince) throws ServiceException;

	public R get(String Id) throws ServiceException;

	public List<R> getAll() throws ServiceException;

	public boolean delete(String Id) throws ServiceException;

	public List<R> search(SearchParam param) throws ServiceException;

	default public <T1, R1> R1 execute(T1 t, ServiceFunction<T1, R1> function) throws ServiceException {
		R1 r = null;
		try {
			r = function.execute(t);
		} catch (DaoException e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e.getCause());
		}
		return r;
	}
}
