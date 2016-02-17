package com.web.rest.model;

import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.web.rest.util.ServicesConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Product extends Entity {

	private String productId;
	private String productName;
	private String productCode;
	private String productDescription;
	private Integer productPrice;
	private Long createdAt;
	private Long updatedAt;
	private Boolean active;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Integer getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(Integer productPrice) {
		this.productPrice = productPrice;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public Document toBsonDocument() {
		Document document = new Document();

		document.append(ServicesConstants.ID, this.getProductId());
		document.append(ServicesConstants.PRODUCT_NAME, this.getProductName());
		document.append(ServicesConstants.PRODUCT_CODE, this.getProductCode());
		document.append(ServicesConstants.PRODUCT_DESCRIPTION, this.getProductDescription());
		document.append(ServicesConstants.PRODUCT_PRICE, this.getProductCode());
		document.append(ServicesConstants.CREATED_AT, this.getCreatedAt());
		document.append(ServicesConstants.UPDATED_AT, this.getUpdatedAt());
		document.append(ServicesConstants.IS_ACTIVE, this.isActive());

		return document;
	}

	public static Product toObject(Document document) {
		Product product = new Product();

		String id = document.getString(ServicesConstants.ID);

		if (id != null) {
			product.setProductId(id);
		}

		String name = document.getString(ServicesConstants.PRODUCT_NAME);
		if (name != null) {
			product.setProductName(name);
		}

		String code = document.getString(ServicesConstants.PRODUCT_CODE);
		if (code != null) {
			product.setProductCode(code);
		}

		String desc = document.getString(ServicesConstants.PRODUCT_DESCRIPTION);
		if (desc != null) {
			product.setProductDescription(desc);
		}

		Integer price = document.getInteger(ServicesConstants.PRODUCT_PRICE);
		if (price != null) {
			product.setProductPrice(price);
		}

		Long createTime = document.getLong(ServicesConstants.CREATED_AT);
		if (createTime != null) {
			product.setCreatedAt(createTime);
		}

		Long updateTime = document.getLong(ServicesConstants.UPDATED_AT);
		if (updateTime != null) {
			product.setUpdatedAt(updateTime);
		}

		Boolean activeValue = document.getBoolean(ServicesConstants.IS_ACTIVE);
		if (activeValue != null) {
			product.setActive(activeValue);
		}

		return product;
	}
}
