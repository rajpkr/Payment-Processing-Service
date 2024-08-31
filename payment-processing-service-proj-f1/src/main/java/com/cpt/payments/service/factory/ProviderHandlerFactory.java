package com.cpt.payments.service.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.cpt.payments.constant.ProviderEnum;
import com.cpt.payments.service.impl.provider.handler.StripeProviderHandler;
import com.cpt.payments.service.interfaces.ProviderHandler;

@Component
public class ProviderHandlerFactory {
	
	@Autowired
	private ApplicationContext ctx;

	public ProviderHandler getProviderHandler(ProviderEnum providerEnum) {
		
		switch(providerEnum) {
		case STRIPE:
			System.out.println("Creating StripeProviderHandler for status: "+providerEnum);
			StripeProviderHandler stripeProviderHandler = ctx.getBean(StripeProviderHandler.class);
			System.out.println("Got Bean from ApllicationContext ||"+"stripeProviderHandler: "+stripeProviderHandler);
			return stripeProviderHandler;
			
			
		default:
			System.out.println("Unable to find Handler");
			return null;
		}
		
	}
}
