package com.elasticvitae.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.elasticvitae.base.helper.Indecies;
import com.elasticvitae.base.model.CV;
import com.elasticvitae.base.repository.CVRepository;
import com.elasticvitae.base.search.SearchRequestDTO;
import com.elasticvitae.base.search.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CVService {
	private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(CV.class);
	private final CVRepository cvrepo;
	private final RestHighLevelClient client;
	
	
	@Autowired
	public CVService(CVRepository cvrepo, RestHighLevelClient client) {
		this.cvrepo = cvrepo;
		this.client = client;
	}
	
	public void save(final CV cv) {
		SearchRequestDTO dto = new SearchRequestDTO();
		dto.setFields(Arrays.asList(new String[]{"email"}));
		dto.setSearchTerm(cv.getEmail());
		List<CV> checkList = new ArrayList<CV>();
		search(dto).forEach(checkList::add);
		if(CollectionUtils.isEmpty(checkList)) cvrepo.save(cv);
	}
	
	public CV findById(final UUID id) {
		return cvrepo.findById(id).orElse(null);
	}
	
	public Iterable<CV> findAll(){
		return cvrepo.findAll();
	}
	
	public void deleteAll() {
		cvrepo.deleteAll();
	}
	
	public void deleteById(final UUID id) {
		cvrepo.deleteById(id);
	}
	
	public Iterable<CV> multiSearch(List<String> q){
		List<Iterable<CV>> result = new ArrayList<Iterable<CV>>();
		if(CollectionUtils.isEmpty(q) || q==null) return Collections.emptyList();
		for(String s: q) {
			result.add(searchAll(s));
		}
		if(CollectionUtils.isEmpty(result)) return Collections.emptyList();
		ArrayList<CV> response = new ArrayList<CV>(); 
		result.get(0).forEach(response::add);
		
		for(Iterable<CV> r: result) {
			ArrayList<CV> temp = new ArrayList<CV>(); 
			r.forEach(temp::add);
			response.retainAll(temp);
		} 
		
		return response;
		
		
	}
	public Iterable<CV> searchInCV(String q){
		SearchRequestDTO dto = new SearchRequestDTO();
		dto.setFields(Arrays.asList(new String[]{"cvdata"}));
		dto.setSearchTerm(q);
		
		return search(dto);
		
	}
	
	public Iterable<CV> searchAll(String q){
		SearchRequestDTO dto = new SearchRequestDTO();
		dto.setFields(Arrays.asList(new String[]{"cvdata", "name", "email", "phone"}));
		dto.setSearchTerm(q);
		Iterable<CV> response = search(dto);
		if(response == null) return Collections.emptyList();
		return response;
		
	}
	
	public Iterable<CV> search(final SearchRequestDTO dto){
		final SearchRequest request  = SearchUtil.buildSearchRequest(Indecies.CV_INDEX, dto);
		if(request == null) {
			LOG.error("failed to build search request");  
			return Collections.emptyList();
		}
		
		try {
			final SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			
			final SearchHit[] searchHits = response.getHits().getHits();
			final List<CV> cvs = new ArrayList<>(searchHits.length);
			for(SearchHit hit: searchHits) {
				System.out.println(formatHit(hit.getSourceAsString()));
				cvs.add(MAPPER.readValue(formatHit(hit.getSourceAsString()), CV.class));
			}
			
			return cvs;
		}catch(Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	private String formatHit(String hit) {
		String result = hit;
		int idx = -1;
		char[] l = result.toCharArray();
		for(int i=0; i<l.length; i++ ) {
			if(l[i] == ',') {
				idx = i;
				break;
			}
		}
		if(idx>0) {result = result.substring(idx+1); result = '{'+result;}
		return result;
			
	}
	
	public void update(String email, CV cv) throws Exception{
		SearchRequestDTO dto = new SearchRequestDTO();
		dto.setFields(Arrays.asList(new String[]{"email"}));
		dto.setSearchTerm(email);
		List<CV> checkList = new ArrayList<CV>();
		search(dto).forEach(checkList::add);
		if(CollectionUtils.isEmpty(checkList) || checkList.size()>1) throw new Exception("Couldn't Find The Requested Email");
		
		CV oldcv = checkList.get(0);
		UpdateRequest ur = new UpdateRequest();
		ur.index("cv")
			.id(oldcv.getId().toString())
			.doc(jsonStringMaker(cv, oldcv), XContentType.JSON);
		
		client.update(ur, RequestOptions.DEFAULT);
		
	}
	
	public String jsonStringMaker(CV cv, CV oldcv) {
		String jsonString = "{";
		if(cv.getName() != null && !cv.getName().isBlank()) jsonString+="\"name\":\""+cv.getName()+"\",";
		else jsonString+="\"name\":\""+oldcv.getName()+"\",";
		if(cv.getEmail() != null && !cv.getEmail().isBlank()) jsonString+="\"email\":\""+cv.getEmail()+"\",";
		else jsonString+="\"email\":\""+oldcv.getEmail()+"\",";
		if(cv.getPhone() != null && !cv.getPhone().isBlank()) jsonString+="\"phone\":\""+cv.getPhone()+"\",";
		else jsonString+="\"phone\":\""+oldcv.getPhone()+"\",";
		if(cv.getCvsource() != null && !cv.getCvsource().isBlank()) {
			jsonString+="\"cvsource\":\""+cv.getCvsource()+"\",";
			jsonString+="\"cvdata\":"+jsonStringMakerList(cv.getCvdata())+"}";
		}
		else jsonString+="\"cvsource\":\""+oldcv.getCvsource()+"\"}";
		
		
		return jsonString;
		
	}
	public String jsonStringMakerList(List<String> l) {
		String s = "[";
		for(String ss : l) {
			s+= "\""+ss+"\",";
		}
		if(!l.isEmpty()) s = s.substring(0, s.length()-1);
		s+="]";
		return s;
	}
}
