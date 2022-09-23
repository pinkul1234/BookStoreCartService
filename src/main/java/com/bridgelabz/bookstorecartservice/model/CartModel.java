package com.bridgelabz.bookstorecartservice.model;

import com.bridgelabz.bookstorecartservice.dto.CartDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Cart")
public class CartModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;
    private long cartId;
    private long userId;
    private int quantity;
    private int totalPrice;

    public CartModel(CartDto cartDto) {
        this.quantity = cartDto.getQuantity();
    }


    public CartModel() {

    }
}
