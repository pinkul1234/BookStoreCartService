package com.bridgelabz.bookstorecartservice.util;


import com.bridgelabz.bookstorecartservice.dto.BookDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
	private int errorCode;
	private String message;
	private BookDto object;
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BookDto getObject() {
		return object;
	}
	public void setObject(BookDto object) {
		this.object = object;
	}
}
