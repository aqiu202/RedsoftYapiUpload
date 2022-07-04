package com.github.aqiu202.ideayapi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 通用Builders
 *
 * @param <T>
 */
public final class Builders<T> {

    private final Supplier<T> supplier;

    private final List<Consumer<T>> list = new ArrayList<>();

    private Builders(Supplier<T> value) {
        this.supplier = value;
    }

    public static <T> Builders<T> of(Supplier<T> value) {
        return new Builders<>(value);
    }

    public <P1> Builders<T> with(BiConsumer<T, P1> biConsumer, P1 p1) {
        this.list.add(t -> biConsumer.accept(t, p1));
        return this;
    }

    public T build() {
        T t = this.supplier.get();
        this.list.forEach(item -> item.accept(t));
        this.list.clear();
        return t;
    }

}
