package dev.n7meless.db;

import java.util.HashMap;
import java.util.Map;

public class Row {
    private Map<Object, String> columnsHashMap = new HashMap<>();

    public Row() {
    }

    public Row(Map<String, Object> columnsHashMap) {
        setColumns(columnsHashMap);
    }

    public void setColumns(Map<String, Object> columnsHashMap) {
        for (Map.Entry<String, Object> entry : columnsHashMap.entrySet()) {
            this.columnsHashMap.put(entry.getValue(), entry.getKey());
        }
    }

    public boolean existsKeyByColumnValue(Object column) {
        return columnsHashMap.get(column) != null;
    }

    public String add(String key, Object value) {
        return columnsHashMap.put(value, key);
    }

    public void update(String key, Object newValue) {
        remove(key);
        columnsHashMap.put(newValue, key);
    }

    public String remove(String key) {
        Object oldValue = findValueByKey(key);
        return columnsHashMap.remove(oldValue);
    }

    public Object findValueByKey(String key) {
        for (Map.Entry<Object, String> entry : columnsHashMap.entrySet()) {
            if (entry.getValue().equals(key)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Map<String, Object> getColumns() {
        Map<String, Object> columns = new HashMap<>();
        for (Map.Entry<Object, String> entry : columnsHashMap.entrySet()) {
            columns.put(entry.getValue(), entry.getKey());
        }
        return columns;
    }

    public void clearColumns() {
        columnsHashMap.clear();
    }

    public void removeColumnsByKey(String... keys) {
        for (int i = 0; i < keys.length; i++) {
            Object value = findValueByKey(keys[i]);
            columnsHashMap.remove(value);
        }
    }
}


