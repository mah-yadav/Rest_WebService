package com.web.rest.api.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.web.rest.api.Controller;
import com.web.rest.api.ControllerFunction;
import com.web.rest.api.Response;
import com.web.rest.api.exception.RESTException;
import com.web.rest.service.Service;

public abstract class RESTController<T> implements Controller<T> {

	private final Service<T> service;

	public RESTController(Service<T> aService) {
		service = aService;
	}

	@Override
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<String>> add(@RequestBody T t) throws RESTException {
		ControllerFunction<T, T> controllerFunction = (data) -> {
			System.out.println("Data posted successfully :" + data.toString());
			service.add(data);
			return null;
		};

		this.<T, T>execute(t, controllerFunction);

		String message = "Item created successfully.";
		return getStringResponse(message, message, HttpStatus.CREATED);
	}

	@Override
	@RequestMapping(value = "/{Id:.+}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<String>> update(@PathVariable Integer Id, T t) throws RESTException {
		ControllerFunction<T, T> controllerFunction = (data) -> {
			System.out.println("Data putted successfully :" + data.toString());
			service.update(Id, data);
			return null;
		};

		this.<T, T>execute(t, controllerFunction);

		String message = "Item with id '" + Id + "' updated successfully.";
		return getStringResponse(message, message, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/{Id:.+}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<T>>> get(@PathVariable Integer Id) throws RESTException {
		ControllerFunction<Integer, List<T>> controllerFunction = (dataId) -> {
			return service.get(dataId);
		};

		List<T> dataList = this.<Integer, List<T>>execute(Id, controllerFunction);

		String message = "Item with id '" + Id + "' fetched successfully.";
		return getResponseEntity(dataList, message, HttpStatus.OK);
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Response<List<T>>> getAll() throws RESTException {
		ControllerFunction<String, List<T>> controllerFunction = (data) -> {
			return service.getAll();
		};

		List<T> dataList = this.<String, List<T>>execute(null, controllerFunction);

		String message = "Items fetched successfully.";
		return getResponseEntity(dataList, message, HttpStatus.OK);
	}
}
