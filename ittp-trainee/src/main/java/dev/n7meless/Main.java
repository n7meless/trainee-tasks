package dev.n7meless;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        InMemoryDatabase memoryDatabase = new InMemoryDatabase();

        HashMap<String, Object> columns1 = new HashMap<>();
        columns1.put("account", 234567);
        columns1.put("name", "Иванов Иван Иванович");
        columns1.put("value", 2035.34);

        memoryDatabase.addRow(columns1);

        Row row = memoryDatabase.findRowByValueColumn(234567).get();
        System.out.println("All columns in row " + memoryDatabase.getColumns(row));

        row.remove("account");
        System.out.println("Row after remove key account " + memoryDatabase.getColumns(row));

        row.remove("value");
        System.out.println("Row after remove key value " + memoryDatabase.getColumns(row));

        row.update("name", "Алексеев Алексей Алексеевич");
        System.out.println("Row after update " + memoryDatabase.getColumns(row));

        row.add("value", 9932.15);
        System.out.println("Row after add column with key \"value\" " + memoryDatabase.getColumns(row));
    }
}