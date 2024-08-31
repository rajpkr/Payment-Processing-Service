package com.cpt.payments.service.recon;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cpt.payments.dto.TransactionDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReconTransactionAsync {

	@Async
	public void processItem(TransactionDTO txnDto) {
		log.info("Processing item: "+txnDto);
	}
}
	