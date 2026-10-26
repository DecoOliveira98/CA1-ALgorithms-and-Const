package com.cct.foodstorage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * One tray of food placed in the storage.
 * Encapsulation: now all field are private and final. An item cannot be
 * changed after it has been created and validated.
 */

public class FoodItem {
    
    public static final int MIN_WEIGHT = 1;      // In grams
    public static final int MAX_WEIGHT = 5000;   // In grams. 5kg per tray
    public static final int MAX_SHELF_DAYS = 14; // Best before: maximum 2 weeks
    
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private final FoodType type;
    private final int weightGrams;
    private final LocalDate bestBefore;
    private final LocalDateTime timePlaced;    // records automatically
    
    public FoodItem(FoodType type, int weightGrams, LocalDate bestBefore) {
        validate(type, weightGrams, bestBefore);
        this.type = type;
        this.weightGrams = weightGrams;
        this.bestBefore = bestBefore;
        this.timePlaced = LocalDateTime.now();
    }

    /** Validation rules. Throws IllegalArgumentException with clear message. */
    private static void validate(FoodType type, int weightGrams, LocalDate bestBefore) {
        if (type == null){
            throw new IllegalArgumentException("Food type is required.");
        }
        if (weightGrams < MIN_WEIGHT || weightGrams > MAX_WEIGHT) {
            throw new IllegalArgumentException("Weight must be between " + MIN_WEIGHT 
                    + " and " + MAX_WEIGHT + " grams.");
        }
        if (bestBefore == null) {
            throw new IllegalArgumentException("Best-before date is required."); 
        }
        LocalDate today = LocalDate.now();
        if (bestBefore.isBefore(today)) {
            throw new IllegalArgumentException("Best-before date cannot be in the past.");
        }
        if (bestBefore.isAfter(today.plusDays(MAX_SHELF_DAYS))) {
            throw new IllegalArgumentException("Best-before date must be at most " + MAX_SHELF_DAYS 
                     + " days from today (latest: " + today.plusDays(MAX_SHELF_DAYS).format(DATE_FORMAT) + ").");
        }
    }
    
    public FoodType getType()             {return type; }
    public String getName()               {return type.getDisplayName(); }
    public int getWeightGrams()           {return weightGrams; }
    public LocalDate getBestBefore()      {return bestBefore; }
    public LocalDateTime getTimePlaced()  {return timePlaced; }
    
    
    @Override
    public String toString() {
        return String.format("%-8s | %5d g | best before %s | placed %s",
                getName(), weightGrams, bestBefore.format(DATE_FORMAT), timePlaced.format(TIME_FORMAT));
    }

}
