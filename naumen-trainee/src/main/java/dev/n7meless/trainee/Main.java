package dev.n7meless.trainee;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Алгоритм решения:
 * 1.Сортируем полученные запросы по их идентификатору.
 * 2.Создаем кэш (в данном случае используем структуру данных HashMap,
 * в котором в качестве ключа используется идентификатор,
 * для быстрого извлечения запроса).
 * 3.Проверяем, существует ли запрос с таким идентификатором
 * в нашем кэше. Если нет, то добавляем и увеличиваем счетчик
 * вызовов к распределенному серверу.
 * 4.Если память кэша заполнилась, то очищаем и возвращаемся к пункту 3.
 */
public class Main {


    public static void main(String[] args) {

        String content = readFromFile("input.txt");

        var result = content.split("\n");

        var params = result[0].split(" ");
        int maxCacheSize = Integer.parseInt(params[0]);
        int maxRequests = Integer.parseInt(params[1]);

        long[] requestIds = new long[maxRequests];
        for (int i = 0; i < maxRequests; i++) {
            requestIds[i] = Long.parseLong(result[i + 1]);
        }

        int calls = processRequests(requestIds, maxCacheSize, maxRequests);

        writeToFile("output.txt", String.valueOf(calls));
    }

    /**
     * @param requests     - массив запросов
     * @param maxCacheSize - максимальное количество запросов,
     *                     которое может быть закэшировано на сервере
     * @param maxRequests  - максимальное количество запросов
     * @return - количество обращений к распределенному серверу
     */
    public static int processRequests(long requests[], int maxCacheSize, int maxRequests) {
        Arrays.sort(requests);
        Map<Long, Integer> map = new HashMap<>(maxCacheSize, 1.1F);
        int calls = 0;
        for (int i = 0; i < maxRequests; i++) {
            if (!map.containsKey(requests[i])) {
                if (map.size() == maxCacheSize) {
                    map.clear();
                }
                map.put(requests[i], 0);
                calls++;
            }
        }
        return calls;
    }

    public static String readFromFile(String fileName) {
        File inputFile = new File(fileName);

        String lines = "";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputFile), StandardCharsets.UTF_8))) {

            lines = reader.lines().collect(Collectors.joining("\n"));

        } catch (FileNotFoundException e) {
            System.out.println(("File with name " + fileName + " doesn't exists in path " + inputFile.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the file " + fileName + " in path " + inputFile.getAbsolutePath());
        }
        return lines;
    }

    public static void writeToFile(String fileName, String content) {
        File outputFile = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            writer.write(content);

        } catch (FileNotFoundException e) {
            System.out.println("File with name " + fileName + " doesn't exists in path " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file " + fileName + " in path " + outputFile.getAbsolutePath());
        }
    }
}

