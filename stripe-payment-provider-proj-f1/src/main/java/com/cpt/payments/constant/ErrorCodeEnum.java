package com.cpt.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

	GENERIC_ERROR("30000","Unable to process, please try later.."),
	INVALID_SESSION_URL("30001","Invalid sessionURL received from STRIPE"),
	UNABLE_TO_CONNECT_WITH_STRIPE("30002","Unable to connect with Stripe, Try again later.."),
	
	DUPLICATE_TXN_REF("20003","Invalid Request. Duplicate TxnReference");

	private String errorCode;
	private String errorMessage;
	
	ErrorCodeEnum(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
}
