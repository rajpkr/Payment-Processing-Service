package com.cpt.payments.service.impl.provider.handler;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cpt.payments.constant.ErrorCodeEnum;
import com.cpt.payments.dto.InitiatePaymentReqDTO;
import com.cpt.payments.dto.InitiatePaymentResDTO;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.exception.ProcessingServiceException;
import com.cpt.payments.service.http.HttpRequest;
import com.cpt.payments.service.http.HttpServiceEngine;
import com.cpt.payments.service.interfaces.PaymentStatusService;
import com.cpt.payments.stripeprovider.CreatePaymentReq;
import com.cpt.payments.stripeprovider.CreatePaymentRes;
import com.cpt.payments.stripeprovider.ErrorResponse;
import com.google.gson.Gson;

@ExtendWith(MockitoExtension.class)
public class StripeProviderHandlerTest {

    @InjectMocks
    private StripeProviderHandler stripeProviderHandler;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private HttpServiceEngine httpServiceEngine;

    @Mock
    private Gson gson;

    @Mock
    private PaymentStatusService statusService;

    private TransactionDTO txn;
    private InitiatePaymentReqDTO req;
    private HttpRequest httpRequest;
    private CreatePaymentReq createPaymentReq;
    private InitiatePaymentResDTO initiatePaymentResDTO;
    private ResponseEntity<String> responseEntity;

    @BeforeEach
    public void setUp() {
        // Sample objects initialization
        txn = new TransactionDTO();
        txn.setTxnReference("txnRef");
        txn.setTxnStatus("INITIATED");

        req = new InitiatePaymentReqDTO();
        
        createPaymentReq = new CreatePaymentReq();
        createPaymentReq.setTxnRef("txnRef");

        initiatePaymentResDTO = new InitiatePaymentResDTO();
        initiatePaymentResDTO.setId("12345");
        initiatePaymentResDTO.setTxnReference("txnRef");

        httpRequest = new HttpRequest();
        httpRequest.setMethod(HttpMethod.POST);
        httpRequest.setUrl("http://test.com");
    }

    @Test
    public void testProcessPayment_Success() {// Working
        // Arrange
        String responseBody = "{\"id\":\"12345\"}";
        responseEntity = new ResponseEntity<>(responseBody, HttpStatus.CREATED);

        when(modelMapper.map(req, CreatePaymentReq.class)).thenReturn(createPaymentReq);
        when(httpServiceEngine.makeHttpRequest(any(HttpRequest.class))).thenReturn(responseEntity);
        when(gson.fromJson(responseEntity.getBody(), CreatePaymentRes.class)).thenReturn(new CreatePaymentRes("12345",""));
        when(modelMapper.map(any(CreatePaymentRes.class), eq(InitiatePaymentResDTO.class))).thenReturn(initiatePaymentResDTO);

        // Act
        InitiatePaymentResDTO result = stripeProviderHandler.processPayment(txn, req);

        // Assert
        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertEquals("txnRef", result.getTxnReference());

        verify(statusService, times(2)).processStatus(txn);
        assertEquals("PENDING", txn.getTxnStatus());
    }

    @Test
    public void testProcessPayment_ClientError() {//working
        // Arrange
        String errorResponseBody = "{\"errorCode\":\"400\", \"errorMessage\":\"Client error\"}";
        responseEntity = new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);

		when(modelMapper.map(req, CreatePaymentReq.class)).thenReturn(createPaymentReq);
        when(httpServiceEngine.makeHttpRequest(any(HttpRequest.class))).thenReturn(responseEntity);
        when(gson.fromJson(responseEntity.getBody(), ErrorResponse.class)).thenReturn(new ErrorResponse());

        // Act & Assert
        ProcessingServiceException exception = assertThrows(ProcessingServiceException.class,
                () -> stripeProviderHandler.processPayment(txn, req));

        assertEquals("400", exception.getErrorCode());
        assertEquals("Client error", exception.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testProcessPayment_ServerError() {// Working
        // Arrange
        String errorResponseBody = "{\"errorCode\":\"500\", \"errorMessage\":\"Server error\"}";
        responseEntity = new ResponseEntity<>(errorResponseBody, HttpStatus.INTERNAL_SERVER_ERROR);

        when(modelMapper.map(req, CreatePaymentReq.class)).thenReturn(createPaymentReq);
        when(httpServiceEngine.makeHttpRequest(any(HttpRequest.class))).thenReturn(responseEntity);
        when(gson.fromJson(responseEntity.getBody(), ErrorResponse.class)).thenReturn(new ErrorResponse());

        // Act & Assert
        ProcessingServiceException exception = assertThrows(ProcessingServiceException.class,
                () -> stripeProviderHandler.processPayment(txn, req));

        assertEquals("500", exception.getErrorCode());
        assertEquals("Server error", exception.getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void testProcessPayment_NonCreatedResponse() {//WORKING
        // Arrange
        responseEntity = new ResponseEntity<>("", HttpStatus.ACCEPTED);

        when(modelMapper.map(req, CreatePaymentReq.class)).thenReturn(createPaymentReq);
        when(httpServiceEngine.makeHttpRequest(any(HttpRequest.class))).thenReturn(responseEntity);

        // Act & Assert
        ProcessingServiceException exception = assertThrows(ProcessingServiceException.class,
                () -> stripeProviderHandler.processPayment(txn, req));

        assertEquals(ErrorCodeEnum.GENERIC_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(), exception.getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }

    @Test
    public void testProcessPayment_HttpCallFailure() {
        // Arrange
        responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    	
    	when(modelMapper.map(req, CreatePaymentReq.class)).thenReturn(createPaymentReq);
        when(httpServiceEngine.makeHttpRequest(any(HttpRequest.class))).thenReturn(responseEntity);

        // Act & Assert
        ProcessingServiceException exception = assertThrows(ProcessingServiceException.class,
                () -> stripeProviderHandler.processPayment(txn, req));

        assertEquals(ErrorCodeEnum.GENERIC_ERROR.getErrorCode(), exception.getErrorCode());
        assertEquals(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage(), exception.getErrorMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
    }
}

