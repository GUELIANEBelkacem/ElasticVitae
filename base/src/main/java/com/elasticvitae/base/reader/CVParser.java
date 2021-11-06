package com.elasticvitae.base.reader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.elasticvitae.base.log.Logger;

/**
 * 
 * the class that parses documents to generate a CV
 * it checks the type of the document (doc, docx, pdf), it also checks whether the source is a 
 * local path or a link, and the it takes the document and returns a list of strings
 *
 */
public class CVParser {

	public static List<String> read(String source) {

		if(source == null || source.isBlank()) return new ArrayList<String>();
		List<String> text = Collections.emptyList();
		
		try {
		
		if(source.endsWith(".doc")) {
			if(CVParser.isValidURL(source)) text = Arrays.asList((WordParser.readOnline(source).split("\r\n") ));
			else text = Arrays.asList((WordParser.readLocal(source).split("\r\n") ));
		}else {
		if(source.endsWith(".pdf")) {
			if(CVParser.isValidURL(source)) text = Arrays.asList((PDFParser.readOnline(source).split("\r\n") ));
			else text = Arrays.asList((PDFParser.readLocal(source).split("\r\n") ));
		}
		else {
			throw new Exception("Source not a pdf not word\n");
		}
		}
		}catch(Exception e) {
			Logger.error("Error while parsing file: "+source+"\n"+e.getMessage());
		}
		if(!CollectionUtils.isEmpty(text)) {
			List<String> done = new ArrayList<String>();
			for(String s:text) {
				Collections.addAll(done, s.split(" "));
				
			}
			return done;
		}
		return text;
	}
	
	 public static boolean isValidURL(String url)
	 {
	       
	        try {
	            new URL(url).toURI();
	            return true;
	        }
	        catch (Exception e) {
	            return false;
	        }
	 }
}
