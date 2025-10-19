import java.util.*;

public class Main {
    public static void main(String[] args) {
        Rose rose = new Rose(6.0, 40, 9, "Red");
        Tulip tulip = new Tulip(3.0, 25, 8, "Yellow ");
        Lily lily = new Lily(4.5, 35, 10, "White");
        Rose rose2 = new Rose(5.2, 28, 9, "Pink");

        // 1. add()
        FlowerSet<Flower> flowerSet = new FlowerSet<>();
        flowerSet.add(rose);
        flowerSet.add(tulip);
        flowerSet.add(lily);
        System.out.println(toRedString("After add(): ") + flowerSet);

        // 2. addAll()
        flowerSet.addAll(List.of(rose2, tulip));
        System.out.println(toRedString("After addAll(): ") + flowerSet);

        // 3. contains()
        System.out.println(toRedString("Contains Lily? ") + flowerSet.contains(lily));

        // 4. containsAll()
        System.out.println(toRedString("Contains all [Rose, Lily]? ") + flowerSet.containsAll(List.of(rose, lily)));

        // 5. iterator()
        System.out.print(toRedString("Iterating using iterator(): "));
        for (Flower f : flowerSet) {
            System.out.print(f.getName() + " ");
        }
        System.out.println();

        // 6. size() and isEmpty()
        System.out.println(toRedString("Size: ") + flowerSet.size());
        System.out.println(toRedString("Is empty? ") + flowerSet.isEmpty());

        // 7. remove()
        flowerSet.remove(rose);
        System.out.println(toRedString("After remove(Rose): ") + flowerSet);

        // 8. retainAll()
        flowerSet.retainAll(List.of(lily));
        System.out.println(toRedString("After retainAll([Lily]): ") + flowerSet);

        // 9. addAll() again
        flowerSet.addAll(List.of(rose, tulip, rose2));
        System.out.println(toRedString("After re-adding flowers: ") + flowerSet);

        // 10. removeAll()
        flowerSet.removeAll(List.of(rose, tulip));
        System.out.println(toRedString("After removeAll([Rose, Tulip]): ") + flowerSet);

        // 11. toArray()
        Object[] array = flowerSet.toArray();
        System.out.println(toRedString("toArray(): ") + Arrays.toString(array));

        // 12. clear()
        flowerSet.clear();
        System.out.println(toRedString("After clear(): ") + flowerSet + toRedString(", isEmpty = ") + flowerSet.isEmpty());
    }

    private static String toRedString(String text) {
        String ansiRed = "\u001B[31m";
        String ansiReset = "\u001B[0m";
        return ansiRed + text + ansiReset;
    }
}

/**
 * A custom generic implementation of the {@link Set} interface that stores
 * unique elements using a singly linked list as its internal data structure.
 *
 * This class is built upon the generic {@code Flower} class from Lab 5
 *
 * @param <T> the type of elements maintained by this set (must extend {@code Flower})
 */
class FlowerSet<T extends Flower> implements Set<T> {

    /** The head node of the singly linked list. */
    private Node<T> head;

    /** The number of elements in the set. */
    private int size;

    /**
     * Inner class representing a node in the singly linked list.
     *
     * @param <T> the type of the stored value
     */
    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    /**
     * Default constructor that creates an empty.
     */
    public FlowerSet() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Constructor that creates a FlowerSet containing one element.
     *
     * @param element the single element to add to the set
     */
    public FlowerSet(T element) {
        this();
        add(element);
    }

    /**
     * Constructor that creates a FlowerSet from an existing collection.
     *
     * @param collection the collection of elements to add to this set
     */
    public FlowerSet(Collection<? extends T> collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean add(T element) {
        if (contains(element)) {
            return false;
        }
        Node<T> newNode = new Node<>(element);
        newNode.next = head;
        head = newNode;
        size++;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean modified = false;
        for (T element : collection) {
            if (add(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        head = null;
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (current.value.equals(o)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                T value = current.value;
                current = current.next;
                return value;
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        Node<T> current = head;
        Node<T> prev = null;

        while (current != null) {
            if (current.value.equals(o)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node<T> current = head;
        Node<T> prev = null;

        while (current != null) {
            if (!c.contains(current.value)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                modified = true;
            } else {
                prev = current;
            }
            current = current.next;
        }
        return modified;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.value;
            current = current.next;
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E[] toArray(E[] a) {
        if (a.length < size) {
            a = (E[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        Node<T> current = head;
        int index = 0;
        Object[] result = a;
        while (current != null) {
            result[index++] = current.value;
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * Returns a string representation of the set.
     *
     * @return a formatted string listing all flowers in the set
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) {
                sb.append(",\n\t");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}


//LAB 5

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
