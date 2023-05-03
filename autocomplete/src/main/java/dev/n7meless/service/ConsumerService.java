package dev.n7meless.service;

import dev.n7meless.filter.ExpressionFilter;

public interface ConsumerService {
    boolean consumeRow(ExpressionFilter filter);
}
