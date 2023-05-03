package dev.n7meless.filter;

import dev.n7meless.exceptions.IncorrectFilterEntryException;

public interface ExpressionFilter {

    boolean checkCondition(String[] row, int colNum, String value, char condition);

    boolean isDigit(String value);

    boolean checkColumnsForCondition(String[] columns) throws IncorrectFilterEntryException;
}
