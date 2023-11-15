package io.ylab.walletservice.testutil;

/**
 * The {@code TestBuilder} interface serves as the basic contract for all classes created to produce new objects
 * specifically designed for testing purposes. Implementing classes should provide a standardized way of constructing
 * objects with default or customizable values for use in testing scenarios.
 *
 * <p>
 * Usage:
 * <ul>
 *   <li>Implement this interface in a class for a specific domain object (e.g., {@code Account}, {@code Player}).</li>
 *   <li>Define attributes and default values for the object within the implementing class.</li>
 *   <li>Implement the {@code build()} method to construct and return an instance of the domain object with the specified attributes.</li>
 *   <li>Use the implementing class in test scenarios to create objects with consistent and controlled values.</li>
 * </ul>
 * </p>
 *
 * @param <T> The type of object that the implementing class builds.
 */
public interface TestBuilder<T> {
    /**
     * Constructs and returns an instance of the domain object with the specified attributes.
     *
     * @return An instance of the domain object.
     */
    T build();
}
