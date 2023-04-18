package dev.n7meless.trainee;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProcessRequestsTest {
    @Test
    @DisplayName("Проверка алгоритма на время выполнения при худшем случае")
    void timeLimitProcessRequestsTest() {
        int maxRequests = 100000;
        long requests[] = new long[maxRequests];
        fillArray(requests, maxRequests);

        long x = System.currentTimeMillis();
        Main.processRequests(requests, 1, maxRequests);
        long y = System.currentTimeMillis();
        Assertions.assertTrue((y - x) < 3000);
    }

    @Test
    @DisplayName("Проверка алгоритма по памяти при худшем случае")
    void memoryLimitProcessRequestsTest() {
        int maxRequests = 100000;
        long requests[] = new long[maxRequests];
        fillArray(requests, maxRequests);

        Main.processRequests(requests, 100000, maxRequests);
        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println(usedBytes);
        Assertions.assertTrue(usedBytes / 1048576 < 64);
    }

    @Test
    @DisplayName("Проверка алгоритма на правильность выполнения")
    void processRequestsTest() {
        int maxCacheSize = 5;
        int maxRequests = 15;
        long requests[] = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 7, 9, 3};

        int calls = Main.processRequests(requests, maxCacheSize, maxRequests);
        Assertions.assertTrue(calls == 9);
    }

    private void fillArray(long[] arr, int maxRequests) {
        for (int i = 0; i < maxRequests; i++) {
            arr[i] = 1 + (long) (Math.random() * Long.MAX_VALUE);
        }
    }
}
