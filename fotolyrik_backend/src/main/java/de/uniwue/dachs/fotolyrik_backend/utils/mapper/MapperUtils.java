package de.uniwue.dachs.fotolyrik_backend.utils.mapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapperUtils {
    public static <I, O> List<O> mapList(Collection<I> input, Function<I, O> mapper) {
        if (input == null || input.isEmpty()) return Collections.emptyList();
        return input.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static <I, O> Set<O> mapSet(Collection<I> input, Function<I, O> mapper) {
        if (input == null || input.isEmpty()) return Collections.emptySet();
        return input.stream()
                .map(mapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
