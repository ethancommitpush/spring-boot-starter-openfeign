package com.github.ethancommitpush.feign.example;

public class TargetClassWrong implements TargetInterface {

    public TargetClassWrong() {
        throw new RuntimeException("mock error");
    }
}