package dev.n7meless;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InMemoryDatabaseTest {
    private final Map<String, Object> columnsHashMap = new HashMap<>();
    private final InMemoryDatabase memoryDatabase = new InMemoryDatabase();
    private final Object columnValue = 234567;

    @BeforeAll
    public void init() {
        columnsHashMap.put("account", 234567);
        columnsHashMap.put("name", "Иванов Иван Иванович");
        columnsHashMap.put("value", 2035.34);

        memoryDatabase.addRow(columnsHashMap);
    }

    @Test
    @DisplayName("Полный набор записей по любому из полей")
    public void findRowByValueColumn() {
        var row = memoryDatabase.findRowByValueColumn(columnValue);
        Assertions.assertTrue(row.isPresent());
    }

    @Test
    @DisplayName("Добавление записей")
    public void addColumns() {
        var row = memoryDatabase.findRowByValueColumn(columnValue).get();
        var columns = row.getColumns();

        int oldSize = columns.size();

        Map<String, Object> columnsHashMap = new HashMap<>();

        columnsHashMap.put("age", 14);
        columnsHashMap.put("date", LocalDate.now());

        memoryDatabase.addColumns(row, columnsHashMap);

        columns = row.getColumns();
        int newSize = columns.size();

        Assertions.assertTrue(oldSize < newSize);
    }

    @Test
    @DisplayName("Изменение записей по ключу")
    public void update() {
        Object columnValue = 234567;
        var row = memoryDatabase.findRowByValueColumn(columnValue).get();
        String oldName = (String) memoryDatabase.findValueByKey(row, "name");

        memoryDatabase.updateColumn(row, "name", "Алексеев Алекесей Алексеевич");
        String newName = (String) memoryDatabase.findValueByKey(row, "name");
        Assertions.assertNotEquals(oldName, newName);
    }

    @Test
    @DisplayName("Удаление ненужных записей")
    public void removeColumn() {
        var row = memoryDatabase.findRowByValueColumn(columnValue).get();
        var columns = row.getColumns();

        int oldSize = columns.size();
        memoryDatabase.removeColumnsByKey(row, "name", "value");

        columns = row.getColumns();
        int newSize = columns.size();

        Assertions.assertTrue(oldSize > newSize);
    }

}