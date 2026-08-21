package com.product_order_system.serviceImpl;

import com.product_order_system.dto.request.CartItemRequest;
import com.product_order_system.dto.response.CartItemResponse;
import com.product_order_system.dto.response.CartResponse;
import com.product_order_system.entity.Cart;
import com.product_order_system.entity.CartItem;
import com.product_order_system.entity.Product;
import com.product_order_system.entity.User;
import com.product_order_system.exception.BadRequestException;
import com.product_order_system.exception.ResourceNotFoundException;
import com.product_order_system.repository.CartItemRepository;
import com.product_order_system.repository.CartRepository;
import com.product_order_system.repository.ProductRepository;
import com.product_order_system.repository.UserRepository;
import com.product_order_system.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getCart(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cartRepository.save(newCart);
        });

        return mapToResponse(cart);
    }

    @Override
    public CartResponse addItem(Long userId, CartItemRequest request) {

        // Find user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Find product
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // Product must be active
        if (!product.isActive()) {
            throw new BadRequestException("Product is disabled: " + product.getName());
        }

        // Find existing cart or create one
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {

            Cart newCart = new Cart();
            newCart.setUser(user);

            return cartRepository.save(newCart);
        });

        // Check whether product already exists
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId()).orElse(null);

        if (cartItem == null) {

            cartItem = new CartItem();

            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());

        } else {

            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        }

        cartItemRepository.save(cartItem);

        return mapToResponse(cart);
    }

    @Override
    public CartResponse updateItem(Long userId, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        cartItem.setQuantity(quantity);

        cartItemRepository.save(cartItem);

        return mapToResponse(cart);
    }

    @Override
    public void removeItem(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId).orElseThrow(() -> new ResourceNotFoundException("Product not found in cart"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        cartItemRepository.deleteAll(items);
    }

    private CartResponse mapToResponse(Cart cart) {

        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());

        List<CartItemResponse> itemResponses = items.stream().map(this::mapItemToResponse).toList();

        BigDecimal totalAmount = itemResponses.stream().map(CartItemResponse::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), cart.getUser().getId(), itemResponses, totalAmount);
    }

    private CartItemResponse mapItemToResponse(CartItem item) {

        BigDecimal totalPrice = item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(), item.getProduct().getPrice(), item.getQuantity(), totalPrice);
    }
}