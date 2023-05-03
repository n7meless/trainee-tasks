package dev.n7meless.util;

import dev.n7meless.service.ProducerService;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AirportFileReader implements FileReader {
    private final ProducerService producerService;
    private final File file =
            new File("airports.csv");

    public AirportFileReader(ProducerService producerService) {
        this.producerService = producerService;
    }

    @Override
    public void readFile() {

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {

            in.lines().forEach(producerService::produce);


        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
