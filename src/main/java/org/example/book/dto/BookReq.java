package org.example.book.dto;

public record BookReq(
    String title,
    String author,
    Integer price
) {
}
