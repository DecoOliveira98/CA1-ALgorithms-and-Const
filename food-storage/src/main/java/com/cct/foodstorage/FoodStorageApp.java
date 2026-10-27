package com.cct.foodstorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

/**
 * Main class: console menu for the fast-food party storage.
 *
 * This class only talks to the user (reads input and prints results).
 * The data rules live in FoodItem/FoodType and the FIFO queue lives in FoodStorage,
 * so each class has a single responsibility.
 *
 * Every input is validated: the program asks again instead of crashing,
 * and the storage exceptions (full / empty) are caught and shown as messages.
 */
public class FoodStorageApp {

    // STRICT resolver rejects impossible dates such as 31/02/2026.
    private static final DateTimeFormatter INPUT_DATE =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final FoodStorage storage = new FoodStorage(); // the FIFO queue (8 trays)
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new FoodStorageApp().run();
    }

    /** Shows the menu in a loop until the user chooses Exit. */
    private void run() {
        System.out.println("==================================================");
        System.out.println("   FAST-FOOD PARTY STORAGE  (FIFO - " + storage.getCapacity() + " trays)");
        System.out.println("==================================================");

        while (true) {
            printMenu();
            int choice = readIntInRange("Enter your choice (1-7): ", 1, 7);

            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> showTop();
                case 4 -> displayAll();
                case 5 -> searchItem();
                case 6 -> showStatus();
                case 7 -> {
                    System.out.println("Exiting application. Goodbye!");
                    scanner.close();
                    return; // terminates the application
                }
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("------------------- MENU -------------------");
        System.out.println("1. Add food item (enqueue - front side)");
        System.out.println("2. Remove food item (dequeue - opposite side, oldest first)");
        System.out.println("3. Show item at the top (peek)");
        System.out.println("4. Display all items");
        System.out.println("5. Search for a food item");
        System.out.println("6. Storage status");
        System.out.println("7. Exit");
    }

    // =================================================================== operations

    /** Option 1: reads a new tray from the user and enqueues it. */
    private void addItem() {
        // Check first, so the user does not type everything just to be told it is full.
        if (storage.isFull()) {
            System.out.println("ERROR: Storage is full (" + storage.getCapacity()
                    + " trays). Remove an item before adding a new one.");
            return;
        }

        FoodType type = readFoodType();
        int weight = readIntInRange("Weight in grams (" + FoodItem.MIN_WEIGHT + "-"
                + FoodItem.MAX_WEIGHT + "): ", FoodItem.MIN_WEIGHT, FoodItem.MAX_WEIGHT);
        LocalDate bestBefore = readBestBefore();

        try {
            // FoodItem validates again and records the time placed automatically.
            FoodItem item = new FoodItem(type, weight, bestBefore);
            storage.enqueue(item);
            System.out.println("Added: " + item);
            System.out.println("Trays used: " + storage.size() + "/" + storage.getCapacity());
        } catch (IllegalArgumentException | StorageFullException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /** Option 2: removes the oldest tray (FIFO). */
    private void removeItem() {
        try {
            FoodItem removed = storage.dequeue();
            System.out.println("Removed (first in, first out): " + removed);
            System.out.println("Trays used: " + storage.size() + "/" + storage.getCapacity());
        } catch (StorageEmptyException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /** Option 3: shows the name of the food at the top of the queue (next to leave). */
    private void showTop() {
        try {
            FoodItem top = storage.peek();
            System.out.println("Item at the top (next to be removed): " + top.getName());
            System.out.println("   details: " + top);
        } catch (StorageEmptyException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    /** Option 4: lists every tray from the oldest to the newest. */
    private void displayAll() {
        if (storage.isEmpty()) {
            System.out.println("Storage is empty - no data to display.");
            return;
        }
        List<FoodItem> items = storage.getAll();
        System.out.println("Items in FIFO order (1 = next to leave):");
        for (int i = 0; i < items.size(); i++) {
            String tag = "";
            if (i == 0) tag = "  <- top (next out)";
            else if (i == items.size() - 1) tag = "  <- last placed";
            System.out.printf("  %d. %s%s%n", i + 1, items.get(i), tag);
        }
        System.out.println("Trays used: " + storage.size() + "/" + storage.getCapacity());
    }

    /** Option 5: linear search by food type. */
    private void searchItem() {
        if (storage.isEmpty()) {
            System.out.println("Storage is empty - nothing to search.");
            return;
        }
        FoodType type = readFoodType();
        List<Integer> positions = storage.search(type);
        if (positions.isEmpty()) {
            System.out.println(type + " is not in the storage.");
        } else {
            System.out.println(type + " found at position(s) " + positions + " (1 = next to leave).");
        }
    }

    /** Option 6: size, empty and full. */
    private void showStatus() {
        System.out.println("Trays used: " + storage.size() + "/" + storage.getCapacity()
                + " | empty: " + storage.isEmpty()
                + " | full: " + storage.isFull());
    }

    // =================================================================== input validation

    /** Asks for the food until a valid one is typed (number 1-5 or name). */
    private FoodType readFoodType() {
        while (true) {
            System.out.print("Food (1=Burger, 2=Pizza, 3=Fries, 4=Sandwich, 5=Hotdog): ");
            String input = readLine();
            try {
                return FoodType.fromInput(input); // validation lives in FoodType
            } catch (IllegalArgumentException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    /** Asks for a date in dd/MM/yyyy between today and today + 14 days. */
    private LocalDate readBestBefore() {
        LocalDate today = LocalDate.now();
        LocalDate latest = today.plusDays(FoodItem.MAX_SHELF_DAYS);
        while (true) {
            System.out.print("Best-before date (dd/MM/yyyy, " + today.format(FoodItem.DATE_FORMAT)
                    + " to " + latest.format(FoodItem.DATE_FORMAT) + "): ");
            String input = readLine().trim();
            try {
                LocalDate date = LocalDate.parse(input, INPUT_DATE);
                if (date.isBefore(today)) {
                    System.out.println("ERROR: Best-before date cannot be in the past.");
                } else if (date.isAfter(latest)) {
                    System.out.println("ERROR: Best-before date must be at most "
                            + FoodItem.MAX_SHELF_DAYS + " days from today.");
                } else {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: '" + input + "' is not a valid date. Use dd/MM/yyyy.");
            }
        }
    }

    /** Asks until a whole number between min and max is typed. */
    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = readLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("ERROR: Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("ERROR: '" + input + "' is not a whole number.");
            }
        }
    }

    /** Reads one line; if the input ends unexpectedly (Ctrl+D / Ctrl+Z), exits cleanly. */
    private String readLine() {
        if (!scanner.hasNextLine()) {
            System.out.println("\nNo more input. Exiting application.");
            System.exit(0);
        }
        return scanner.nextLine();
    }
}
