package com.web.rest.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.web.rest.dao.Dao;
import com.web.rest.model.Product;
import com.web.rest.model.SearchParam;
import com.web.rest.service.ServiceFunction;
import com.web.rest.service.ServiceInterface;
import com.web.rest.service.exception.ServiceException;
import com.web.rest.service.validator.ServiceValidator;
import com.web.rest.util.InventoryLock;

@Service("service")
public class ServiceImpl implements ServiceInterface<Product, Product> {

	@Autowired
	@Qualifier("dao")
	private Dao<Product, Product> dao;

	@Autowired
	@Qualifier("serviceValidator")
	private ServiceValidator<Product> serviceValidator;

	@Autowired
	@Qualifier("inventoryLock")
	private InventoryLock lock;

	@Override
	public Product add(Product t) throws ServiceException {
		Predicate<Product> predicate = ServiceImpl::validateRole;
		serviceValidator.validateAdd(t, predicate);

		ServiceFunction<Product, Product> serviceFunction = (data) -> {

			Consumer<Product> consumer = (finalData) -> {
				long currentTimeMillis = System.currentTimeMillis();
				finalData.setCreatedAt(currentTimeMillis);
				finalData.setUpdatedAt(currentTimeMillis);
				finalData.setActive(true);
			};

			data.setProductId(UUID.randomUUID() + "-" + System.currentTimeMillis());

			return dao.add(data, consumer);
		};

		return this.<Product, Product> execute(t, serviceFunction);
	}

	@Override
	public Product update(String Id, Product t, long unmodifiedSince) throws ServiceException{
		Predicate<Product> predicate = ServiceImpl::validateRole;
		serviceValidator.validateUpdate(Id, t, predicate);

		ServiceFunction<Product, Product> serviceFunction = (data) -> {
			if (unmodifiedSince > 0) {
				try {
					lock.getLock(Id);
					Function<Document, Product> function = (document) -> {
						Product product = null;
						if (document != null) {
							product = Product.toObject(document);
						}
						return product;
					};

					Product finalProduct = dao.get(Id, function);

					if (unmodifiedSince > finalProduct.getUpdatedAt()) {
						Consumer<Product> consumer = (finalData) -> {
							long currentTimeMillis = System.currentTimeMillis();
							finalData.setUpdatedAt(currentTimeMillis);
						};
						finalProduct = dao.update(Id, data, consumer);
					} else {
						// throw new ValidationException("New version of resource exist in system. Update after fetching the latest version.");
					}

					return finalProduct;
				} finally {
					lock.releaseLock(Id);
				}
			} else {
				Consumer<Product> consumer = (finalData) -> {
					long currentTimeMillis = System.currentTimeMillis();
					finalData.setUpdatedAt(currentTimeMillis);
				};

				return dao.update(Id, data, consumer);
			}
		};

		return this.<Product, Product> execute(t, serviceFunction);
	}

	@Override
	public Product get(String Id) throws ServiceException {
		Predicate<String> predicate = (id) -> {
			if (StringUtils.isEmpty(id)) {
				LOGGER.info("Product Id can't be null/empty.");
				throw new IllegalArgumentException("Product Id can't be null/empty.");
			}

			return true;
		};
		serviceValidator.validateGet(Id, predicate);

		ServiceFunction<String, Product> serviceFunction = (id) -> {

			Function<Document, Product> function = (document) -> {
				Product product = null;

				if (document != null) {
					product = Product.toObject(document);
				}

				return product;
			};

			return dao.get(id, function);
		};

		return this.<String, Product> execute(Id, serviceFunction);
	}

	@Override
	public List<Product> getAll() throws ServiceException {
		ServiceFunction<String, List<Product>> serviceFunction = (data) -> {

			List<Product> dataList = new ArrayList<>();

			Function<Document, Product> function = (document) -> {
				Product product = null;

				if (document != null) {
					product = Product.toObject(document);
				}

				return product;
			};

			dataList = dao.getAll(function);
			return dataList;
		};

		return this.<String, List<Product>> execute(null, serviceFunction);
	}

	private static boolean validateRole(Product part) {
		if (StringUtils.isEmpty(part.getProductName())) {
			LOGGER.info("Product name can't be null/empty.");
			throw new IllegalArgumentException("Product name can't be null/empty.");

		} else if (StringUtils.isEmpty(part.getProductCode())) {
			LOGGER.info("Product code can't be null/empty.");
			throw new IllegalArgumentException("Product code can't be null/empty.");

		} else if (part.getProductPrice() <= 0) {
			LOGGER.info("Product price can't be less than equal to zero.");
			throw new IllegalArgumentException("Product price can't be less than equal to zero.");

		}

		return true;
	}

	@Override
	public boolean delete(String Id) throws ServiceException {

		Predicate<String> predicate = (id) -> {
			if (StringUtils.isEmpty(id)) {
				LOGGER.info("Product Id can't be null/empty.");
				throw new IllegalArgumentException("Product Id can't be null/empty.");
			}

			return true;
		};
		serviceValidator.validateDelete(Id, predicate);

		ServiceFunction<String, Boolean> serviceFunction = (id) -> {
			return dao.delete(id);
		};

		return this.<String, Boolean> execute(Id, serviceFunction);
	}

	@Override
	public List<Product> search(SearchParam param) throws ServiceException {
		List<Product> dataList = new ArrayList<>();

		ServiceFunction<SearchParam, List<Product>> serviceFunction = (searchParam) -> {

			Function<Document, Product> function = (document) -> {
				Product data = null;

				if (document != null) {
					data = Product.toObject(document);
				}

				return data;
			};
			return dao.search(searchParam, function);
		};

		dataList = this.<SearchParam, List<Product>> execute(param, serviceFunction);

		return dataList;
	}
}
