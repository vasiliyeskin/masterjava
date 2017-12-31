package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {
    private static final String INTERRUPTED_BY_TIMEOUT = "+++ Interrupted by timeout";
    private static final String INTERRUPTED_EXCEPTION = "+++ InterruptedException";

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor, int threadnumber) throws InterruptedException, ExecutionException, Exception {
        final int matrixSize = matrixA.length;

        final CompletionService<int[][]> completionService = new ExecutorCompletionService<>(executor);

        List<Future<int[][]>> futures = new ArrayList<>();

        int[] iar = new int[threadnumber + 1];
        for (int i = 1; i < threadnumber + 1; i++) {
            iar[i] = iar[i - 1] + matrixSize / threadnumber;
        }
        for (int i = 0; i < threadnumber; i++) {
            final int j = i;
            futures.add(completionService.submit(new Callable<int[][]>() {
                @Override
                public int[][] call() throws Exception {
                    return singleThreadMultiplyFromNToMRowOfMatrixA(matrixA, matrixB, iar[j], iar[j+1]);
                }
            }));
        }

        //   return matrixC;
        return new Callable<int[][]>() {
            @Override
            public int[][] call() throws Exception {
                int[][] matrixD = new int[matrixSize][matrixSize];

                while (!futures.isEmpty()) {
                    try {
                        Future<int[][]> future = completionService.poll(1, TimeUnit.SECONDS);
                        if (future == null) {
                            return cancelWithFail(INTERRUPTED_BY_TIMEOUT);
                        }
                        futures.remove(future);
                        int[][] result = future.get();
                        addElementsToMatrix(matrixD, result, matrixSize);

                    } catch (ExecutionException e) {
                        return cancelWithFail(e.getCause().toString());
                    } catch (InterruptedException e) {
                        return cancelWithFail(INTERRUPTED_EXCEPTION);
                    }
                }
                return matrixD;
            }

            public void addElementsToMatrix(int[][] matrixC, int[][] result, int size) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        matrixC[i][j] = matrixC[i][j] + result[i][j];
                    }
                }
            }

            private int[][] cancelWithFail(String cause) {
                futures.forEach(f -> f.cancel(true));
                return new int[matrixSize][matrixSize];
            }
        }.call();
    }


    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int thatColumn[] = new int[matrixSize];

        try {
            for (int i = 0; ; i++) {
                for (int j = 0; j < matrixSize; j++) {
                    thatColumn[j] = matrixB[j][i];
                }

                for (int j = 0; j < matrixSize; j++) {
                    int thisRow[] = matrixA[j];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        //                    sum += matrixA[i][k] * matrixB[k][j];
                        //                    sum += matrixA[i][k] * BT[j][k];
                        sum += thisRow[k] * thatColumn[k];
                    }
                    matrixC[j][i] = sum;
                }
            }
        } catch (IndexOutOfBoundsException ignore) {
        }

        return matrixC;
    }

    public static int[][] singleThreadMultiplyFromNToMRowOfMatrixA(int[][] matrixA, int[][] matrixB, int N, int M) {
        final int matrixSize = matrixB.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];


        int BT[][] = new int[matrixSize][matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                BT[j][i] = matrixB[i][j];
            }
        }

       for (int i = N; i < M; i++) {
             for (int j = 0; j < matrixSize; j++) {
                int summand = 0;
                for (int k = 0; k < matrixSize; k++) {
                    summand += matrixA[i][k] * BT[j][k];
                }
                matrixC[i][j] = summand;
            }
        }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
