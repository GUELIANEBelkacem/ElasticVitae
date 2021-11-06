package com.elasticvitae.base.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elasticvitae.base.log.Logger;
import com.elasticvitae.base.model.CV;
import com.elasticvitae.base.search.SearchRequestDTO;
import com.elasticvitae.base.service.CVService;


/**
 * 
 * the class that contains the main rest hooks
 *
 */
@RestController
@RequestMapping("/api/cv")
public class CVController {

	private final CVService cvservice;
	
	
	@Autowired
	public CVController(CVService cvservice) {
		this.cvservice = cvservice;
	}
	
	@PostMapping("/new")
	public void save(@RequestBody final CV cv) {
		Logger.log("\nAdded:\n\n"+CV.toJson(cv));
		cvservice.save(cv);
	}
	
	@GetMapping("/search/id={id}")
	public CV findById(@PathVariable final UUID id) {
		Logger.log("\nQuery:\nid = \n\n"+id.toString());
		return cvservice.findById(id);
	}
	
	@GetMapping("/search/all")
	public Iterable<CV> findAll(){
		Logger.log("\nQuery:\nall = \n\n");
		return cvservice.findAll();
	}
	
	@PostMapping("/search")
	public Iterable<CV> search(@RequestBody final SearchRequestDTO dto) {
		Logger.log("\nQuery:\nterm = "+dto.getSearchTerm()+", fields = "+CV.toJson(dto.getFields())+"\n\n");
		return cvservice.search(dto);
	}
	
	@PostMapping("/multisearch") 
	public Iterable<CV> multiSearch(@RequestBody final List<String> terms) {
		Logger.log("\nQuery:\nterm = "+CV.toJson(terms)+", fields = all\n\n");
		return cvservice.multiSearch(terms);
	}
	@GetMapping("/search/term={term}")
	public Iterable<CV> searchAll(@PathVariable String term){
		Logger.log("\nQuery:\nterm = "+term+", fields = all\n\n");
		return cvservice.searchAll(term);
	}
	
	@GetMapping("/search/cv/term={term}")
	public Iterable<CV> searchInCV(@PathVariable String term) {
		Logger.log("\nQuery:\nterm = "+term+", fields = CV\n\n");
		return cvservice.searchInCV(term);
	}
	@DeleteMapping("/remove/all")
	public void deleteAll() {
		Logger.log("\nRemove:\nall\n\n");
		cvservice.deleteAll();
	}
	@DeleteMapping("/remove/id={id}")
	public void deleteById(@PathVariable  UUID id) {
		Logger.log("\nRemove:\nid = "+id.toString()+"\n\n");
		cvservice.deleteById(id);;
	}
	
	
	@PutMapping("/update/email={email}")
	public void update(@PathVariable String email, @RequestBody final CV cv){
		try {
			
			Logger.log("\nUpdate instance: email = "+email+"\nNew CV Fields:\n"+CV.toJson(cv)+"\n\n");
			cvservice.update(email, cv);
		} catch (Exception e) {
			Logger.error("Failed to ipdate cv, email = "+email+"\n"+e.getMessage());
		}
	}
	
	
}
