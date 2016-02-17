package com.web.rest.util;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.autoginie.common.PropertyCache;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component("mongoDBUtil")
@DependsOn("propertyContextInitializer")
public class MongoDBUtil {
	private MongoClient client;
	private MongoDatabase database;
	private Map<String, MongoCollection<Document>> collectionMap = new HashMap<String, MongoCollection<Document>>();

	@Autowired
	@Qualifier("propertyCache")
	private PropertyCache propertyCache;

	@PostConstruct
	private void init() {
		String mongoHost = propertyCache.getPropertyValue(ServicesConstants.PROPERTIES, ServicesConstants.MONGO_HOST);
		String mongoPort = propertyCache.getPropertyValue(ServicesConstants.PROPERTIES, ServicesConstants.MONGO_PORT);
		String servicesDB = propertyCache.getPropertyValue(ServicesConstants.PROPERTIES, ServicesConstants.MONGO_PARTS_DB);
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
