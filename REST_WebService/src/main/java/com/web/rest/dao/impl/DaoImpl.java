package com.web.rest.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.UpdateResult;
import com.web.rest.dao.Dao;
import com.web.rest.dao.DaoFunction;
import com.web.rest.dao.exception.DaoException;
import com.web.rest.model.Entity;
import com.web.rest.model.Filter;
import com.web.rest.model.SearchParam;
import com.web.rest.util.MongoDBUtil;
import com.web.rest.util.ServicesConstants;
import com.web.rest.web.ApplicationConfig;

@Repository("dao")
public class DaoImpl<T extends Entity, R extends Entity> implements Dao<T, R> {

	@Autowired
	@Qualifier("mongoDBUtil")
	private MongoDBUtil MONGODB_UTIL;

	private MongoCollection<Document> mongoCollection;

	@PostConstruct
	private void init() {
		String propertyValue = ApplicationConfig.getInstance().getValue(ServicesConstants.MONGO_PRODUCT_COLLECTION);
		mongoCollection = MONGODB_UTIL.getMongoCollection(propertyValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public R add(T t, Consumer<T> consumer) throws DaoException {
		DaoFunction<T, R> daoFunction = (data) -> {

			consumer.accept(data);

			Document bsonDocument = data.toBsonDocument();
			mongoCollection.insertOne(bsonDocument);
			return (R) data;
		};
		return this.<T, R> execute(t, daoFunction);
	}

	@SuppressWarnings("unchecked")
	@Override
	public R update(String Id, T t, Consumer<T> consumer) throws DaoException {
		DaoFunction<T, R> daoFunction = (data) -> {

			consumer.accept(data);

			Document bsonDocument = data.toBsonDocument();
			UpdateResult updateResult = mongoCollection.updateOne(new BasicDBObject(ServicesConstants.ID, Id), new Document("$set", bsonDocument));

			long modifiedCount = updateResult.getModifiedCount();
			if (modifiedCount == 0) {
				throw new DaoException("Update operation doesn't completed successfully for product :" + Id);
			}

			return (R) data;
		};

		return this.<T, R> execute(t, daoFunction);
	}

	@Override
	public R get(String Id, Function<Document, R> function) throws DaoException {
		DaoFunction<String, R> daoFunction = (id) -> {

			BasicDBObject searchObj = new BasicDBObject();
			searchObj.put(ServicesConstants.ID, id);
			searchObj.put(ServicesConstants.IS_ACTIVE, true);

			FindIterable<Document> findIterable = mongoCollection.find(searchObj);
			Document document = findIterable.first();

			return function.apply(document);
		};

		return this.<String, R> execute(Id, daoFunction);
	}

	@Override
	public List<R> getAll(Function<Document, R> function) throws DaoException {
		DaoFunction<String, List<R>> daoFunction = (data) -> {
			List<R> entities = new ArrayList<>();

			BasicDBObject searchObj = new BasicDBObject();
			searchObj.put(ServicesConstants.IS_ACTIVE, true);
			FindIterable<Document> findIterable = mongoCollection.find(searchObj);
			findIterable.batchSize(1000);
			MongoCursor<Document> iterator = findIterable.iterator();

			while (iterator.hasNext()) {
				Document document = iterator.next();
				R r = function.apply(document);
				entities.add(r);
			}

			return entities;

		};

		return this.<String, List<R>> execute(null, daoFunction);
	}

	@Override
	public List<R> search(SearchParam param, Function<Document, R> function) throws DaoException {
		DaoFunction<SearchParam, List<R>> daoFunction = (searchParam) -> {
			List<R> entities = new ArrayList<>();

			List<Filter<Object>> filters = searchParam.getFilters();
			BasicDBObject searchObj = new BasicDBObject();
			searchObj.put(ServicesConstants.IS_ACTIVE, true);

			if (!filters.isEmpty()) {
				for (Filter<Object> filter : filters) {
					if (filter.getOperator() != null) {
						searchObj.put(filter.getName(), new BasicDBObject(filter.getOperator(), filter.getValue()));
					} else {
						searchObj.put(filter.getName(), filter.getValue());
					}
				}
			}

			List<String> fields = searchParam.getFields();

			FindIterable<Document> findIterable = null;

			if (!fields.isEmpty()) {
				findIterable = mongoCollection.find(searchObj).projection(Projections.include(fields));
			} else {
				findIterable = mongoCollection.find(searchObj);
			}

			findIterable.batchSize(1000);
			MongoCursor<Document> iterator = findIterable.iterator();

			while (iterator.hasNext()) {
				Document document = iterator.next();
				R r = function.apply(document);
				entities.add(r);
			}
			return entities;
		};
		return this.<SearchParam, List<R>> execute(param, daoFunction);
	}

	@Override
	public boolean delete(String Id) throws DaoException {
		DaoFunction<String, Boolean> daoFunction = (id) -> {

			boolean status = false;
			BasicDBObject searchObj = new BasicDBObject();
			searchObj.put(ServicesConstants.ID, id);
			searchObj.put(ServicesConstants.IS_ACTIVE, true);

			FindIterable<Document> findIterable = mongoCollection.find(searchObj);
			Document document = findIterable.first();
			if (document != null) {
				document.append(ServicesConstants.IS_ACTIVE, false);
				document.append(ServicesConstants.UPDATED_AT, System.currentTimeMillis());
				UpdateResult updateResult = mongoCollection.updateOne(new BasicDBObject(ServicesConstants.ID, id), new Document("$set", document));

				long modifiedCount = updateResult.getModifiedCount();
				if (modifiedCount == 0) {
					throw new DaoException("Delete operation doesn't completed successfully for product :" + Id);
				}
				status = true;
			}
			return status;
		};

		return this.<String, Boolean> execute(Id, daoFunction);
	}
}
