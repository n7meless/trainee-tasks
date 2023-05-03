package dev.n7meless;

import dev.n7meless.service.AirportService;

import javax.script.ScriptException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    private final static AirportService airportService = new AirportService();

    public static void main(String[] args) throws ScriptException, ExecutionException, InterruptedException {

        String filterExpression = " ";
        boolean isFilter = false;
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {

                while (!isFilter) {
                    System.out.print("- Введите фильтр: ");
                    filterExpression = scanner.nextLine();
                    if (!isCommand(filterExpression)) {
                        if (filterExpression.isEmpty()) {
                            isFilter = true;
                        } else {
                            isFilter = airportService.readFileWithFilter(filterExpression);
                        }
                    }
                }
                if (isFilter) {
                    System.out.print("- Введите имя аэропорта: ");
                    filterExpression = scanner.nextLine();
                    if (!isCommand(filterExpression)) {
                        airportService.findByAirportName(filterExpression.toLowerCase());
                        isFilter = false;
                    }
                }
            }
        }
    }

    private static boolean isCommand(String command) {
        switch (command) {
            case "!quit": {
                System.exit(0);
                return true;
            }
            case "!reset": {
                airportService.reset();
                System.out.println("Фильтр был успешно очищен!");
                return true;
            }
            case "!help": {
                System.out.println("Команды:\n" +
                        "\t!quit\t - завершить программу\n" +
                        "\t!reset\t - сбросить фильтр\n" +
                        "\t!help\t - посмотреть команды");
                return true;
            }
            default:
                return false;
        }
    }
}
//        String filterExp = "(column[1]>6000||(column[2]<>'Bowman Field'))&(column[3]='JKA'||column[4]<>'Papua New Guinea')";


