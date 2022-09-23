package com.bridgelabz.bookstorecartservice.service;

import com.bridgelabz.bookstorecartservice.dto.CartDto;
import com.bridgelabz.bookstorecartservice.exception.CartNotFoundException;
import com.bridgelabz.bookstorecartservice.model.CartModel;
import com.bridgelabz.bookstorecartservice.repository.CartRepository;
import com.bridgelabz.bookstorecartservice.util.BookResponse;
import com.bridgelabz.bookstorecartservice.util.CartResponse;
import com.bridgelabz.bookstorecartservice.util.TokenUtil;
import com.bridgelabz.bookstorecartservice.util.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TokenUtil tokenUtil;


    @Override
    public CartResponse addCart(CartDto cartDto, String token, Long bookId) {
        Long userId = tokenUtil.decodeToken(token);
        UserResponse isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8082/user/validate/" + userId, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            BookResponse isBookPresent = restTemplate.getForObject("http://BOOK-SERVICE:8089/book/validatebookId/" + bookId, BookResponse.class);
            if (isBookPresent.getErrorCode() == 200) {
                CartModel cartModel = new CartModel(cartDto);
                cartModel.setUserId(userId);
                cartModel.setBookId(bookId);
                if (isBookPresent.getObject().getQuantity() >= cartDto.getQuantity()) {
                    cartModel.setQuantity(cartDto.getQuantity());
                    cartModel.setTotalPrice((cartDto.getQuantity()) * (isBookPresent.getObject().getPrice()));
                    cartRepository.save(cartModel);
                    BookResponse isBookIdPresent = restTemplate.getForObject("http://BOOK-SERVICE:8089/book/updatebookquantity/" +
                            bookId +"/"+ cartDto.getQuantity(), BookResponse.class);
                    return new CartResponse(200, "Success", cartModel);
                }
                throw new CartNotFoundException(400, "Not found");
            }

        }
        throw new CartNotFoundException(400, "Token is wrong");
    }
    @Override
    public CartResponse updateQuantity(Long cartId, String token, Integer quantity) {
        Long userId = tokenUtil.decodeToken(token);
        UserResponse isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8082/user/validate/" + userId, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
            if (isCartPresent.isPresent()) {
                BookResponse isBookPresent = restTemplate.getForObject("http://BOOK-SERVICE:8089/book/validatebookId/" +
                        isCartPresent.get().getBookId(), BookResponse.class);
                if (isCartPresent.get().getUserId() == userId) {
                    if (isCartPresent.get().getQuantity() > quantity) {
                        int bookQuantity = isCartPresent.get().getQuantity() - quantity;
                        isCartPresent.get().setQuantity(quantity);
                        isCartPresent.get().setTotalPrice(quantity * isBookPresent.getObject().getPrice());
                        cartRepository.save(isCartPresent.get());
                        BookResponse isBookIdPresent = restTemplate.getForObject("http://BOOK-SERVICE:8089/book/updatequantity/" +
                                isCartPresent.get().getBookId() + "/" + bookQuantity, BookResponse.class);
                        return new CartResponse(200, "success", isCartPresent.get());
                    } else {
                        int bookQuantity = quantity - isCartPresent.get().getQuantity();
                        isCartPresent.get().setQuantity(quantity);
                        isCartPresent.get().setTotalPrice(quantity * isBookPresent.getObject().getPrice());
                        cartRepository.save(isCartPresent.get());
                        BookResponse isBookIdPresent = restTemplate.getForObject("http://BOOK-SERVICE:8089/bookdetails/updatebookquantity/" +
                                isCartPresent.get().getBookId() + "/" + bookQuantity, BookResponse.class);
                        return new CartResponse(200, "success", isCartPresent.get());
                    }
                }
            }

        }
        throw new CartNotFoundException(400, "Not found");
    }

    @Override
    public List<CartModel> getAllCartItems(String token) {
        Long userId = tokenUtil.decodeToken(token);
        UserResponse isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8082/user/validate/" + userId, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            List<CartModel> isCartPresent = cartRepository.findAll();
            if (isCartPresent.size()>0) {
                return isCartPresent;
            }
            throw new CartNotFoundException(500, "Cart item list is empty");
        }
        throw new CartNotFoundException(500, "User is Not Available");
    }


    @Override
    public CartResponse removingCart(String token, Long cartId) {
        Long userId = tokenUtil.decodeToken(token);
        UserResponse isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8082/user/validate/" +
                userId, UserResponse.class);
        if (isUserPresent.getErrorCode() == 200) {
            Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
            if (isCartPresent.isPresent()) {
                if (isCartPresent.get().getUserId() == userId) {
                    cartRepository.delete(isCartPresent.get());
                    BookResponse isBookIdPresent = restTemplate.getForObject("http://BOOK-SERVICE:8089/book/updatequantity/" +
                            isCartPresent.get().getBookId() + "/" + isCartPresent.get().getQuantity(), BookResponse.class);
                    return new CartResponse(200, "Success", isCartPresent.get());
                }
                throw new CartNotFoundException(400, "Invalid User");
            }
        }
        throw new CartNotFoundException(400, "Token is wrong");
    }
}


