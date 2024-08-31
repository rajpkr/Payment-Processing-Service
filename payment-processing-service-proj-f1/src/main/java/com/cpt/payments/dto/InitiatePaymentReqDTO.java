package com.cpt.payments.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitiatePaymentReqDTO {
	
	private int id;

	private List<LineItemDTO> lineItem;
	
	private String successUrl;
	private String cancelUrl;
	
}
