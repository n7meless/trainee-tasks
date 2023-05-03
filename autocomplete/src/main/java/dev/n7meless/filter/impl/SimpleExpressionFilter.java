package dev.n7meless.filter.impl;

import dev.n7meless.exceptions.IncorrectFilterEntryException;
import dev.n7meless.filter.ExpressionFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleExpressionFilter implements ExpressionFilter {
    private Matcher matcher;

    public SimpleExpressionFilter(String expression) {
        setExpressionMatcher(expression);
    }

    public String convertToBoolExpression(String[] columns) {
        return matcher.replaceAll((matchResult) ->
                {
                    String expression = matchResult.group();
                    int closeBracketIdx = expression.indexOf("]");
                    int colNum = Integer.parseInt(expression.substring(7, closeBracketIdx)) - 1;
                    String value = expression.substring(closeBracketIdx + 2);
                    char condition = expression.charAt(closeBracketIdx + 1);

                    var result = checkCondition(columns, colNum, value, condition);
                    return String.valueOf(result);
                }
        );
    }

    @Override
    public boolean checkColumnsForCondition(String[] columns) throws IncorrectFilterEntryException {
        var boolOnString = convertToBoolExpression(columns);
        return parseBooleanExpression(boolOnString);
    }

    @Override
    public boolean isDigit(String value) {
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCondition(String[] row, int colNum, String value, char condition) {
        boolean digit = isDigit(value);
        switch (condition) {
            case '<':
                return Double.parseDouble(row[colNum]) < Double.parseDouble(value);
            case '>':
                return Double.parseDouble(row[colNum]) > Double.parseDouble(value);
            case '^':
                if (digit) {
                    return Long.parseLong(row[colNum]) != Long.parseLong(value);
                } else return !row[colNum].equalsIgnoreCase("\"" + value + "\"");
            default:
                if (digit) {
                    return Long.parseLong(row[colNum]) == Long.parseLong(value);
                } else return row[colNum].equalsIgnoreCase("\"" + value + "\"");
        }
    }

    private void setExpressionMatcher(String expression) {

        expression = expression.replaceAll("['\"]", "")
                .replace("<>", "^");

        Pattern pattern = Pattern.compile("column(.)\\d(.)[\\D]{1,2}[\\w\\s]*");
        this.matcher = pattern.matcher(expression);
    }

    public boolean parseBooleanExpression(String expression) throws IncorrectFilterEntryException {
        return new Object() {
            final int length = expression.length();
            int index = 0;

            boolean match(String expect) {
                while (index < length && Character.isWhitespace(expression.charAt(index)))
                    ++index;
                if (index >= length)
                    return false;
                if (expression.startsWith(expect, index)) {
                    index += expect.length();
                    return true;
                }
                return false;
            }

            boolean element() throws IncorrectFilterEntryException {
                if (match("true"))
                    return true;
                else if (match("false"))
                    return false;
                else if (match("(")) {
                    boolean result = expression();
                    if (!match(")"))
                        throw new IncorrectFilterEntryException();
                    return result;
                } else throw new IncorrectFilterEntryException();
            }


            boolean factor() throws IncorrectFilterEntryException {
                boolean result = element();
                while (match("&"))
                    result &= element();
                return result;
            }

            boolean expression() throws IncorrectFilterEntryException {
                boolean result = factor();
                while (match("||"))
                    result |= factor();
                return result;
            }

            boolean parse() throws IncorrectFilterEntryException {
                boolean result = expression();
                if (index < length)
                    throw new IncorrectFilterEntryException();
                return result;
            }
        }.parse();
    }

}
