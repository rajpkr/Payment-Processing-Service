package com.cpt.payments.service.impl.provider.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.cpt.payments.dto.InitiatePaymentReqDTO;
import com.cpt.payments.dto.InitiatePaymentResDTO;
import com.cpt.payments.dto.LineItemDTO;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.service.http.HttpRequest;
import com.cpt.payments.service.http.HttpServiceEngine;
import com.cpt.payments.service.interfaces.PaymentStatusService;
import com.cpt.payments.stripeprovider.CreatePaymentReq;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StripeProviderHandlerTest2 {

	@Mock
	private ModelMapper modelMapper;

	@Mock
	private PaymentStatusService statusService;

	@Mock
	private HttpServiceEngine httpServiceEngine;
	
	@Mock
	private Gson gson;

	@InjectMocks
	private StripeProviderHandler stripeProviderHandler;

	//@Test
	public void testMethod() {
		log.info("running testMethod");

		//Arrange data
		TransactionDTO txn = new TransactionDTO();
		txn.setTxnReference("TXN-TEST-001");

		List<LineItemDTO> lineItemList = new ArrayList<>();
		LineItemDTO lineItemDto = LineItemDTO.builder()
				.currency("USD")
				.productName("Laptop")
				.quantity(1)
				.unitAmount(1000.00)
				.build(); 
		lineItemList.add(lineItemDto);

		InitiatePaymentReqDTO req = InitiatePaymentReqDTO.builder()
				.id(1)
				.lineItem(lineItemList)
				.successUrl("https://example.com/success")
				.cancelUrl("https://example.com/cancel")
				.build();

		CreatePaymentReq createPaymentReq = new CreatePaymentReq();
		createPaymentReq.setSuccessUrl("https://example.com/success");

		when(modelMapper.map(any(InitiatePaymentReqDTO.class), eq(CreatePaymentReq.class))).thenReturn(createPaymentReq);

		ReflectionTestUtils.setField(stripeProviderHandler, "stripeCreatePaymentUrl", "http://localhost:8086/v1/payments");

		ResponseEntity<String> responseEntity = new ResponseEntity<String>("", HttpStatus.CREATED);
		when(httpServiceEngine.makeHttpRequest(any(HttpRequest.class)))
		.thenReturn(responseEntity);

		//2 Invoke business logic
		InitiatePaymentResDTO response = stripeProviderHandler.processPayment(txn, req);

		//3 verify data

	}


}
