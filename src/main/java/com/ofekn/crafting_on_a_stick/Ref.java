package com.ofekn.crafting_on_a_stick;

import java.util.List;
import java.util.function.*;

public interface Ref<V> {
    V get();
    void set(V value);


    static <V> Ref<V> fromGetterSetter(Supplier<V> getter, Consumer<V> setter) {
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
