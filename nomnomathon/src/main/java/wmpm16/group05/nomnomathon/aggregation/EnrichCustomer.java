package wmpm16.group05.nomnomathon.aggregation;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.log4j.Logger;

import wmpm16.group05.nomnomathon.domain.OrderRequest;
import wmpm16.group05.nomnomathon.models.Customer;
import wmpm16.group05.nomnomathon.models.OrderInProcess;
import wmpm16.group05.nomnomathon.models.OrderState;
import wmpm16.group05.nomnomathon.routers.NomNomConstants;

public class EnrichCustomer implements AggregationStrategy{
	private final Logger log = Logger.getLogger(EnrichCustomer.class);
	
	@Override
	public Exchange aggregate(Exchange arg0, Exchange arg1) {
		log.debug("Aggregating " + arg0.getIn().getBody().toString() + " and " + arg1.getIn().getBody().toString());
		
		OrderInProcess order = new OrderInProcess();
		OrderRequest orderRequest = (OrderRequest) arg0.getIn().getBody();
		Customer customer = (Customer) arg1.getIn().getBody();
		Message in = arg0.getIn();
	
		order.setCustomer(customer);
		order.setState(OrderState.CREATED);
		
		if (orderRequest.getDishes() != null) {
			orderRequest.getDishes().forEach((String dishName) -> order.addDish(dishName));
		}
		
		if (orderRequest.getRestaurantId() != null && orderRequest.getRestaurantId().isPresent()) {
			order.setRestaurantId(orderRequest.getRestaurantId().get());
		}
		
		// Exchange transform OrderRequest to Order 
		in.setBody(order);
		in.setHeader(NomNomConstants.HEADER_TYPE, orderRequest.getType());
		
		arg0.setIn(in);
		
		return arg0;
	}

}
