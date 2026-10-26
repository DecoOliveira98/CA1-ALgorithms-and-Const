package com.cct.foodstorage;

/**
 * The storage unit: a FIFO queue implemented with a circular array (capacity 8).
 * Trays enter through the front side of the unit and leave through the opposite side,
 * so the first tray placed is always the first one removed.
 */
public class FoodStorage {

    public static final int DEFAULT_CAPACITY = 8;

    private FoodItem[] trays;
    private int head;   // index of the next tray to leave (opposite side)
    private int tail;   // index where the next tray enters (front side)
    private int size;   // number of trays currently stored

    // TODO: enqueue, dequeue, peek, isEmpty, isFull, display, search
}