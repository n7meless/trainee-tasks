package dev.n7meless.db;

import dev.n7meless.exception.RowNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryDatabase {
    private List<Row> rows;
    public InMemoryDatabase() {
        rows = new ArrayList<>(10);
    }

    public void addRow(Map<String, Object> columns) {
        Row row = new Row(columns);
        rows.add(row);
    }

    public Optional<Row> findRowByValueColumn(Object column) {
        Row currRow = null;
        try {
            currRow = rows.stream()
                    .filter(row -> row.existsKeyByColumnValue(column))
                    .findFirst()
                    .orElseThrow(RowNotFoundException::new);

        } catch (RowNotFoundException e) {
            System.out.println("Row with column value " + column + " doesn't exists");
        }
        return Optional.of(currRow);
    }

    public Object findValueByKey(Row row, String key) {
        return row.findValueByKey(key);
    }


    public void addColumns(Row row, Map<String, Object> columnsHashMap) {
        row.setColumns(columnsHashMap);
    }

    public boolean existsRowByColumnValue(Row row, Object column) {
        return row.existsKeyByColumnValue(column);
    }

    public String addColumn(Row row, String key, Object value) {
        return row.add(key, value);
    }

    public void updateColumn(Row row, String key, Object newValue) {
        row.update(key, newValue);
    }

    public String removeColumn(Row row, String key) {
        return row.remove(key);
    }

    public List<Row> getRows() {
        return rows;
    }

    public boolean removeRow(Row row) {
        row.clearColumns();
        return rows.remove(row);
    }

    public void removeColumnsByKey(Row row, String... keys) {
        row.removeColumnsByKey(keys);
    }
    public Map<String, Object> getColumns(Row row) {
        return row.getColumns();
    }
}