package com.cpt.payments.service.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class HttpRequest {

	String url;
	HttpMethod method; 
	Object request;
	HttpHeaders httpHeaders;
}
