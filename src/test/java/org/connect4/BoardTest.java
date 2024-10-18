package org.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        board = new Board();
        player1 = new Player("Player 1", 'X');
        player2 = new Player("Player 2", 'O');
    }

    @Test
    void testDropToken() {
        assertTrue(board.dropToken(0, player1.getToken()), "Player 1-nek sikeresen le kell dobnia egy zsetont.");
        assertEquals('X', board.getBoard()[5][0], "Az alsó cellának az 0. oszlopban 'X'-nek kell lennie.");
        assertFalse(board.dropToken(7, player1.getToken()), "Érvénytelen oszlopba történő dobásnak sikertelennek kell lennie.");
    }

    @Test
    void testCheckHorizontalWin() {
        board.dropToken(0, player1.getToken());
        board.dropToken(1, player1.getToken());
        board.dropToken(2, player1.getToken());
        board.dropToken(3, player1.getToken());
        assertTrue(board.checkHorizontalWin(player1), "Player 1-nek vízszintes győzelmet kell elérnie.");
    }

    @Test
    void testCheckVerticalWin() {
        board.dropToken(0, player1.getToken());
        board.dropToken(0, player1.getToken());
        board.dropToken(0, player1.getToken());
        board.dropToken(0, player1.getToken());
        assertTrue(board.checkVerticalWin(player1), "Player 1-nek függőleges győzelmet kell elérnie.");
    }

    @Test
    void testCheckDiagonalWin() {
        // Pozitív lejtésű átlós győzelem ellenőrzése (\)
        board.dropToken(0, player1.getToken());
        board.dropToken(1, player2.getToken());
        board.dropToken(1, player1.getToken());
        board.dropToken(2, player2.getToken());
        board.dropToken(2, player2.getToken());
        board.dropToken(2, player1.getToken());
        board.dropToken(3, player2.getToken());
        board.dropToken(3, player2.getToken());
        board.dropToken(3, player2.getToken());
        board.dropToken(3, player1.getToken());

        assertTrue(board.checkDiagonalWin(player1), "Player 1-nek átlós győzelmet kell elérnie pozitív lejtésű irányban.");

        // Negatív lejtésű átlós győzelem ellenőrzése (/)
        board = new Board(); // Új tábla inicializálása
        board.dropToken(3, player1.getToken());
        board.dropToken(2, player2.getToken());
        board.dropToken(2, player1.getToken());
        board.dropToken(1, player2.getToken());
        board.dropToken(1, player2.getToken());
        board.dropToken(1, player1.getToken());
        board.dropToken(0, player2.getToken());
        board.dropToken(0, player2.getToken());
        board.dropToken(0, player2.getToken());
        board.dropToken(0, player1.getToken());

        assertTrue(board.checkDiagonalWin(player1), "Player 1-nek átlós győzelmet kell elérnie negatív lejtésű irányban.");
    }

    @Test
    void testIsFull() {
        assertFalse(board.isFull(), "A táblának kezdetben nem szabad telinek lennie.");

        // A tábla megtöltése
        for (int col = 0; col < 7; col++) {
            for (int i = 0; i < 6; i++) {
                board.dropToken(col, player1.getToken());
            }
        }
        assertTrue(board.isFull(), "A táblának telinek kell lennie, miután minden cellát feltöltöttünk.");
    }

    @Test
    void testLoadStateFromFile() throws IOException {
        String testFileName = "test_board_state.txt";
        // Teszt táblaállapot létrehozása megfelelő számú oszloppal (7)
        String boardState =
                "XOXOXOO\n" +
                        ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        ".......\n"; // Ügyeljünk rá, hogy minden sor pontosan 7 karakter hosszú legyen

        Files.write(Paths.get(testFileName), boardState.getBytes());

        assertTrue(board.loadStateFromFile(testFileName), "A játékállapotot sikeresen be kellene tölteni.");
        assertEquals('X', board.getBoard()[0][0], "A bal felső cellának 'X'-nek kell lennie.");
        assertEquals('O', board.getBoard()[0][1], "Az első sor második cellájának 'O'-nak kell lennie.");

        // Tesztfájl törlése
        new File(testFileName).delete();
    }

    @Test
    void testSaveStateToFile() throws IOException {
        String testFileName = "test_save_state.txt";
        board.dropToken(0, player1.getToken());
        board.dropToken(1, player2.getToken());

        board.saveStateToFile(testFileName);
        String savedState = new String(Files.readAllBytes(Paths.get(testFileName)));

        assertTrue(savedState.contains("X"), "A mentett állapotnak tartalmaznia kell Player 1 zsetonját.");
        assertTrue(savedState.contains("O"), "A mentett állapotnak tartalmaznia kell Player 2 zsetonját.");

        // Tesztfájl törlése
        new File(testFileName).delete();
    }

    @Test
    void testDropTokenColumnFull() {
        // Az oszlop megtöltése
        for (int i = 0; i < 6; i++) {
            assertTrue(board.dropToken(0, player1.getToken()), "A zsetont le kellene tudni dobni az oszlopba.");
        }

        // Próbálkozás egy újabb zseton ledobásával az 0. oszlopba (ami már tele van)
        assertFalse(board.dropToken(0, player1.getToken()), "A zsetont nem szabadna ledobni egy tele oszlopba.");
    }

    @Test
    void testPrintBoard() {
        // A standard kimenet átirányítása, hogy ellenőrizhessük a kiírt szöveget
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Egy zseton ledobása az 0. oszlopba
        board.dropToken(0, player1.getToken());

        // A tábla kiírása
        board.print();

        // Várt eredmény a tábla megjelenítésére (egy 'X' a bal alsó sarokban, és üres pontok máshol)
        String expectedOutput =
                ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        ".......\n" +
                        "X......\n";

        // Normalizáljuk a sorvégeket a teszt során
        String actualOutput = outContent.toString().replace("\r\n", "\n");

        assertEquals(expectedOutput, actualOutput, "A tábla kiírásának meg kell egyeznie a várt állapottal.");

        // A standard kimenet visszaállítása
        System.setOut(System.out);
    }
}
