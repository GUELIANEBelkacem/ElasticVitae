package com.elasticvitae.base.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import com.elasticvitae.base.log.Logger;

public class WordParser {

	
	
	public static String readLocal(String path){
		try {
			if(path.endsWith(".doc")){
				File file = null;
			    WordExtractor extractor = null;
			    String text = "";
			    
			    
			
		        file = new File(path);
		        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		        HWPFDocument document = new HWPFDocument(fis);
		        extractor = new WordExtractor(document);
		        String[] fileData = extractor.getParagraphText();
		        
		        for (int i = 0; i < fileData.length; i++)
		        {
		            if (fileData[i] != null) {
		                text += fileData[i];
		            }
		            
		        }
		        extractor.close();
		        return text;
			}
			else {
				if(path.endsWith(".docx")){
					File file = new File(path);
	                FileInputStream fis = new FileInputStream(file.getAbsolutePath());
	                String text = "";
	                XWPFDocument document = new XWPFDocument(fis);
	
	                List<XWPFParagraph> paragraphs = document.getParagraphs();
	
	
	                for (XWPFParagraph para : paragraphs) {
	                    text+= para.getText()+"\n";
	                }
	                document.close();
	                fis.close();
	                return text;
				}
				else return "";
			}
		}
		catch(Exception e) {
			Logger.error("Error while parsing word file: "+path+"\n"+e.getMessage());
			return "";
		}
	}
	
	public static String readOnline(String url) throws Exception {
		try {
			 SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
				    @Override
				    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				        return true;
				    }
				}).build();
				HostnameVerifier hnv = new NoopHostnameVerifier();      
				SSLConnectionSocketFactory sslcf = new SSLConnectionSocketFactory(sslContext, hnv);     
				CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslcf).build();
		     
		     HttpGet httpGet = new HttpGet(url);
		     CloseableHttpResponse response = httpClient.execute(httpGet);
		     
		     HttpEntity en =  response.getEntity();
		     
			if(url.endsWith(".doc")){
			    WordExtractor extractor = null;
			    String text = "";
			    
			    
		        //FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		        InputStream fis = en.getContent();
		        HWPFDocument document = new HWPFDocument(fis);
		        extractor = new WordExtractor(document);
		        String[] fileData = extractor.getParagraphText();
		        
		        for (int i = 0; i < fileData.length; i++)
		        {
		            if (fileData[i] != null) {
		                text += fileData[i];
		            }
		            
		        }
		        extractor.close();
		        return text;
	        
			}
			else {
				if(url.endsWith(".docx")){
					
	                InputStream fis = en.getContent();
	                String text = "";
	                XWPFDocument document = new XWPFDocument(fis);
	
	                List<XWPFParagraph> paragraphs = document.getParagraphs();
	
	
	                for (XWPFParagraph para : paragraphs) {
	                    text+= para.getText()+"\n";
	                }
	                document.close();
	                fis.close();
	                return text;
				}
				else return "";
		}
	}
	catch(Exception e) {
		Logger.error("Error while parsing word file (URL): "+url+"\n"+e.getMessage());
		return "";
	}
	}
	
	
}
