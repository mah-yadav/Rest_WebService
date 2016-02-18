package com.web.rest.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.web.rest.model.Entity;
import com.web.rest.service.exception.ServiceException;
import com.web.rest.web.exception.RSTException;

public interface RSTController<T extends Entity, R extends Entity> {

	static final Logger LOGGER = LoggerFactory.getLogger(RSTController.class);

	public ResponseEntity<Response<List<R>>> add(T t, String version) throws RSTException;

	public ResponseEntity<Response<List<R>>> update(String Id, T t, String version) throws RSTException;

	public ResponseEntity<Response<List<R>>> get(String Id, String version, long modifiedSince) throws RSTException;

	public ResponseEntity<Response<List<R>>> getAll(String version) throws RSTException;

	public ResponseEntity<Response<String>> delete(String Id, String version) throws RSTException;

	public ResponseEntity<Response<List<R>>> search(String apiVersion, String query, String fields) throws RSTException;

	default public <T1, R1> R1 execute(T1 t, ControllerFunction<T1, R1> function) throws RSTException {
		R1 r = null;
		try {
			r = function.execute(t);
		} catch (ServiceException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RSTException(e.getMessage(), e.getCause());
		}
		return r;
	}

	@ExceptionHandler(RSTException.class)
	default public ResponseEntity<Response<String>> handleServerException(HttpServletRequest request, Exception e) {
		WebResponse<String, Response<String>> notificationResponse = WebResponse::prepareStringResponse;

		return new ResponseEntity<Response<String>>(notificationResponse.response(null, HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.getMessage(), request.getRequestURL()
				.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	default public ResponseEntity<Response<String>> handleValidationException(HttpServletRequest request, Exception e) {
		WebResponse<String, Response<String>> notificationResponse = WebResponse::prepareStringResponse;

		return new ResponseEntity<Response<String>>(notificationResponse.response(null, HttpStatus.BAD_REQUEST.toString(), e.getMessage(), request.getRequestURL().toString()),
				HttpStatus.BAD_REQUEST);
	}
}
