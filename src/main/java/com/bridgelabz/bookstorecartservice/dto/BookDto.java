package com.bridgelabz.bookstorecartservice.dto;

import lombok.Data;

import javax.persistence.Lob;
@Data
public class BookDto {
    private long bookId;
    private String bookName;
    private String author;
    private String description;
    @Lob
    private byte[] logo;
    private int price;
    private int quantity;
}
