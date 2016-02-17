package com.web.rest.web.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.rest.model.Filter;
import com.web.rest.model.Product;
import com.web.rest.model.SearchParam;
import com.web.rest.service.ServiceInterface;
import com.web.rest.util.ServicesConstants;
import com.web.rest.web.ControllerFunction;
import com.web.rest.web.RSTController;
import com.web.rest.web.Response;
import com.web.rest.web.WebResponse;
import com.web.rest.web.exception.RSTException;

@RestController
@RequestMapping(value = "/api/v1/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class AGControllerImpl implements RSTController<Product, Product> {

	private static final Logger logger = LoggerFactory.getLogger(AGControllerImpl.class);

	@Autowired
	@Qualifier("service")
	private ServiceInterface<Product, Product> service;

	@Override
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<List<Product>>> add(@RequestBody Product t, @RequestHeader(value = ServicesConstants.API_VERSION, required = true) String apiVersion)
			throws RSTException {
		List<Product> dataList = new ArrayList<>();
		String message = null;
		HttpHeaders headers = new HttpHeaders();

		if (ServicesConstants.API_VERSION_1_1.equals(apiVersion)) {
			ControllerFunction<Product, Product> controllerFunction = (data) -> {
				return service.add(data);
			};

			Product finalData = this.<Product, Product> execute(t, controllerFunction);
			dataList.add(finalData);

			message = "Product created successfully : " + t.getProductId();
			logger.info(message);

			try {
				String uriString = "/api/v1/inventory/" + finalData.getProductId();
				URI uri = new URI(uriString);
				headers.setLocation(uri);
			} catch (URISyntaxException e) {
				logger.error(e.getMessage(), e);
				throw new RSTException(e.getMessage(), e);
			}
		}

		WebResponse<List<Product>, Response<List<Product>>> webResponse = WebResponse::prepareProductResponse;

		return new ResponseEntity<Response<List<Product>>>(webResponse.response(dataList, HttpStatus.CREATED.toString(), message, null), headers, HttpStatus.CREATED);
	}

	@Override
	@RequestMapping(value = "/{Id:.+}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response<List<Product>>> update(@PathVariable String Id, @RequestBody Product t,
			@RequestHeader(value = ServicesConstants.API_VERSION, required = true) String apiVersion,
			@RequestHeader(value = ServicesConstants.IF_UNMODIFIED_SINCE, required = false) long unmodifiedSince) throws RSTException {

		List<Product> dataList = new ArrayList<>();
		String message = null;

		if (ServicesConstants.API_VERSION_1_1.equals(apiVersion)) {
			ControllerFunction<Product, Product> controllerFunction = (data) -> {
				return service.update(Id, data, unmodifiedSince);
			};

			Product finalData = this.<Product, Product> execute(t, controllerFunction);
			dataList.add(finalData);

			message = "Product updated successfully : " + t.getProductId();
			logger.info(message);
		}

		WebResponse<List<Product>, Response<List<Product>>> webResponse = WebResponse::prepareProductResponse;

		return new ResponseEntity<Response<List<Product>>>(webResponse.response(dataList, HttpStatus.OK.toString(), message, null), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/{Id:.+}", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Product>>> get(@PathVariable String Id, @RequestHeader(value = ServicesConstants.API_VERSION, required = true) String apiVersion,
			@RequestHeader(value = ServicesConstants.IF_MODIFIED_SINCE, required = false) long modifiedSince) throws RSTException {
		List<Product> dataList = new ArrayList<>();
		String message = null;
		boolean notModified = false;

		if (ServicesConstants.API_VERSION_1_1.equals(apiVersion)) {
			ControllerFunction<String, Product> controllerFunction = (id) -> {
				return service.get(id);
			};

			Product finalData = this.<String, Product> execute(Id, controllerFunction);

			if (modifiedSince > 0 && (modifiedSince < finalData.getUpdatedAt())) {
				dataList.add(finalData);
			} else {
				notModified = true;
			}

			message = "Product fetched successfully : " + Id;
			logger.info(message);
		}

		WebResponse<List<Product>, Response<List<Product>>> webResponse = WebResponse::prepareProductResponse;

		if (notModified) {
			message = "Product hasn't modified : " + Id;
			logger.info(message);

			return new ResponseEntity<Response<List<Product>>>(webResponse.response(dataList, HttpStatus.NOT_MODIFIED.toString(), message, null), HttpStatus.NOT_MODIFIED);
		} else {
			return new ResponseEntity<Response<List<Product>>>(webResponse.response(dataList, HttpStatus.OK.toString(), message, null), HttpStatus.OK);
		}
	}

	@Override
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Response<List<Product>>> getAll(@RequestHeader(value = ServicesConstants.API_VERSION, required = true) String apiVersion) throws RSTException {

		List<Product> dataList = new ArrayList<>();
		String message = null;

		if (ServicesConstants.API_VERSION_1_1.equals(apiVersion)) {
			ControllerFunction<Product, List<Product>> controllerFunction = (data) -> {
				return service.getAll();
			};

			dataList = this.<Product, List<Product>> execute(null, controllerFunction);

			message = "All products fetched successfully.";
			logger.info(message);
		}

		WebResponse<List<Product>, Response<List<Product>>> webResponse = WebResponse::prepareProductResponse;

		return new ResponseEntity<Response<List<Product>>>(webResponse.response(dataList, HttpStatus.OK.toString(), message, null), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<Response<List<Product>>> search(@RequestHeader(value = ServicesConstants.API_VERSION, required = true) String apiVersion,
			@RequestParam(value = ServicesConstants.QUERY, required = false) String query, @RequestParam(value = ServicesConstants.FIELDS, required = false) String fields)
			throws RSTException {

		List<Product> dataList = new ArrayList<>();
		String message = null;

		if (ServicesConstants.API_VERSION_1_1.equals(apiVersion)) {
			ControllerFunction<Product, List<Product>> controllerFunction = (data) -> {
				SearchParam param = new SearchParam();

				if (!StringUtils.isEmpty(query)) {
					String[] queryArray = query.split(",");
					if (queryArray != null && queryArray.length > 0) {

						for (int i = 0; i < queryArray.length; i++) {
							String queryString = queryArray[i];

							String[] split = queryString.split(":");

							if (split != null && split.length == 2) {
								Filter<Object> filter = new Filter<Object>();
								param.add(filter);

								filter.setName(split[0]);
								filter.setValue(split[1]);
							}
						}
					}
				}

				if (!StringUtils.isEmpty(fields)) {
					String[] fieldArray = fields.split(",");
					if (fieldArray != null && fieldArray.length > 0) {
						for (int j = 0; j < fieldArray.length; j++) {
							param.addFields(fieldArray[j]);
						}
					}
				}

				return service.search(param);
			};

			dataList = this.<Product, List<Product>> execute(null, controllerFunction);

			message = "All products fetched successfully.";
			logger.info(message);
		}

		WebResponse<List<Product>, Response<List<Product>>> webResponse = WebResponse::prepareProductResponse;

		return new ResponseEntity<Response<List<Product>>>(webResponse.response(dataList, HttpStatus.OK.toString(), message, null), HttpStatus.OK);

	}

	@Override
	@RequestMapping(value = "/{Id:.+}", method = RequestMethod.DELETE)
	public ResponseEntity<Response<String>> delete(@PathVariable String Id, @RequestHeader(value = ServicesConstants.API_VERSION, required = true) String apiVersion)
			throws RSTException {
		boolean status = false;
		String message = null;

		if (ServicesConstants.API_VERSION_1_1.equals(apiVersion)) {
			ControllerFunction<String, Boolean> controllerFunction = (id) -> {
				return service.delete(id);
			};

			status = this.<String, Boolean> execute(Id, controllerFunction);

			message = "Product with Id '" + Id + "' deleted succssfully :" + status;
			logger.info(message);
		}

		WebResponse<String, Response<String>> promotionResponse = WebResponse::prepareStringResponse;

		return new ResponseEntity<Response<String>>(promotionResponse.response(String.valueOf(status), HttpStatus.OK.toString(), message, null), HttpStatus.OK);
	}
}
