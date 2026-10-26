package com.cct.foodstorage;

/**
 * The storage unit: a deque implemented with a circular array (capacity 8).
 */
public class FoodStorage {

    public static final int DEFAULT_CAPACITY = 8;

    private FoodItem[] trays;
    private int front;
    private int size;

    // TODO: addFront, removeFront, removeRear, peek, display, search
}