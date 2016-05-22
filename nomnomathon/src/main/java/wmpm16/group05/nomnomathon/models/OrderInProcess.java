package wmpm16.group05.nomnomathon.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class OrderInProcess {

	@Id
	@GeneratedValue
	private Long orderId;
	private OrderState state;
	@ManyToOne
	private Customer customer;
	private Long restaurantid;
	@ManyToMany
	private List<Dish> dishes = new ArrayList<Dish>();

	public Long getOrderId() {
		return orderId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((dishes == null) ? 0 : dishes.hashCode());
		result = prime * result + ((orderId == null) ? 0 : orderId.hashCode());
		result = prime * result + ((restaurantid == null) ? 0 : restaurantid.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderInProcess other = (OrderInProcess) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (dishes == null) {
			if (other.dishes != null)
				return false;
		} else if (!dishes.equals(other.dishes))
			return false;
		if (orderId == null) {
			if (other.orderId != null)
				return false;
		} else if (!orderId.equals(other.orderId))
			return false;
		if (restaurantid == null) {
			if (other.restaurantid != null)
				return false;
		} else if (!restaurantid.equals(other.restaurantid))
			return false;
		if (state != other.state)
			return false;
		return true;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public OrderState getState() {
		return state;
	}

	public void setState(OrderState state) {
		this.state = state;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Optional<Long> getRestaurantid() {
		return Optional.ofNullable(restaurantid);
	}

	public void setRestaurantid(Optional<Long> restaurantid) {
		this.restaurantid = restaurantid.get() != null ? restaurantid.get() : null;
	}

	public List<Dish> getDishes() {
		return dishes;
	}

	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", state=" + state + ", customer=" + customer + ", restaurantid="
				+ restaurantid + ", dishes=" + dishes + "]";
	}

	public void proceed(OrderState state) {
		// TODO should check if the transition is valid or switch to next state
		this.state = state;
	}

	public void addDish(String dishName) {
		Dish dish = new Dish();
		dish.setDish(dishName);
		this.dishes.add(dish);
	}

}