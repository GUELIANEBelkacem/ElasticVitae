package com.elasticvitae.base.search.util;

import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.CollectionUtils;

import com.elasticvitae.base.log.Logger;
import com.elasticvitae.base.search.SearchRequestDTO;

public final class SearchUtil {

	
	private SearchUtil() {}
	
	public static SearchRequest buildSearchRequest(final String indexName, final SearchRequestDTO dto) {
		try {
			final SearchSourceBuilder builder = new SearchSourceBuilder()
					.postFilter(getQueryBuilder(dto));
			
			SearchRequest request = new SearchRequest(indexName);
			
			request.source(builder);
			
			
			return request;
		}catch(final Exception e){
			Logger.error("Error while building dto request:\n"+e.getMessage());
	
			return null;
		}
	}
	
	
	
	public static QueryBuilder getQueryBuilder(final SearchRequestDTO dto) {
		if(dto == null) return null;
		
		final List<String> fields = dto.getFields();
		if(CollectionUtils.isEmpty(fields)) return null;
		
		if(fields.size()>1) {
			MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
					.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
					.operator(Operator.AND); //perhaps change to or later
		
			fields.forEach(queryBuilder::field);
			
			return queryBuilder;		
			
		}
		
		return fields.stream()
				.findFirst()
				.map(field -> QueryBuilders.matchQuery(field, dto.getSearchTerm())
						.operator(Operator.AND))
				.orElse(null);
	}
	
	
}
