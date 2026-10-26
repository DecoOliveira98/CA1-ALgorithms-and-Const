package com.cct.foodstorage;

public class StorageFullException extends RuntimeException {
    public StorageFullException(String message) {
        super(message);
    }
}