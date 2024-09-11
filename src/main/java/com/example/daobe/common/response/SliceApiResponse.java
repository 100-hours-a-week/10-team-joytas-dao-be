package com.example.daobe.common.response;

import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Slice;

public record SliceApiResponse<T>(
        boolean hasNext,
        Object nextCursor,
        List<T> data
) {

    public static <T> SliceApiResponse<T> of(Slice<T> slice, Function<T, Object> cursorExtractor) {
        List<T> content = slice.getContent();
        Object nextCursor = slice.hasNext() && !slice.isEmpty()
                ? cursorExtractor.apply(content.get(content.size() - 1))
                : null;
        return new SliceApiResponse<>(slice.hasNext(), nextCursor, content);
    }
}
