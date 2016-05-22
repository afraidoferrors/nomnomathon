package wmpm16.group05.nomnomathon.routers;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import wmpm16.group05.nomnomathon.models.CustomerNotificationType;
import wmpm16.group05.nomnomathon.models.OrderState;

/**
 * Route to notify users on status changes of his order.
 */
@Component
public class CustomerNotifyRouter extends RouteBuilder {

	@Value("${mail.host}")
	private String host;
	
	@Value("${mail.port}")
	private String port;
	
	@Value("${mail.pass}")
	private String pass;
	
	@Value("${mail.user}")
	private String user;
	
	
    @Override
    public void configure() throws Exception {
    	
    	/*direct notification to preferred communication channel*/
        from("direct:sendCustomerNotification")
	        .choice()
	        	.when(header("notificationType").isEqualTo(CustomerNotificationType.SMS))
	        		.to("direct:notifyCustomerSms")
		        .when(header("notificationType").isEqualTo(CustomerNotificationType.MAIL))
	        		.to("direct:notifyCustomerMail")
		        .when(header("notificationType").isEqualTo(CustomerNotificationType.REST))
	        		.to("direct:notifyCustomerRest")
	        .end();
	        		
	
        /* Send SMS to Customer */
        from("direct:notifyCustomerSms")
			.to("log:notifyCustomerSms");
    
        /* Send Mail to Customer */
        from("direct:notifyCustomerMail")
	        .setHeader("Content-type", constant("text/html"))
	        .choice()
	        	.when(header("orderState").isEqualTo(OrderState.REJECTED_NO_CAPACITY))
	        		.setHeader("subject", constant("NomNom - No Capacity"))
	    			.to("chunk:mail_no_capacity")
	        	.when(header("orderState").isEqualTo(OrderState.REJECTED_INVALID_PAYMENT))
        			.setHeader("subject", constant("NomNom - Payment failed"))
	    			.to("chunk:mail_invalid_payment")
		        .when(header("orderState").isEqualTo(OrderState.FULLFILLED))
        			.setHeader("subject", constant("NomNom - Order finished"))
	    			.to("chunk:mail_fullfilled")
	        .end()
			.wireTap("log:mail:send")
			.to("smtp://" + host + "?password=" + pass + "&username=" + user + "&from=" + user);
    
        /* Send REST to Customer */
        from("direct:notifyCustomerRest")
			.to("log:notifyCustomerRest");
        
        
    }
}

