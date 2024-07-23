package me.ryan.simplepluginhome.database.async;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ForkJoinPool;

@UtilityClass
public class Async {

    @Getter
    private final ForkJoinPool worker = new ForkJoinPool(32,
            ForkJoinPool.defaultForkJoinWorkerThreadFactory, (t, e) -> e.printStackTrace(), true);

    public <T> CompletableFuture<T> run(Callable<T> supplier) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return supplier.call();
            } catch (Exception exception) {
                throw new CompletionException(exception);
            }
        }, worker);
    }

    public CompletableFuture<Void> run(Runnable runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception exception) {
                throw new CompletionException(exception);
            }
        }, worker);
    }




}