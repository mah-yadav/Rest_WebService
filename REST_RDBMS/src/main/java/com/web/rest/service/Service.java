package com.web.rest.service;

import java.util.List;
import com.web.rest.service.exception.ServiceException;

public interface Service<T> {

	public void add(T t) throws ServiceException;

	public void update(Integer Id, T t) throws ServiceException;

	public List<T> get(Integer Id) throws ServiceException;

	public List<T> getAll() throws ServiceException;

	default <T1, R1> R1 execute(T1 t, ServiceFunction<T1, R1> function) throws ServiceException {
		R1 r = null;
		try {
			r = function.execute(t);
		} catch (ServiceException e) {
			throw new ServiceException(e.getMessage(), e.getCause());
		}
		return r;
	}
}
