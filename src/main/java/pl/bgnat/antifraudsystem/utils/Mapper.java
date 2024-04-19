package pl.bgnat.antifraudsystem.utils;

import java.util.function.Function;

public interface Mapper<K, T> extends Function<K, T> {
    T map(K model);
}
