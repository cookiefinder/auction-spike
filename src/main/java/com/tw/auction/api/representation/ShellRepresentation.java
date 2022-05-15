package com.tw.auction.api.representation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShellRepresentation<T> {
    private T data;

    public static <T> ShellRepresentation<T> of(T data) {
        return new ShellRepresentation<>(data);
    }
}
