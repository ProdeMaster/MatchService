package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * Interceptor handling retry logic for Sportmonks API calls.
 * Implements an exponential backoff strategy to handle transient failures.
 */
public class SportmonksRetryInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SportmonksRetryInterceptor.class);

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_DELAY_MS = 1000;
    private static final double BACKOFF_MULTIPLIER = 2.0;
    private static final long MAX_DELAY_MS = 10000;

    private SportmonksRetryInterceptor() {
    }

    /**
     * Executes an operation with automatic retry logic.
     * <p>
     * Uses an exponential backoff retry mechanism. If the operation fails,
     * it waits for a specified time before retrying, increasing the wait time
     * with each failed attempt until the maximum number of retries is reached.
     *
     * @param request       Supplier encapsulating the API call or operation to
     *                      execute.
     * @param operationName Descriptive name of the operation for logging purposes.
     * @param <T>           Return type of the operation.
     * @return The result of the operation if successful.
     * @throws RuntimeException if all attempts are exhausted or if the thread is
     *                          interrupted.
     */
    public static <T> T executeWithRetry(Supplier<T> request, String operationName) {
        int attempt = 0;
        long delay = INITIAL_DELAY_MS;

        while (attempt < MAX_RETRIES) {
            attempt++;
            try {
                return request.get();
            } catch (Exception e) {
                log.warn("Intento {} de {} para {} falló: {}",
                        attempt, MAX_RETRIES, operationName, e.getMessage());

                if (attempt >= MAX_RETRIES) {
                    log.error("Todos los {} intentos fallaron para {}", MAX_RETRIES, operationName);
                    throw e;
                }

                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrumpido", ie);
                }

                delay = (long) (delay * BACKOFF_MULTIPLIER);
                delay = Math.min(delay, MAX_DELAY_MS);
            }
        }

        throw new IllegalStateException("No se pudo completar la operación después de " + MAX_RETRIES + " intentos");
    }
}
