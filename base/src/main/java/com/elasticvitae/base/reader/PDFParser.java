package com.elasticvitae.base.reader;

import java.io.File;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.elasticvitae.base.log.Logger; 

public class PDFParser {

	
	public static String readLocal(String path) throws Exception {
		try {
			String text = "";
			PDDocument document = PDDocument.load(new File(path));
			if (!document.isEncrypted()) {
				
			    PDFTextStripper stripper = new PDFTextStripper();
			    text = stripper.getText(document);
			}
			document.close();
			return text;
		}
		catch(Exception e) {
			Logger.error("Error while parsing PDF file: "+path+"\n"+e.getMessage());
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
		     
			String text = "";
			//InputStream is = new URL(url).openStream();
			//URL urlq = new URL(url);
			//URLConnection connection = urlq.openConnection(); 
			//InputStream is = connection.getInputStream(); 
			InputStream is = en.getContent();
			PDDocument document = PDDocument.load(is);
			if (!document.isEncrypted()) {
				
			    PDFTextStripper stripper = new PDFTextStripper();
			    text = stripper.getText(document);
			   
			}
			document.close();
			return text;
		}
		catch(Exception e) {
			Logger.error("Error while parsing PDF file(URL): "+url+"\n"+e.getMessage());
			
			return "";
		}
	}
	
	
}
