package com.cpt.payments.stripeprovider;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentReq {

	private String txnRef;
	
	private List<LineItem> lineItem;
	
	private String successUrl;
	private String cancelUrl;
	
}
