# Rest_WebService

## Introduction

Rest_WebService is a generic web service based on REST principles.This web service has CRUD operations and basic search facility for adding, updating, deleting and searching products in an e-commerce application. This web service uses JSON as representation medium. This web service can be readily used in other applications and domains by modifying model and service layer as per requirements.

## Technologies Used

1. **Java 8** - Specifically lambda expression, generics and collection streams.
2. **Spring MVC** - Web service is developed using spring MVC.
3. **Maven** - Maven is used for compiling and bundling of web service.
4. **Mongodb** - Mongodb has been used as the datastore.

## Application Architecture

1. REST principles. 
2. MVC Architecture.
3. Microservices Architecture.

## Deployment Steps

1. Get all the code from github.

2. Open file **"$HOME"/Rest_WebService/REST_WebService/src/main/java/com/web/rest/util/ServicesConstants.java**. Modify value of private variable **"PROP_LOCATION"** as desired,value of the variable is the location where properties file will be present.

3. At location, which has been specified as value of **"PROP_LOCATION"** in above setup, create **"product.properties"**. A sample properties file is present at location **"$HOME"/Rest_WebService/REST_WebService/src/main/resources/config**.

4. In product.properties provide value for following keys:
  1. mongo.host
  2. mongo.port
  3. mongo.product.db
  4. mongo.product.collection

5. Open file **"$HOME"/Rest_WebService/REST_WebService/src/main/resources/logback.xml**. Modify value of property **"DEV_HOME"** as desired, value of this property is the location where log files will be created.

6. Build web service using maven with goal as **"clean install -U"**.

7. Output of above step is RestService.war in **"$HOME"/Rest_WebService/REST_WebService/target** folder. Deploy this war file on a tomcat server and use it.

##Operations

1. **Add a product**
	1. *Request-URL* - http://ipaddress:port/RestService/api/v1/products
	2. *Request-method* - POST
	3. *Request-header* - apiVersion=1.1 (*required*)
	4. *Request-body* - JSON payload
	5. *Response-status* - 201
	6. *Response-body* - Created product in JSON format.
	7. *Response-header* - Location=http://ipaddress:port/RestService/api/v1/product/{productId} (*Created resource's access URI*)

2. **Update a product**
	1. *Request-URL* - http://ipaddress:port/RestService/api/v1/products/{productId}
	2. *Request-method* - PUT
	3. *Request-header* - apiVersion=1.1 (*required*)
	4. *Request-body* - JSON payload
	5. *Response-status* - 200
	6. *Response-body* - Updated product in JSON format.

3. **Get a product**
	1. *Request-URL* - http://ipaddress:port/RestService/api/v1/products/{productId}
	2. *Request-method* - GET
	3. *Request-header* - apiVersion=1.1 (*required*)
	4. *Response-status* - 200
	5. *Response-body* - Fetched product in JSON format.

4. **Get all products**
	1. *Request-URL* - http://ipaddress:port/RestService/api/v1/products
	2. *Request-method* - GET
	3. *Request-header* - apiVersion=1.1 (*required*)
	4. *Response-status* - 200
	5. *Response-body* - Fetched products in JSON format.

4. **Search for products**
	1. *Request-URL* - http://ipaddress:port/RestService/api/v1/products/search
	2. *Request-method* - GET
	3. *Request-header* - apiVersion=1.1 (*required*)
	4. *Request-Parameters* - Query=field:value,field1:value1,field2:value2 ....  ,  fields=field1,field2,field3 .....
	4. *Response-status* - 200
	5. *Response-body* - Fetched products in JSON format.

5. **Delete a product**
	1. *Request-URL* - http://ipaddress:port/RestService/api/v1/products/{productId}
	2. *Request-method* - DELETE
	3. *Request-header* - apiVersion=1.1 (*required*)
	4. *Response-status* - 200
