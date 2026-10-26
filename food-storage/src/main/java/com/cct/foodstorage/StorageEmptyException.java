package com.cct.foodstorage;

public class StorageEmptyException extends RuntimeException {
    public StorageEmptyException(String message) {
        super(message);
    }
}