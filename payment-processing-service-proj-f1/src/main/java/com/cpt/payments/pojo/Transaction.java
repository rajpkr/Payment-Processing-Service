package com.cpt.payments.pojo;

import lombok.Data;

@Data
public class Transaction {

    private int id;
    private int userId;
    
    private String paymentMethod;
    private String provider;
    private String paymentType;
    private String txnStatus;
    
    private double amount;
    private String currency;
    private String merchantTransactionReference;
    private String txnReference;
    private String providerReference;
    private String providerCode;
    private String providerMessage;
    private int retryCount;

}
