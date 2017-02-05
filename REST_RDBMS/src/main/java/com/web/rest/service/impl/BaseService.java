package com.web.rest.service.impl;

import java.util.List;

import com.web.rest.service.Service;
import com.web.rest.service.ServiceFunction;
import com.web.rest.service.exception.ServiceException;
import com.web.rest.service.validator.Validator;

public abstract class BaseService<T> implements Service<T> {
	private final Validator<T> validator;

	public BaseService(Validator<T> aValidator) {
		validator = aValidator;
	}

	@Override
	public void add(T t) throws ServiceException {
		validator.validateAdd(t);
		ServiceFunction<T, T> serviceFunction = (data) -> {
			System.out.println("Data added successfully. " + data.toString());
			return null;
		};

		this.<T, T>execute(t, serviceFunction);
	}

	@Override
	public void update(Integer Id, T t) throws ServiceException {
		validator.validateUpdate(Id, t);
		ServiceFunction<T, T> serviceFunction = (data) -> {
			System.out.println("Data updated successfully. " + data.toString());
			return null;
		};

		this.<T, T>execute(t, serviceFunction);

	}

	@Override
	public List<T> get(Integer Id) throws ServiceException {
		validator.validateGet(Id);
		ServiceFunction<Integer, List<T>> serviceFunction = (dataId) -> {
			System.out.println("Data fetched successfully for Id : " + dataId);
			return null;
		};

		return this.<Integer, List<T>>execute(Id, serviceFunction);
	}

	@Override
	public List<T> getAll() throws ServiceException {
		ServiceFunction<String, List<T>> serviceFunction = (dataId) -> {
			System.out.println("Data fetched successfully.");
			return null;
		};

		return this.<String, List<T>>execute(null, serviceFunction);
	}
}
