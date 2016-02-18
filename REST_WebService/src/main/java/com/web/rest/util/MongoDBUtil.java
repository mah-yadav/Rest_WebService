package com.web.rest.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.web.rest.web.ApplicationConfig;

@Component("mongoDBUtil")
public class MongoDBUtil {
	private MongoClient client;
	private MongoDatabase database;
	private Map<String, MongoCollection<Document>> collectionMap = new HashMap<String, MongoCollection<Document>>();

	@PostConstruct
	private void init() {
		String mongoHost = ApplicationConfig.getInstance().getValue(ServicesConstants.MONGO_HOST);
		String mongoPort = ApplicationConfig.getInstance().getValue(ServicesConstants.MONGO_PORT);
		String servicesDB = ApplicationConfig.getInstance().getValue(ServicesConstants.MONGO_PRODUCT_DB);
		client = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
		database = client.getDatabase(servicesDB);
	}

	public synchronized MongoCollection<Document> getMongoCollection(String collectionName) {
		MongoCollection<Document> mongoCollection = collectionMap.get(collectionName);

		if (mongoCollection == null) {
			mongoCollection = database.getCollection(collectionName);
			collectionMap.put(collectionName, mongoCollection);
		}
		return mongoCollection;
	}

	public MongoDatabase getMongoDatabase() {
		return database;
	}
}
