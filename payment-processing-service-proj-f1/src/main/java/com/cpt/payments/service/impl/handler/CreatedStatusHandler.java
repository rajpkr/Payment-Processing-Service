package com.cpt.payments.service.impl.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cpt.payments.dao.interfaces.TransactionDao;
import com.cpt.payments.dao.interfaces.TransactionLogDao;
import com.cpt.payments.dto.TransactionDTO;
import com.cpt.payments.dto.TransactionLogDTO;
import com.cpt.payments.service.interfaces.PaymentStatusHandler;

@Component
public class CreatedStatusHandler extends PaymentStatusHandler {
	
	private static final String TXN_FROM_STATUS_DURING_CREATE_TXN = "";

	@Autowired
	private TransactionDao txnDAO;
	
	@Autowired
	private TransactionLogDao txnLogDao;

	@Override
	public TransactionDTO processStatus(TransactionDTO payment) {		
		System.out.println("CreatedStatusHandler.processStatus() ||"+"payment: "+payment);
		
		TransactionDTO txnCreatedResponse = txnDAO.createTransaction(payment);
		
		System.out.println("Received below response from DAO ||txnCreatedResponse: "+txnCreatedResponse);
		
		TransactionLogDTO logDto = new TransactionLogDTO();
		logDto.setTransactionId(txnCreatedResponse.getId());
		logDto.setTxnFromStatus(TXN_FROM_STATUS_DURING_CREATE_TXN);
		logDto.setTxnToStatus(txnCreatedResponse.getTxnStatus());
		
		int insertCount = txnLogDao.createTransactionLog(logDto);
		
		if(insertCount == 1) {
			System.out.println("Txn Log Created| logDto: "+logDto);
		} else {
			System.out.println("Failed to insert Log in DB| logDto: "+logDto);
		}
		
		return txnCreatedResponse;
	}

}
