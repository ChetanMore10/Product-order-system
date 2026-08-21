package com.product_order_system.exception;

public class InsufficientInventoryException extends RuntimeException {

    public InsufficientInventoryException(String message){
        super(message);
    }
}
