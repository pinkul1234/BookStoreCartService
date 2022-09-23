package com.bridgelabz.bookstorecartservice.controller;

import com.bridgelabz.bookstorecartservice.dto.CartDto;
import com.bridgelabz.bookstorecartservice.model.CartModel;
import com.bridgelabz.bookstorecartservice.service.ICartService;
import com.bridgelabz.bookstorecartservice.util.CartResponse;
import com.bridgelabz.bookstorecartservice.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    ICartService cartService;

    @PostMapping("/addcart")
    public ResponseEntity<CartResponse> addCart(@RequestBody CartDto cartDto, @RequestHeader String token, @RequestParam Long bookId) {
        CartResponse cartResponse = cartService.addCart(cartDto,token,bookId);
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }

    @PutMapping("/updatequantity/{cartId}")
    public ResponseEntity<CartResponse> updateQuantity(@PathVariable Long cartId, @RequestHeader String token, @RequestParam Integer quantity) {
        CartResponse cartResponse = cartService.updateQuantity(cartId,token,quantity);
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }
    @GetMapping("/getallcartitems")
    public ResponseEntity<List<?>> getAllCartItems(@RequestHeader String token) {
        List<CartModel> cartResponse = cartService.getAllCartItems(token);
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }
    @DeleteMapping("/removingcart")
    public ResponseEntity<CartResponse> removingCart(@RequestHeader String token, @RequestParam Long cartId) {
        CartResponse cartResponse = cartService.removingCart(token,cartId);
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }

}
