package com.cct.foodstorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FoodStorage (FIFO queue on a circular array).
 *
 * Each test follows the pattern Arrange - Act - Assert:
 *   1. Arrange: create the storage and the trays needed
 *   2. Act:     call the operation being tested
 *   3. Assert:  check the result is what we expect
 *
 * @BeforeEach runs before EVERY test, so each test starts with a new,
 * empty storage and one test can never affect another.
 */
class FoodStorageTest {

    private FoodStorage storage;

    // a valid best-before date (5 days from today) reused by all tests
    private final LocalDate validDate = LocalDate.now().plusDays(5);

    @BeforeEach
    void setUp() {
        storage = new FoodStorage();
    }

    /** Helper: creates a valid tray of the given type. */
    private FoodItem tray(FoodType type) {
        return new FoodItem(type, 300, validDate);
    }

    // =====================================================================
    //  ERROR HANDLING - no data in the system (required by the brief)
    // =====================================================================

    @Test
    @DisplayName("A new storage is empty")
    void newStorageIsEmpty() {
        assertTrue(storage.isEmpty());
        assertFalse(storage.isFull());
        assertEquals(0, storage.size());
        assertTrue(storage.getAll().isEmpty());
    }

    @Test
    @DisplayName("dequeue on an empty storage throws StorageEmptyException (underflow)")
    void dequeueEmptyThrows() {
        assertThrows(StorageEmptyException.class, () -> storage.dequeue());
    }

    @Test
    @DisplayName("peek on an empty storage throws StorageEmptyException")
    void peekEmptyThrows() {
        assertThrows(StorageEmptyException.class, () -> storage.peek());
    }

    @Test
    @DisplayName("search on an empty storage returns an empty list (no crash)")
    void searchEmptyReturnsEmptyList() {
        assertTrue(storage.search(FoodType.PIZZA).isEmpty());
    }

    // =====================================================================
    //  CAPACITY - maximum of 8 trays
    // =====================================================================

    @Test
    @DisplayName("Default capacity is 8")
    void capacityIsEight() {
        assertEquals(8, storage.getCapacity());
    }

    @Test
    @DisplayName("Storage accepts 8 trays and rejects the 9th (overflow)")
    void ninthTrayIsRejected() {
        for (int i = 0; i < 8; i++) {
            storage.enqueue(tray(FoodType.BURGER));
        }
        assertTrue(storage.isFull());
        assertThrows(StorageFullException.class, () -> storage.enqueue(tray(FoodType.PIZZA)));
        assertEquals(8, storage.size());   // the failed add did not change anything
    }

    @Test
    @DisplayName("A null tray is rejected")
    void nullTrayIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> storage.enqueue(null));
    }

    @Test
    @DisplayName("Capacity of zero or less is rejected")
    void invalidCapacityIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FoodStorage(0));
        assertThrows(IllegalArgumentException.class, () -> new FoodStorage(-3));
    }

    // =====================================================================
    //  FIFO BEHAVIOUR - first in, first out
    // =====================================================================

    @Test
    @DisplayName("dequeue removes trays in the same order they were added (FIFO)")
    void dequeueIsFifo() {
        storage.enqueue(tray(FoodType.BURGER));
        storage.enqueue(tray(FoodType.PIZZA));
        storage.enqueue(tray(FoodType.FRIES));

        assertEquals(FoodType.BURGER, storage.dequeue().getType());
        assertEquals(FoodType.PIZZA, storage.dequeue().getType());
        assertEquals(FoodType.FRIES, storage.dequeue().getType());
        assertTrue(storage.isEmpty());
    }

    @Test
    @DisplayName("peek shows the oldest tray without removing it")
    void peekDoesNotRemove() {
        storage.enqueue(tray(FoodType.HOTDOG));
        storage.enqueue(tray(FoodType.SANDWICH));

        assertEquals(FoodType.HOTDOG, storage.peek().getType());
        assertEquals("Hotdog", storage.peek().getName());   // the NAME shown at the top
        assertEquals(2, storage.size());                     // nothing was removed
    }

    @Test
    @DisplayName("A tray can be added again after the storage was full and one was removed")
    void addAfterRemoveWhenFull() {
        for (int i = 0; i < 8; i++) {
            storage.enqueue(tray(FoodType.FRIES));
        }
        storage.dequeue();
        assertDoesNotThrow(() -> storage.enqueue(tray(FoodType.PIZZA)));
        assertTrue(storage.isFull());
    }

    @Test
    @DisplayName("Circular array: indexes wrap around and FIFO order is kept")
    void circularWrapAround() {
        // fill 6 trays, then add one / remove one many times:
        // head and tail go round the array several times
        FoodType[] types = FoodType.values();
        for (int i = 0; i < 6; i++) {
            storage.enqueue(tray(types[i % types.length]));
        }
        for (int i = 6; i < 50; i++) {
            storage.enqueue(tray(types[i % types.length]));
            FoodType expectedOldest = types[(i - 6) % types.length];
            assertEquals(expectedOldest, storage.dequeue().getType());
        }
        assertEquals(6, storage.size());
    }

    // =====================================================================
    //  DISPLAY AND SEARCH
    // =====================================================================

    @Test
    @DisplayName("getAll lists trays from oldest (next to leave) to newest")
    void getAllOrder() {
        storage.enqueue(tray(FoodType.BURGER));
        storage.enqueue(tray(FoodType.PIZZA));
        storage.enqueue(tray(FoodType.FRIES));

        List<FoodItem> all = storage.getAll();
        assertEquals(3, all.size());
        assertEquals(FoodType.BURGER, all.get(0).getType());
        assertEquals(FoodType.FRIES, all.get(2).getType());
    }

    @Test
    @DisplayName("search returns every position of a food type (1 = next to leave)")
    void searchFindsAllPositions() {
        storage.enqueue(tray(FoodType.PIZZA));     // position 1
        storage.enqueue(tray(FoodType.FRIES));     // position 2
        storage.enqueue(tray(FoodType.PIZZA));     // position 3

        assertEquals(List.of(1, 3), storage.search(FoodType.PIZZA));
        assertTrue(storage.search(FoodType.SANDWICH).isEmpty());   // not found
    }
}
