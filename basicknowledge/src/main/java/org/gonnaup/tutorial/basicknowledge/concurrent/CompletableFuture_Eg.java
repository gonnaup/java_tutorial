package org.gonnaup.tutorial.basicknowledge.concurrent;

import java.util.List;
import java.util.concurrent.*;

/**
 * CompletableFuture 的常见使用方法
 *
 * @author gonnaup
 * @version created at 2023/5/13 下午12:10
 * @see java.util.concurrent.CompletableFuture
 */
public class CompletableFuture_Eg {

    static final ThreadPoolExecutor COMMON_POOL = new ThreadPoolExecutor(4, 6, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * 使用{@link CompletableFuture} 计算长方形的面积和周长
     */
    static void calculateSquareAreaPerimeter() {
        final Square s1 = new Square(4.5, 3);
        final Square s2 = new Square(8, 3.5);
        final Square s3 = new Square(12.8, 8.3);
        List<Square> squareList = List.of(s1, s2, s3);
        CompletableFuture<Void> areaTask = CustomPoolCompletableFuture.supplyAsync(() -> squareList, COMMON_POOL)
                .thenApplyAsync(squares -> squares.stream().map(square -> square.length * square.width).toList())
                .thenAcceptAsync(areas -> areas.forEach(area -> System.out.println("Area  " + Thread.currentThread().getName() + " => " + area)), COMMON_POOL);
        CompletableFuture<Void> perimeterTask = CustomPoolCompletableFuture.supplyAsync(() -> squareList, COMMON_POOL)
                .thenApplyAsync(squares -> squares.stream().map(square -> square.length * 2 + square.width * 2).toList())
                .thenAcceptAsync(perimeters -> perimeters.forEach(perimeter -> System.out.println("Perimeter  " + Thread.currentThread().getName() + " => " + perimeter)));

        CompletableFuture<Void> allOf = CustomPoolCompletableFuture.allOf(areaTask, perimeterTask);
        allOf.join();


    }

    public static void main(String[] args) {
        calculateSquareAreaPerimeter();

        COMMON_POOL.shutdown();
    }

    /**
     * 使用自定义线程池的 CompletableFuture
     *
     * @param <T>
     */
    public static class CustomPoolCompletableFuture<T> extends CompletableFuture<T> {

        //简单的自定义线程池
        static final Executor THREAD_POOL = new ThreadPoolExecutor(4, 6, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10),
                new ThreadPoolExecutor.CallerRunsPolicy());

        @Override
        public <U> CompletableFuture<U> newIncompleteFuture() {
            return new CustomPoolCompletableFuture<U>();
        }

        @Override
        public Executor defaultExecutor() {
            return THREAD_POOL;
        }

        @Override
        public void obtrudeValue(T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void obtrudeException(Throwable ex) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * 正方形
     *
     * @param length
     * @param width
     */
    record Square(double length, double width) {
    }

}
