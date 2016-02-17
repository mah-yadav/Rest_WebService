package com.web.rest.model;

import java.util.ArrayList;
import java.util.List;

public class SearchParam {
	private List<Filter<Object>> filters = new ArrayList<>();
	private List<String> fields = new ArrayList<>();

	public List<Filter<Object>> getFilters() {
		return filters;
	}

	public void add(Filter<Object> filter) {
		filters.add(filter);
	}

	public List<String> getFields() {
		return fields;
	}

	public void addFields(String field) {
		fields.add(field);
	}

}
