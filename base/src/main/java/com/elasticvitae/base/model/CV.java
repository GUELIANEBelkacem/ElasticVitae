package com.elasticvitae.base.model;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.elasticvitae.base.log.Logger;
import com.elasticvitae.base.reader.CVParser;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
/**
 * 
 * the main cv model
 *
 */
@Document(indexName = "cv")
@Setting(settingPath = "static/essettings.json")//, shards = 2)
public class CV {

	@Id
	@Field(type = FieldType.Keyword)
	@JsonProperty("id")
	private final UUID id;
	@NotNull
	@NotBlank
	@Field(type = FieldType.Text)
	private final String name;
	@NotNull
	@NotBlank
	@Field(type = FieldType.Text)
	private final String email;
	
	@NotNull
	@NotBlank
	@Field(type = FieldType.Text)
	private final String phone;
	
	@NotNull
	@NotBlank
	@Field(type = FieldType.Auto)
	private final String cvsource;
	
	@JsonProperty("cvdata")
	private final List<String> cvdata;

	public UUID getId() {
		return id;
	}

	public CV(
			  @JsonProperty("name") @NotNull @NotBlank String name, 
			  @JsonProperty("email") @NotNull @NotBlank String email, 
			  @JsonProperty("phone") String phone,
			  @JsonProperty("cvsource") String cvsource
			 ) {
		super();
		this.id = UUID.randomUUID();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.cvsource = cvsource;
		this.cvdata = CVParser.read(cvsource);
		
		Logger.log("Preparing CV with email: "+email+ " source: " + cvsource);
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}


	public String getCvsource() {
		return cvsource;
	}

	public List<String> getCvdata() {
		return cvdata;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
            return true;
        }
       
        if (!(o instanceof CV)) {
            return false;
        }
         
        CV c = (CV) o;
         
        return this.id.equals(c.getId());
    
	}
	public static String toJson(Object o) {
		String jsonText = "{}";
		try {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			jsonText = ow.writeValueAsString(o);
		}catch(Exception e) {
			Logger.error("Error generating json\n"+e.getMessage());
		}
		return jsonText; 
	}
	

}
