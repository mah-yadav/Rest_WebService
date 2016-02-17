package com.web.rest.web;

import java.util.List;

import com.web.rest.model.Product;

@FunctionalInterface
public interface WebResponse<T, R> {

	public R response(T t, String status, String message, String url);

	static public Response<String> prepareStringResponse(String data, String status, String message, String url) {
		Response<String> response = new Response<String>();

		response.setData(data);
		response.setStatus(status);
		response.setMessage(message);
		response.setUrl(url);

		return response;
	}

	static public Response<List<Product>> prepareProductResponse(List<Product> parts, String status, String message, String url) {
		Response<List<Product>> response = new Response<List<Product>>();

		response.setData(parts);
		response.setStatus(status);
		response.setMessage(message);
		response.setUrl(url);

		return response;
	}

}
