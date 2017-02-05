package com.web.rest.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.web.rest.api.exception.RESTException;
import com.web.rest.service.exception.ServiceException;

public interface Controller<T> {

	public ResponseEntity<Response<String>> add(T t) throws RESTException;

	public ResponseEntity<Response<String>> update(Integer Id, T t) throws RESTException;

	public ResponseEntity<Response<List<T>>> get(Integer Id) throws RESTException;

	public ResponseEntity<Response<List<T>>> getAll() throws RESTException;

	default <T1, R1> R1 execute(T1 t, ControllerFunction<T1, R1> function) throws RESTException {
		R1 r = null;
		try {
			r = function.execute(t);
		} catch (ServiceException | RESTException e) {
			throw new RESTException(e.getMessage(), e.getCause());
		}
		return r;
	}

	default ResponseEntity<Response<String>> getStringResponse(String message, String data, HttpStatus status) throws RESTException {
		Response<String> response = new Response<>();
		response.setData(data);
		response.setStatus(status.toString());
		response.setMessage(message);

		return new ResponseEntity<Response<String>>(response, status);
	}

	public ResponseEntity<Response<List<T>>> getResponseEntity(List<T> data, String message, HttpStatus status) throws RESTException;

}
