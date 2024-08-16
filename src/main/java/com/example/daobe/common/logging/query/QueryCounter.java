package com.example.daobe.common.logging.query;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Component
@RequestScope
public class QueryCounter {

    private int queryCount;

    public void increase() {
        queryCount++;
    }
}
