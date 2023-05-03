package dev.n7meless.service;

import dev.n7meless.exceptions.IncorrectFilterEntryException;
import dev.n7meless.filter.ExpressionFilter;
import dev.n7meless.filter.impl.SimpleExpressionFilter;
import dev.n7meless.model.Airport;
import dev.n7meless.model.comparator.AirportNameComparator;
import dev.n7meless.model.comparator.AirportRowComparator;
import dev.n7meless.service.impl.ConsumerServiceImpl;
import dev.n7meless.service.impl.ProducerServiceImpl;
import dev.n7meless.util.AirportFileReader;
import dev.n7meless.util.FileReader;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.*;

public class AirportService {
    private final BlockingQueue<String> rowsQueue = new LinkedBlockingQueue<>();
    private final AirportNameComparator nameComparator = new AirportNameComparator();
    private final AirportRowComparator rowComparator = new AirportRowComparator();
    private final SortedSet<Airport> airports =
            Collections.synchronizedSortedSet(new TreeSet<>(nameComparator.thenComparing(rowComparator)));
    private final ProducerService producerService = new ProducerServiceImpl(rowsQueue);
    private final ConsumerService consumerService = new ConsumerServiceImpl(rowsQueue, airports);
    private final FileReader utility = new AirportFileReader(producerService);

    public boolean readFileWithFilter(String filter) {

        if (!airports.isEmpty()) return readMapWithFilter(filter);


        ExecutorService producerExecutor = Executors.newSingleThreadExecutor();
        ExecutorService consumerExecutor =
                Executors.newFixedThreadPool(4);

        rowsQueue.clear();

        producerExecutor.execute(utility::readFile);
        Future<Boolean> submit = consumerExecutor.submit(() ->
                consumerService.consumeRow(new SimpleExpressionFilter(filter))
        );

        try {
            return submit.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Произошла ошибка при параллелньом вычислении");
        } finally {
            producerExecutor.shutdown();
            consumerExecutor.shutdown();
        }
        return false;
    }

    private boolean readMapWithFilter(String filter) {
        ExpressionFilter expressionFilter = new SimpleExpressionFilter(filter);


        Iterator<Airport> iterator = airports.iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            String value = entry.getRow();
            String[] columns = value.split(",");

            try {
                if (!expressionFilter.checkColumnsForCondition(columns)) {
                    iterator.remove();
                }
            } catch (IncorrectFilterEntryException e) {
                return false;
            }
        }
        return true;
    }


    public void findByAirportName(String airportName) {
        long x = System.currentTimeMillis();
        airports.removeIf((entry) -> {
            String key = entry.getName().toLowerCase();

            if (!key.startsWith(airportName)) {
                return true;
            } else {
                System.out.printf("\"%s\"[%s]\n", entry.getName(), entry.getRow());
                return false;
            }
        });

        long y = System.currentTimeMillis();

        System.out.println("* Количество найденных строк: " + airports.size());
        System.out.println("* Время, затраченное на поиск: " + (y - x) + " мс");
    }

    public void reset() {
        airports.clear();
        rowsQueue.clear();
    }
}
