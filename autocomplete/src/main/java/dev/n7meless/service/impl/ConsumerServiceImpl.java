package dev.n7meless.service.impl;

import dev.n7meless.exceptions.IncorrectFilterEntryException;
import dev.n7meless.filter.ExpressionFilter;
import dev.n7meless.model.Airport;
import dev.n7meless.service.ConsumerService;

import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;

public class ConsumerServiceImpl implements ConsumerService {
    private final BlockingQueue<String> rowsQueue;
    private final SortedSet<Airport> airports;

    public ConsumerServiceImpl(BlockingQueue<String> rowsQueue,
                               SortedSet<Airport> airports) {
        this.rowsQueue = rowsQueue;
        this.airports = airports;
    }

    @Override
    public boolean consumeRow(ExpressionFilter filter) {
        try {
            while (true) {

                String row = rowsQueue.take();
                String[] columns = row.split(",");

                if (filter.checkColumnsForCondition(columns)) {
                    airports.add(new Airport(columns[1].replace("\"", ""), row));
                }
                if (rowsQueue.isEmpty()) break;
            }
            return true;
        } catch (InterruptedException | IncorrectFilterEntryException e) {
            return false;
        }
    }
}
