package org.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MainTest {

    private Scanner mockScanner;

    @BeforeEach
    void setUp() {
        // Inicializáljuk a mockolt Scanner objektumot
        mockScanner = mock(Scanner.class);
    }

    @Test
    void testPrivateConstructor() throws Exception {
        Constructor<Main> constructor = Main.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertThrows(UnsupportedOperationException.class, () -> {
            try {
                constructor.newInstance();
            } catch (InvocationTargetException e) {
                // Dobd tovább az eredeti kivételt, amit az InvocationTargetException tartalmaz.
                throw (RuntimeException) e.getCause();
            }
        });
    }

    @Test
    void testInditjatekInvalidChoice() {
        // Érvénytelen választás
        when(mockScanner.nextInt()).thenReturn(4); // Választás

        // Ellenőrizzük, hogy az IllegalArgumentException dobódik
        assertThrows(IllegalArgumentException.class, () -> {
            Main.inditjatek(mockScanner); // A metódus meghívása
        });
    }
}
