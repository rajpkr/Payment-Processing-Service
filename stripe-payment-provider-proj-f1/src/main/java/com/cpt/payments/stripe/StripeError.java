package com.cpt.payments.stripe;

import lombok.Data;

@Data
public class StripeError {

	String type;
	String code;
	String decline_code;
	String param;
	String message;
}
