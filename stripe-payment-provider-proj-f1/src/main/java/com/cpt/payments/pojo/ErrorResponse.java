package com.cpt.payments.pojo;

import lombok.Data;

@Data
public class ErrorResponse {

	private String errorCode;
	private String errorMessage;
}
