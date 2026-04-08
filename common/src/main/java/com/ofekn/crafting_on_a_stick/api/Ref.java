package com.ofekn.crafting_on_a_stick.api;

import java.util.List;
import java.util.function.*;

/**
 * A reference wrapper interface that provides getter and setter access to a value.
 * <p>
 * This interface is used to wrap values (typically ItemStacks in inventories) so they can be
 * accessed and modified through a uniform interface, regardless of their underlying storage
 * mechanism (arrays, lists, custom containers, etc.).
 * <p>
 * The primary use case is to allow modification of ItemStacks in various inventory types
 * (player inventory, Curios slots, etc.) through a common interface.
 *
 * @param <V> the type of value being referenced
 */
public interface Ref<V> {
    /**
     * Gets the current value.
     *
     * @return the current value
     */
    V get();

    /**
     * Sets a new value.
     *
     * @param value the new value to set
     */
    void set(V value);

    /**
     * Creates a Ref from a getter and setter function pair.
     * <p>
     * This factory method is useful for creating references to values that are accessed
     * through methods rather than direct field access.
     *
     * @param <V> the type of value
     * @param getter a function that retrieves the value
     * @param setter a function that sets the value
     * @return a Ref that delegates to the provided getter and setter
     */
    static <V> Ref<V> of(Supplier<V> getter, Consumer<V> setter) {
        return new Ref<>() {
            @Override
            public V get() {
                return getter.get();
            }

            @Override
            public void set(V value) {
                setter.accept(value);
            }
        };
    }

    /**
     * Creates a Ref that references a specific index in an array.
     * <p>
     * Changes made through the returned Ref will directly modify the array element.
     *
     * @param <V> the type of array elements
     * @param arr the array to reference
     * @param index the index in the array to reference
     * @return a Ref that accesses the array element at the specified index
     */
    static <V> Ref<V> ofArrayIndex(V[] arr, int index) {
        return new Ref<>() {
            @Override
            public V get() {
                return arr[index];
            }

            @Override
            public void set(V value) {
                arr[index] = value;
            }
        };
    }

    /**
     * Creates Refs for every index in a range and adds them to the result list.
     * <p>
     * This method is useful for creating references to all elements in a collection-like
     * structure that is accessed by index. For each index from 0 to {@code size - 1}, a Ref
     * is created that uses the provided getter and setter functions.
     * <p>
     * Example usage: creating Refs for all slots in a player's inventory.
     *
     * @param <V> the type of values
     * @param getter a function that takes an index and returns the value at that index
     * @param setter a function that takes an index and a value, and sets the value at that index
     * @param size the number of indices to create Refs for
     * @param result the list to add all created Refs to
     */
    static <V> void forEveryIndex(IntFunction<V> getter, BiConsumer<Integer, V> setter, int size, List<Ref<V>> result) {
        for (int i = 0; i < size; i++) {
            final int finalI = i;
            result.add(new Ref<>() {
                @Override
                public V get() {
                    return getter.apply(finalI);
                }

                @Override
                public void set(V value) {
                    setter.accept(finalI, value);
                }
            });
        }
    }


}
