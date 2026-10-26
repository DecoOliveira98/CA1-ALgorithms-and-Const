package com.cct.foodstorage;


/**
 * the five types of food the restaurant can store.
 *Using an enum guarantees that only the 5 values can exist.
 */
public enum FoodType {
    BURGER("Burger"),
    PIZZA("Pizza"),
    FRIES("Fries"),
    SANDWICH("Sandwich"),
    HOTDOG("Hotdog");
    
    private final String displayName;
    
    FoodType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Converts the user input into FoodType.
     * Accepts the menu number (1-5) or the name of the food (case-insensitive, e.g. "pizza").
     * 
     * @throws IllegalArgumentException if the input does not match any food type.
     */
    public static FoodType fromInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Food type cannot be empty.");
        }
        String clean = input.trim().replace(" ", "").replace("-", "");
        
        //1- option number from the menu
        if (clean.matches("\\d+")) {
            int number = Integer.parseInt(clean);
            if (number >= 1 && number <= values().length) {
                return values()[number - 1];
            }
            throw new IllegalArgumentException("Option must be between 1 and " + values().length + ".");
        }
        
        //2- food name
        for (FoodType type : values()) {
            if (type.displayName.equalsIgnoreCase(clean)) {
                return type;
            }
        }
        throw new IllegalArgumentException("'" + input.trim()
                + "' is not a valid food. Choose Burger, Pizza, Fries, Sandwich or Hotdog.");
    }
    @Override
    public String toString() {
        return displayName;
    }
}
