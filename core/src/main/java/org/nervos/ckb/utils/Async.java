package org.nervos.ckb.utils;

import java.util.concurrent.*;

/** Async task facilitation. */
public class Async {

  private static final ExecutorService executor = Executors.newCachedThreadPool();

  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                new Runnable() {
                  @Override
                  public void run() {
                    shutdown(executor);
                  }
                }));
  }

  public static <T> Future<T> run(Callable<T> callable) {
    return executor.submit(callable);
  }

  private static int getCpuCount() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Provide a new ScheduledExecutorService instance.
   *
   * <p>A shutdown hook is created to terminate the thread pool on application termination.
   *
   * @return new ScheduledExecutorService
   */
  public static ScheduledExecutorService defaultExecutorService() {
    final ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(getCpuCount());

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                new Runnable() {
                  @Override
                  public void run() {
                    shutdown(scheduledExecutorService);
                  }
                }));

    return scheduledExecutorService;
  }

  /**
   * Shutdown as per {@link ExecutorService} Javadoc recommendation.
   *
   * @param executorService executor service we wish to shut down.
   */
  private static void shutdown(ExecutorService executorService) {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
          System.err.println("Thread pool did not terminate");
        }
      }
    } catch (InterruptedException ie) {
      executorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
