package com.exemplo.produtos.logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;

class AppLoggerTest {

    @Test
    void construtorDeveSerPrivado() throws Exception {
        int modificadores = AppLogger.class.getDeclaredConstructor().getModifiers();
        assertEquals(true, Modifier.isPrivate(modificadores));
    }

    @Test
    void getInstanceDeveRetornarSempreAMesmaInstancia() {
        assertSame(AppLogger.getInstance(), AppLogger.getInstance());
    }

    @Test
    void getInstanceDeveRetornarUmaUnicaInstanciaEmVariasThreads() throws Exception {
        int threads = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch largada = new CountDownLatch(1);
        Set<AppLogger> instancias = new HashSet<>();
        try {
            Future<AppLogger>[] resultados = new Future[threads];
            for (int i = 0; i < threads; i++) {
                resultados[i] = executor.submit(() -> {
                    largada.await();
                    return AppLogger.getInstance();
                });
            }
            largada.countDown();
            for (Future<AppLogger> resultado : resultados) {
                instancias.add(resultado.get());
            }
        } finally {
            executor.shutdown();
        }
        assertEquals(1, instancias.size());
    }
}
