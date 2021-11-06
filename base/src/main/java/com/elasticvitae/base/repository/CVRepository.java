package com.elasticvitae.base.repository;

import java.util.UUID;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.elasticvitae.base.model.CV;

public interface CVRepository extends ElasticsearchRepository<CV, UUID>{
	
	//public void updateCV(String email, CV cv);
	
}
