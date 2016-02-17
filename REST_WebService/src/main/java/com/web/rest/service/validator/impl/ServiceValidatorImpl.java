package com.web.rest.service.validator.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.web.rest.dao.Dao;
import com.web.rest.dao.exception.DaoException;
import com.web.rest.model.Filter;
import com.web.rest.model.Product;
import com.web.rest.model.SearchParam;
import com.web.rest.service.exception.ServiceException;
import com.web.rest.service.validator.ServiceValidator;
import com.web.rest.util.ServicesConstants;

@Component("serviceValidator")
public class ServiceValidatorImpl implements ServiceValidator<Product> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceValidatorImpl.class);

	@Autowired
	@Qualifier("dao")
	private Dao<Product, Product> dao;

	@Override
	public void validateAdd(Product t, Predicate<Product> predicate) throws ServiceException {
		boolean status = predicate.test(t);

		if (status) {
			List<Product> parts = new ArrayList<>();

			SearchParam param = new SearchParam();
			Filter<Object> filter = new Filter<Object>();
			filter.setName(ServicesConstants.PRODUCT_CODE);
			filter.setValue(t.getProductCode());

			param.add(filter);

			Function<Document, Product> function = (document) -> {
				Product data = null;

				if (document != null) {
					data = Product.toObject(document);
				}

				return data;
			};

			try {
				parts = dao.search(param, function);
			} catch (DaoException e) {
				throw new ServiceException(e.getMessage(), e.getCause());
			}

			if (!parts.isEmpty()) {
				String message = "Product with code '" + t.getProductCode() + "' already exists in the system.";
				LOGGER.info(message);
				throw new IllegalArgumentException(message);
			}
		}
	}

	@Override
	public void validateUpdate(String Id, Product t, Predicate<Product> predicate) throws ServiceException {
		if (StringUtils.isEmpty(Id)) {
			LOGGER.info("Product Id can't be null/empty.");
			throw new IllegalArgumentException("Product Id can't be null/empty.");
		}

		boolean status = predicate.test(t);

		if (status) {
			List<Product> parts = new ArrayList<>();

			SearchParam param = new SearchParam();

			Filter<Object> filter = new Filter<Object>();
			filter.setName(ServicesConstants.PRODUCT_CODE);
			filter.setValue(t.getProductCode());

			Filter<Object> filter1 = new Filter<Object>();
			filter1.setName(ServicesConstants.ID);
			filter1.setValue(Id);

			param.add(filter);
			param.add(filter1);

			Function<Document, Product> function = (document) -> {
				Product data = null;

				if (document != null) {
					data = Product.toObject(document);
				}

				return data;
			};

			try {
				parts = dao.search(param, function);
			} catch (DaoException e) {
				throw new ServiceException(e.getMessage(), e.getCause());
			}

			if (parts.isEmpty()) {
				String message = "Product with code '" + t.getProductCode() + "' and Id '" + Id + "' doesn't exists in the system.";
				LOGGER.info(message);
				throw new IllegalArgumentException(message);
			}
		}
	}

	@Override
	public void validateGet(String Id, Predicate<String> predicate) {
		predicate.test(Id);

	}

	@Override
	public void validateDelete(String Id, Predicate<String> predicate) {
		predicate.test(Id);

	}
}
