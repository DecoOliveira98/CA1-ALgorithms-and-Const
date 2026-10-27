package com.cct.foodstorage;

import java.util.ArrayList;
import java.util.List;

/**
 * The storage unit: a FIFO QUEUE implemented with a CIRCULAR ARRAY (capacity 8).
 *
 * Scenario: trays enter through the FRONT side of the unit and leave through the
 * OPPOSITE side, so the first tray placed is always the first one removed
 * (First In, First Out). This follows the food-safety FIFO rule: the oldest food,
 * closest to its best-before date, always leaves first.
 *
 * How the circular array works (capacity 8):
 *
 *   index:   0     1     2     3     4     5     6     7
 *          [   ] [ A ] [ B ] [ C ] [   ] [   ] [   ] [   ]
 *                  ^                 ^
 *                 head              tail
 *        (next to leave)     (next free slot to enter)
 *
 *  - enqueue writes at "tail" and moves tail one step forward.
 *  - dequeue reads at "head" and moves head one step forward.
 *  - When an index reaches the end of the array it wraps around to 0
 *    using the modulo operator:  next = (index + 1) % capacity
 *
 * Because only the indexes move (the elements are never shifted),
 * adding and removing are O(1).
 */
public class FoodStorage {

    /** Maximum number of trays the unit can hold (given by the scenario). */
    public static final int DEFAULT_CAPACITY = 8;

    private final FoodItem[] trays; // the circular array that holds the trays
    private final int capacity;     // size of the array (8)
    private int head;               // index of the next tray to leave (opposite side)
    private int tail;               // index where the next tray enters (front side)
    private int size;               // how many trays are stored right now

    /** Creates an empty storage unit with the default capacity of 8 trays. */
    public FoodStorage() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Creates an empty storage unit with a custom capacity
     * (useful for testing with smaller sizes).
     */
    public FoodStorage(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero.");
        }
        this.capacity = capacity;
        this.trays = new FoodItem[capacity];
        this.head = 0;  // nothing stored yet: head and tail start at the same place
        this.tail = 0;
        this.size = 0;
    }

    // ------------------------------------------------------------------ state

    /** True when there is no data in the storage. O(1) */
    public boolean isEmpty() {
        return size == 0;
    }

    /** True when all 8 trays are used. O(1) */
    public boolean isFull() {
        return size == capacity;
    }

    /** Number of trays currently stored. O(1) */
    public int size() {
        return size;
    }

    /** Maximum number of trays. O(1) */
    public int getCapacity() {
        return capacity;
    }

    // ------------------------------------------------------------------ enqueue

    /**
     * ENQUEUE: places a new tray through the front side of the unit.
     * Time complexity: O(1) – one write and one index update, no shifting.
     *
     * @throws StorageFullException if the unit already holds 8 trays (overflow)
     */
    public void enqueue(FoodItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot store an empty tray.");
        }
        // 1. Overflow check: the storage cannot hold more than its capacity.
        if (isFull()) {
            throw new StorageFullException("Storage is full (" + capacity
                    + " trays). Remove an item before adding a new one.");
        }
        // 2. Put the tray in the free slot pointed by tail.
        trays[tail] = item;
        // 3. Move tail forward; if it passes the last index it wraps to 0.
        tail = (tail + 1) % capacity;
        // 4. One more tray stored.
        size++;
    }

    // ------------------------------------------------------------------ dequeue

    /**
     * DEQUEUE: removes the tray that has been in the storage the longest
     * (the first one placed), through the opposite side of the unit.
     * Time complexity: O(1).
     *
     * @return the removed tray
     * @throws StorageEmptyException if there is no data in the storage (underflow)
     */
    public FoodItem dequeue() {
        // 1. Underflow check: nothing to remove.
        if (isEmpty()) {
            throw new StorageEmptyException("Storage is empty - there is no food item to remove.");
        }
        // 2. Take the oldest tray, at the head position.
        FoodItem removed = trays[head];
        // 3. Clear the slot so the old object can be garbage-collected.
        trays[head] = null;
        // 4. Move head forward (wrapping around to 0 if needed).
        head = (head + 1) % capacity;
        // 5. One tray less.
        size--;
        return removed;
    }

    // ------------------------------------------------------------------ peek

    /**
     * PEEK: shows the tray at the top of the queue (the next one to be removed)
     * WITHOUT removing it. Time complexity: O(1).
     *
     * @throws StorageEmptyException if there is no data in the storage
     */
    public FoodItem peek() {
        if (isEmpty()) {
            throw new StorageEmptyException("Storage is empty - there is no food item at the top.");
        }
        return trays[head];
    }

    // ------------------------------------------------------------------ display

    /**
     * DISPLAY: returns all trays in FIFO order, from the oldest (next to leave)
     * to the newest (last placed). Time complexity: O(n) – visits every tray once.
     *
     * The i-th tray in the queue is at index (head + i) % capacity,
     * which handles the case where the trays wrap around the end of the array.
     */
    public List<FoodItem> getAll() {
        List<FoodItem> items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(trays[(head + i) % capacity]);
        }
        return items;
    }

    // ------------------------------------------------------------------ search

    /**
     * SEARCH: linear search for every tray of the given food type.
     * Returns the positions found in the queue (1 = next to leave).
     * An empty list means the food is not in the storage.
     * Time complexity: O(n) – the trays are not sorted, so each one must be checked.
     */
    public List<Integer> search(FoodType type) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            FoodItem item = trays[(head + i) % capacity];
            if (item.getType() == type) {
                positions.add(i + 1); // positions shown to the user start at 1
            }
        }
        return positions;
    }
}
