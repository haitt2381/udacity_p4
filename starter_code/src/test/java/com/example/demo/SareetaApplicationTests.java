package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SareetaApplicationTests {

	@InjectMocks
	private CartController cartController;

	@InjectMocks
	private ItemController itemController;

	@InjectMocks
	private OrderController orderController;

	@InjectMocks
	private UserController userController;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private ItemRepository itemRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	@Test
	public void contextLoads() {
	}

	@Test
	public void createUserTest() {
		CreateUserRequest createUserRequest = new CreateUserRequest("hai", "198237123123123", "198237123123123");
		ResponseEntity<User> response = userController.createUser(createUserRequest);
		assertEquals(createUserRequest.getUsername(), response.getBody().getUsername());
	}

	@Test
	public void findByUsernameTest() {
		String username = "hai";
		User user = new User("hai", "1209319203129", null);
		when(userRepository.findByUsername(any())).thenReturn(user);
		ResponseEntity<User> response = userController.findByUserName(username);
		assertEquals(username, response.getBody().getUsername());
	}

	@Test
	public void findByIdTest() {
		String username = "hai";
		User user = new User("hai", "1209319203129", null);
		when(userRepository.findById(any())).thenReturn(Optional.of(user));
		ResponseEntity<User> response = userController.findById(1L);
		assertEquals(username, response.getBody().getUsername());
	}

	@Test
	public void addToCartTest() {
		User user = new User("hai", "1209319203129", null);
		Cart cart = new Cart();
		cart.setUser(user);
		user.setCart(cart);
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.of(item));

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest("hai", 1, 1);
		ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
		assertEquals("hai", response.getBody().getUser().getUsername());
	}

	@Test
	public void addToCart_NotFoundUserTest() {
		when(userRepository.findByUsername(any())).thenReturn(null);

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest("hai", 1, 1);
		ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void addToCart_NotFoundItemTest() {
		User user = new User("hai", "1209319203129", null);
		Cart cart = new Cart();
		cart.setUser(user);
		user.setCart(cart);
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.empty());

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest("hai", 1, 1);
		ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void removeFromCartTest() {
		User user = new User("hai", "1209319203129", null);
		Cart cart = new Cart();
		cart.setUser(user);
		user.setCart(cart);
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.of(item));

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest("hai", 1, 1);
		ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
		assertEquals("hai", response.getBody().getUser().getUsername());
	}

	@Test
	public void removeFromCart_NotFoundUserTest() {
		when(userRepository.findByUsername(any())).thenReturn(null);

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest("hai", 1, 1);
		ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void removeFromCart_NotFoundItemTest() {
		User user = new User("hai", "1209319203129", null);
		Cart cart = new Cart();
		cart.setUser(user);
		user.setCart(cart);
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");
		when(userRepository.findByUsername(any())).thenReturn(user);
		when(itemRepository.findById(any())).thenReturn(Optional.empty());

		ModifyCartRequest modifyCartRequest = new ModifyCartRequest("hai", 1, 1);
		ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void submitOrderTest() {
		User user = new User("hai", "1209319203129", null);
		Cart cart = new Cart();
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");
		cart.setUser(user);
		cart.setTotal(BigDecimal.ONE);
		cart.setItems(Lists.newArrayList(item));
		user.setCart(cart);

		when(userRepository.findByUsername(any())).thenReturn(user);

		ResponseEntity<UserOrder> response = orderController.submit("hai");
		assertEquals(user.getUsername(), response.getBody().getUser().getUsername());
		assertEquals(cart.getTotal(), response.getBody().getTotal());
	}

	@Test
	public void submitOrder_NotFoundUserTest() {
		when(userRepository.findByUsername(any())).thenReturn(null);

		ResponseEntity<UserOrder> response = orderController.submit("hai");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void getOrderTest() {
		User user = new User("hai", "1209319203129", null);
		Cart cart = new Cart();
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");
		cart.setUser(user);
		cart.setTotal(BigDecimal.ONE);
		cart.setItems(Lists.newArrayList(item));
		user.setCart(cart);

		UserOrder userOrder = new UserOrder(Lists.newArrayList(item), user, BigDecimal.TEN);

		when(userRepository.findByUsername(any())).thenReturn(user);
		when(orderRepository.findByUser(any())).thenReturn(Lists.newArrayList(userOrder));

		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("hai");
		assertNotNull(response.getBody());
		assertEquals(user.getUsername(), response.getBody().get(0).getUser().getUsername());
		assertFalse(response.getBody().get(0).getItems().isEmpty());
	}

	@Test
	public void getOrder_UserNotFoundTest() {
		when(userRepository.findByUsername(any())).thenReturn(null);

		ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("hai");
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test
	public void getItemsByName() {
		Item item = new Item("item 1", BigDecimal.valueOf(1000), "");

		when(itemRepository.findByName(any())).thenReturn(Lists.newArrayList(item));
		ResponseEntity<List<Item>> response = itemController.getItemsByName("item 1");
		assertEquals(item.getName(), response.getBody().get(0).getName());
	}


}
