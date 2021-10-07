package com.etz.fraudeagleeyemanager.util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class ApiUtil {
	
	private final RestTemplate restTemplate;
	
	public String get(String path) {
		return this.<String>get(path, new HashMap<String, String>(), String.class);
	}

	public <T> T get(String path, Class<?> responseClassObject) {
		return get(path, new HashMap<String, String>(), responseClassObject);
	}
	
	public <T> T get(String path, Map<String, String> extraHeaders, Class<?> responseClassObject) {
		URI uri = URI.create(path);
		HttpEntity<String> requestEntity = new HttpEntity<>(computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.GET, requestEntity, responseClassObject);
	}
	
	public String post(String path, Object requestClassOrJsonString) {
		return this.<String>post(path, new HashMap<String, String>(), requestClassOrJsonString, String.class);
	}
	
	public String post(String path, Map<String, String> extraHeaders, Object requestClassOrJsonString) {
		return this.<String>post(path, extraHeaders, requestClassOrJsonString, String.class);
	}
	
	public <T> T post(String path, Object requestClassOrJsonString, Class<?> responseClassObject) {
		return post(path, new HashMap<String, String>(), requestClassOrJsonString, responseClassObject);
	}
	
	public <T> T post(String path, Map<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject) {
		URI uri = URI.create(path);
		String requestBody = JsonConverter.objectToJson(requestClassOrJsonString);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.POST, requestEntity, responseClassObject);
	}
	
	public <T> T post(String path, Map<String, String> extraHeaders, MultiValueMap<String, String> requestClassOrJsonString, Class<?> responseClassObject) {
		URI uri = URI.create(path);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestClassOrJsonString, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.POST, requestEntity, responseClassObject);
	}
	
	public <T> T put(String path, Object requestClassOrJsonString, Class<?> responseClassObject) {
		return put(path, new HashMap<String, String>(), requestClassOrJsonString, responseClassObject, new HashMap<String, String>());
	}
	
	public <T> T put(String path, Object requestClassOrJsonString, Class<?> responseClassObject, Map<String, String> parameter) {
		return put(path, new HashMap<String, String>(), requestClassOrJsonString, responseClassObject, parameter);
	}
	
	public <T> T put(String path, Map<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject) {
		return put(path, extraHeaders, requestClassOrJsonString, responseClassObject, new HashMap<String, String>());
	}
	
	public <T> T put(String path, Map<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject, Map<String, String> parameters) {
		URI uri = URI.create(path);
		String requestBody = JsonConverter.objectToJson(requestClassOrJsonString);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.PUT, requestEntity, responseClassObject, parameters);
	}
	
	public <T> T delete(String path, Map<String, String> parameters) {
		return delete(path, new HashMap<String, String>(), "{}", String.class, parameters);
	}
	
	public <T> T delete(String path, Map<String, String> extraHeaders, Object requestClassOrJsonString, Class<?> responseClassObject, Map<String, String> parameters) {
		URI uri = URI.create(path);
		String requestBody = JsonConverter.objectToJson(requestClassOrJsonString);
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, computeHeaders(extraHeaders));
		return returnContent(uri, HttpMethod.DELETE, requestEntity, responseClassObject, parameters);
	}
	
	
	
	private <T> T returnContent(URI uri, HttpMethod httpMethod, HttpEntity<?> requestEntity, Class<?> responseClassObject) {	
		ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, requestEntity, String.class);
		return JsonConverter.jsonToObject(response.getBody(), responseClassObject);
	}
	
//	private <T> T returnContent(URI uri, HttpMethod httpMethod, HttpEntity<String> requestEntity, Class<?> responseClassObject) {	
//		ResponseEntity<String> response = restTemplate.exchange(uri, httpMethod, requestEntity, String.class);
//		return JsonConverter.jsonToObject(response.getBody(), responseClassObject);
//    }

	private <T> T returnContent(URI uri, HttpMethod httpMethod, HttpEntity<String> requestEntity, Class<?> responseClassObject, Map<String, String> parameters) {	
		ResponseEntity<String> response = restTemplate.exchange(uri.toString(), httpMethod, requestEntity, String.class, parameters);
		return JsonConverter.jsonToObject(response.getBody(), responseClassObject);
    }
	
	private static HttpHeaders computeHeaders(Map<String, String> extraHeaders) {
		HttpHeaders headers = new HttpHeaders();
		//Compute headers
		if ((extraHeaders != null) && (extraHeaders.size() > 0)){
	      for(Map.Entry<String, String> pair : extraHeaders.entrySet()) {
			headers.set(pair.getKey(), pair.getValue());
	      }
	    }
		return headers;
	}
	
}





