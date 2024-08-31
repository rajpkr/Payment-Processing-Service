package com.cpt.payments.service.recon;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cpt.payments.dao.interfaces.TransactionDao;
import com.cpt.payments.dto.TransactionDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReconService {
	
	@Autowired
	private ReconTransactionAsync reconTxnAsync;
	
	@Autowired
	private TransactionDao txnDao;

//	Runs every 15 min..
//	@Scheduled(cron = "50 42 16 25 8 SUN,MON")
	@Scheduled(cron = "0 0/1 * * * ?")

	public void performTask() {
		log.info("Task executed");
		
		List<TransactionDTO> txnForRecon = txnDao.fetchTransactionsForReconcilation();
		
		log.info("About to process recon for txnForRecon.size: "+txnForRecon.size());
		
		List<String> items = Arrays.asList("Item1", "Item2", "Item3", "Item4");
		
		for(TransactionDTO item : txnForRecon) {
			log.trace("submit task for async execution|item: "+item);
			reconTxnAsync.processItem(item);
		}
		
	}
}
