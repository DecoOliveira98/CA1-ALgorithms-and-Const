package com.cct.foodstorage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FoodItem (validation of weight and best-before date)
 * and FoodType (conversion of the user's input into a food).
 * Boundary values (1 g, 5000 g, today, today + 14 days) are tested on purpose:
 * errors usually happen exactly at the limits.
 */
class FoodItemTest {

    private final LocalDate today = LocalDate.now();

    // =====================================================================
    //  FoodItem - valid data
    // =====================================================================

    @Test
    @DisplayName("A valid tray is created and the time placed is recorded")
    void validItem() {
        FoodItem item = new FoodItem(FoodType.PIZZA, 450, today.plusDays(5));
        assertEquals("Pizza", item.getName());
        assertEquals(450, item.getWeightGrams());
        assertEquals(today.plusDays(5), item.getBestBefore());
        assertNotNull(item.getTimePlaced());
    }

    @Test
    @DisplayName("Boundary values are accepted: 1 g, 5000 g, today and today + 14 days")
    void boundaryValuesAccepted() {
        assertDoesNotThrow(() -> new FoodItem(FoodType.HOTDOG, 1, today));
        assertDoesNotThrow(() -> new FoodItem(FoodType.HOTDOG, 5000, today.plusDays(14)));
    }

    // =====================================================================
    //  FoodItem - invalid data
    // =====================================================================

    @Test
    @DisplayName("Weight of 0, negative or above 5000 g is rejected")
    void invalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(FoodType.FRIES, 0, today.plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(FoodType.FRIES, -50, today.plusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(FoodType.FRIES, 5001, today.plusDays(1)));
    }

    @Test
    @DisplayName("Best-before in the past, after 14 days or missing is rejected")
    void invalidBestBefore() {
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(FoodType.BURGER, 200, today.minusDays(1)));
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(FoodType.BURGER, 200, today.plusDays(15)));
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(FoodType.BURGER, 200, null));
    }

    @Test
    @DisplayName("A missing food type is rejected")
    void missingType() {
        assertThrows(IllegalArgumentException.class, () -> new FoodItem(null, 200, today.plusDays(1)));
    }

    // =====================================================================
    //  FoodType - parsing the user's input
    // =====================================================================

    @Test
    @DisplayName("FoodType accepts the menu number or the name (any case, with spaces)")
    void foodTypeValidInput() {
        assertEquals(FoodType.BURGER, FoodType.fromInput("1"));
        assertEquals(FoodType.HOTDOG, FoodType.fromInput("5"));
        assertEquals(FoodType.PIZZA, FoodType.fromInput("pizza"));
        assertEquals(FoodType.HOTDOG, FoodType.fromInput("hot dog"));
        assertEquals(FoodType.SANDWICH, FoodType.fromInput("  SANDWICH "));
    }

    @Test
    @DisplayName("FoodType rejects unknown food, wrong numbers and empty input")
    void foodTypeInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> FoodType.fromInput("Cake"));
        assertThrows(IllegalArgumentException.class, () -> FoodType.fromInput("0"));
        assertThrows(IllegalArgumentException.class, () -> FoodType.fromInput("6"));
        assertThrows(IllegalArgumentException.class, () -> FoodType.fromInput(""));
        assertThrows(IllegalArgumentException.class, () -> FoodType.fromInput(null));
    }
}
