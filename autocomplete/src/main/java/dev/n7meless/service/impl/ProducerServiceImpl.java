package dev.n7meless.service.impl;

import dev.n7meless.service.ProducerService;

import java.util.concurrent.BlockingQueue;

public class ProducerServiceImpl implements ProducerService {
    private final BlockingQueue<String> rowsQueue;

    public ProducerServiceImpl(BlockingQueue<String> rowsQueue) {
        this.rowsQueue = rowsQueue;
    }

    @Override
    public void produce(String row) {
        try {
            rowsQueue.put(row);
        } catch (InterruptedException e) {
            System.out.println("При обработке строк произошла ошибка.");
        }
    }
}