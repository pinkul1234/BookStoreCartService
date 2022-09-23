package com.bridgelabz.bookstorecartservice.service;

import com.bridgelabz.bookstorecartservice.dto.CartDto;
import com.bridgelabz.bookstorecartservice.model.CartModel;
import com.bridgelabz.bookstorecartservice.util.CartResponse;
import com.bridgelabz.bookstorecartservice.util.Response;

import java.util.List;

public interface ICartService {


    

    CartResponse updateQuantity(Long cartId, String token, Integer quantity);

    List<CartModel> getAllCartItems(String token);

    CartResponse removingCart(String token, Long cartId);


    CartResponse addCart(CartDto cartDto, String token, Long bookId);
}
