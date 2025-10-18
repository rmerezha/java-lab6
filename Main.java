import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Bouquet bouquet = new Bouquet();

        bouquet.addFlower(new Rose(6.0, 40, 9, "Red"));
        bouquet.addFlower(new Tulip(3.5, 35, 8, "Yellow"));
        bouquet.addFlower(new Lily(5.5, 45, 10, "White"));
        bouquet.addFlower(new Rose(5.8, 38, 7, "Pink"));

        bouquet.addAccessory(new Accessory("Wrapping paper", 1.5));
        bouquet.addAccessory(new Accessory("Ribbon", 0.5));

        System.out.println("=== INITIAL BOUQUET ===");
        bouquet.printBouquet();

        System.out.println("\n=== SORTING BY FRESHNESS ===");
        bouquet.sortByFreshness();
        bouquet.printBouquet();

        System.out.println("\n=== FINDING FLOWERS WITH STEM LENGTH BETWEEN 37 AND 42 CM ===");
        List<Flower<?>> found = bouquet.findByStemLength(37, 42);
        if (found.isEmpty()) {
            System.out.println("No flowers found with the specified stem length range.");
        } else {
            found.forEach(f -> System.out.println("  - " + f));
        }
    }
}

/**
 * The abstract generic base class that represents a flower.
 *
 * Each flower has the following properties:
 * - Name (species name, for example "Rose")
 * - Price (in currency units)
 * - Stem length (in centimeters)
 * - Freshness level (0 = wilted, 10 = freshly cut)
 * - Additional information (generic type T, for example color or variety)
 *
 * @param <T> the type representing additional information about the flower
 */
abstract class Flower<T> {
    private final String name;
    private final double price;
    private final int stemLength;
    private final int freshnessLevel;
    private final T extraInfo;

    /**
     * Constructs a new Flower instance.
     *
     * @param name the name of the flower
     * @param price the price of a single flower
     * @param stemLength the stem length in centimeters
     * @param freshnessLevel the freshness level (0–10)
     * @param extraInfo additional information (for example, color)
     * @throws IllegalArgumentException if price is negative, stem length is non-positive,
     *                                  or freshness level is out of range
     */
    public Flower(String name, double price, int stemLength, int freshnessLevel, T extraInfo) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (stemLength <= 0) {
            throw new IllegalArgumentException("Stem length must be positive.");
        }
        if (freshnessLevel < 0 || freshnessLevel > 10) {
            throw new IllegalArgumentException("Freshness level must be between 0 and 10.");
        }

        this.name = name;
        this.price = price;
        this.stemLength = stemLength;
        this.freshnessLevel = freshnessLevel;
        this.extraInfo = extraInfo;
    }

    /** Returns the flower name. */
    public String getName() {
        return name;
    }

    /** Returns the price of the flower. */
    public double getPrice() {
        return price;
    }

    /** Returns the stem length in centimeters. */
    public int getStemLength() {
        return stemLength;
    }

    /** Returns the freshness level (0–10). */
    public int getFreshnessLevel() {
        return freshnessLevel;
    }

    /** Returns additional information about the flower (for example, color). */
    public T getExtraInfo() {
        return extraInfo;
    }

    @Override
    public String toString() {
        return String.format("%s [%.2f USD, %d cm, freshness=%d, info=%s]",
                name, price, stemLength, freshnessLevel, extraInfo);
    }
}

/**
 * Represents a rose flower.
 */
class Rose extends Flower<String> {
    /**
     * Creates a new Rose.
     *
     * @param price price of the rose
     * @param stemLength stem length in centimeters
     * @param freshnessLevel freshness level (0–10)
     * @param color color of the rose
     */
    public Rose(double price, int stemLength, int freshnessLevel, String color) {
        super("Rose", price, stemLength, freshnessLevel, color);
    }
}

/**
 * Represents a tulip flower.
 */
class Tulip extends Flower<String> {
    /**
     * Creates a new Tulip.
     *
     * @param price price of the tulip
     * @param stemLength stem length in centimeters
     * @param freshnessLevel freshness level (0–10)
     * @param color color of the tulip
     */
    public Tulip(double price, int stemLength, int freshnessLevel, String color) {
        super("Tulip", price, stemLength, freshnessLevel, color);
    }
}

/**
 * Represents a lily flower.
 */
class Lily extends Flower<String> {
    /**
     * Creates a new Lily.
     *
     * @param price price of the lily
     * @param stemLength stem length in centimeters
     * @param freshnessLevel freshness level (0–10)
     * @param color color of the lily
     */
    public Lily(double price, int stemLength, int freshnessLevel, String color) {
        super("Lily", price, stemLength, freshnessLevel, color);
    }
}

/**
 * Represents an accessory for a bouquet, for example wrapping paper or ribbon.
 */
class Accessory {
    private final String name;
    private final double price;

    /**
     * Creates a new Accessory.
     *
     * @param name the name of the accessory
     * @param price the price of the accessory
     * @throws IllegalArgumentException if price is negative
     */
    public Accessory(String name, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Accessory price cannot be negative.");
        }
        this.name = name;
        this.price = price;
    }

    /** Returns the price of the accessory. */
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f USD)", name, price);
    }
}

/**
 * Represents a bouquet containing flowers and accessories.
 *
 * Provides methods to:
 * - Add flowers and accessories
 * - Calculate total price
 * - Sort flowers by freshness
 * - Find flowers by stem length range
 */
class Bouquet {
    private final List<Flower<?>> flowers = new ArrayList<>();
    private final List<Accessory> accessories = new ArrayList<>();

    /**
     * Adds a flower to the bouquet.
     *
     * @param flower the flower to add
     * @throws NullPointerException if the flower is null
     */
    public void addFlower(Flower<?> flower) {
        if (flower == null) {
            throw new NullPointerException("Cannot add a null flower to the bouquet.");
        }
        flowers.add(flower);
    }

    /**
     * Adds an accessory to the bouquet.
     *
     * @param accessory the accessory to add
     * @throws NullPointerException if the accessory is null
     */
    public void addAccessory(Accessory accessory) {
        if (accessory == null) {
            throw new NullPointerException("Cannot add a null accessory to the bouquet.");
        }
        accessories.add(accessory);
    }

    /**
     * Calculates the total price of the bouquet including all flowers and accessories.
     *
     * @return the total price in USD
     */
    public double getTotalPrice() {
        double sum = accessories.stream()
                .mapToDouble(Accessory::getPrice)
                .sum();
        sum += flowers.stream()
                .mapToDouble(Flower::getPrice)
                .sum();
        return sum;
    }

    /**
     * Sorts the flowers in the bouquet by their freshness level in descending order
     * (from freshest to least fresh).
     */
    public void sortByFreshness() {
        Collections.sort(flowers, Comparator.comparingInt((Flower<?> f) -> f.getFreshnessLevel()).reversed());
    }

    /**
     * Finds all flowers with stem length in a given range.
     *
     * @param min minimum stem length (inclusive)
     * @param max maximum stem length (inclusive)
     * @return a list of flowers matching the given range
     * @throws IllegalArgumentException if min > max
     */
    public List<Flower<?>> findByStemLength(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum length cannot exceed maximum length.");
        }

        List<Flower<?>> result = new ArrayList<>();
        for (Flower<?> flower : flowers) {
            int len = flower.getStemLength();
            if (len >= min && len <= max) {
                result.add(flower);
            }
        }
        return result;
    }

    /**
     * Prints detailed information about the bouquet, including flowers,
     * accessories, and total cost.
     */
    public void printBouquet() {
        System.out.println("Flowers in the bouquet:");
        flowers.forEach(f -> System.out.println("  - " + f));
        System.out.println("Accessories:");
        accessories.forEach(a -> System.out.println("  - " + a));
        System.out.printf("Total bouquet price: %.2f USD%n", getTotalPrice());
    }
}
