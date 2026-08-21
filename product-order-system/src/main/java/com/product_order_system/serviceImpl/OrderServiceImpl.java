package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.CheckoutRequest;
import com.product_order_system.dto.response.OrderItemResponse;
import com.product_order_system.dto.response.OrderResponse;
import com.product_order_system.entity.*;
import com.product_order_system.exception.BadRequestException;
import com.product_order_system.exception.InsufficientInventoryException;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.*;
import com.product_order_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public OrderResponse checkout(Long userId, CheckoutRequest request) {

        // 1. Validate User
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // 2. Validate Cart
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // 3. Validate Address
        Address address = addressRepository.findById(request.getAddressId()).orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + request.getAddressId()));

        // 4. Validate Address belongs to User
        if (!address.getUser().getId().equals(userId)) {
            throw new BadRequestException("Address does not belong to this user");
        }

        // 5. Create Order
        CustomerOrder order = new CustomerOrder();

        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PLACED);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);

        CustomerOrder savedOrder = orderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 6. Validate every cart item
        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            // Product must be active
            if (!product.isActive()) {
                throw new BadRequestException("Product is disabled: " + product.getName());
            }

            // 7. Lock inventory row
            Inventory inventory = inventoryRepository.findByProductIdForUpdate(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Inventory not found for product id: " + product.getId()));

            // 8. Check inventory
            if (inventory.getQuantity() < cartItem.getQuantity()) {

                throw new InsufficientInventoryException("Insufficient inventory for product: " + product.getName() + ". Available: " + inventory.getQuantity() + ", Requested: " + cartItem.getQuantity());
            }

            // 9. Reduce inventory
            inventory.setQuantity(inventory.getQuantity() - cartItem.getQuantity());

            inventoryRepository.save(inventory);

            // 10. Calculate item total
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);

            // 11. Create Order Item
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());

            // Store price at purchase time
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }

        // 12. Update total amount
        savedOrder.setTotalAmount(totalAmount);

        orderRepository.save(savedOrder);

        // 13. Clear cart
        cartItemRepository.deleteAll(cartItems);

        // 14. Return response
        return mapToResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long userId, Long orderId) {

        CustomerOrder order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Order not found");
        }

        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return orderRepository.findByUserId(userId).stream().map(this::mapToResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    private OrderResponse mapToResponse(CustomerOrder order) {

        List<OrderItemResponse> items = orderItemRepository.findByOrderId(order.getId()).stream().map(this::mapItemToResponse).toList();

        return new OrderResponse(order.getId(), order.getUser().getId(), order.getUser().getUsername(), order.getAddress().getId(), order.getTotalAmount(), order.getStatus().name(), order.getCreatedAt(), items);
    }

    private OrderItemResponse mapItemToResponse(OrderItem item) {

        BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return new OrderItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getPrice(), totalPrice);
    }
}
