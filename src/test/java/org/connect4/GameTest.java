package org.connect4;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Player player1;
    private Player player2;
    private Game game;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Játékos1", 'X');
        player2 = new Player("Játékos2", 'O');
        game = new Game(player1, player2, false);
    }

    @Test
    public void testGameInitialization() {
        boolean playAgainstAI = false;

        Game game = new Game(player1, player2, playAgainstAI);

        assertNotNull(game, "A Game objektum nem lehet null.");
        assertEquals(player1, game.getFirstPlayer(), "Az első játékos helytelen.");
        assertEquals(player2, game.getSecondPlayer(), "A második játékos helytelen.");
        assertFalse(game.isPlayingAgainstAI(), "A játék nem AI ellen kellene, hogy legyen.");
        assertEquals(player1, game.getCurrentPlayer(), "Az aktuális játékosnak az elsőnek kell lennie.");
    }

    @Test
    public void testSetupGameWithPlayers() {
        Scanner scanner = new Scanner("Játékos1\nJátékos2\n");
        Game game = Game.setupGame(1, scanner);

        assertNotNull(game, "A játék inicializálása sikertelen.");
        assertEquals("Játékos1", game.getFirstPlayer().getName(), "Az első játékos neve hibás.");
        assertEquals("Játékos2", game.getSecondPlayer().getName(), "A második játékos neve hibás.");
        assertFalse(game.isPlayingAgainstAI(), "Nem AI ellen kellene játszani.");
    }

    @Test
    public void testSetupGameWithAI() {
        Scanner scanner = new Scanner("Játékos1\n");
        Game game = Game.setupGame(2, scanner);

        assertNotNull(game, "A játék inicializálása AI módban sikertelen.");
        assertEquals("Játékos1", game.getFirstPlayer().getName(), "Az első játékos neve hibás.");
        assertEquals("AI", game.getSecondPlayer().getName(), "A második játékos neve AI kell legyen.");
        assertTrue(game.isPlayingAgainstAI(), "AI ellen kellene játszani.");
    }

    @Test
    public void testGetPlayerInput_ValidInput() {
        String input = "3\n"; // A játékos által megadott bemenet
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in); // Beállítjuk a bemenetet a System.in-re

        int column = game.getPlayerInput();

        assertEquals(3, column, "A kiválasztott oszlopnak 3-nak kell lennie.");
    }

    @Test
    public void testGetPlayerInput_InvalidInput() {
        String input = "abc\n2\n"; // Érvénytelen bemenet, majd érvényes
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        int column = game.getPlayerInput();

        assertEquals(2, column, "A kiválasztott oszlopnak 2-nek kell lennie.");
    }

    @Test
    public void testGetCurrentPlayerInput_PlayerTurn() {
        // Állítsuk be a bemenetet egy érvényes oszloppal
        String input = "3\n"; // A játékos által megadott bemenet
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in); // Beállítjuk a bemenetet a System.in-re

        int column = game.getCurrentPlayerInput();

        assertEquals(3, column, "A kiválasztott oszlopnak 3-nak kell lennie.");
    }

    @Test
    public void testGetCurrentPlayerInput_AITurn() {
        // Állítsuk be az AI játékot
        game = new Game(player1, new Player("AI", 'O'), true); // AI ellen játszunk

        // Mockoljuk a bemenetet
        String simulatedInput = "0\n"; // A kívánt oszlop (itt 0)
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        // Kérjük le az AI választását
        int column = game.getCurrentPlayerInput();

        assertTrue(column >= 0 && column < 7, "Az AI kiválasztott oszlopa érvényesnek kell lennie (0-6 között).");
    }

    @Test
    public void testSwitchPlayer() {
        // Kezdő állapot: első játékos
        assertEquals(player1, game.getCurrentPlayer(), "Kezdéskor az első játékosnak kell lennie.");

        // Játékos váltás
        game.switchPlayer();
        assertEquals(player2, game.getCurrentPlayer(), "A váltás után a második játékosnak kell lennie.");

        // Újabb játékos váltás
        game.switchPlayer();
        assertEquals(player1, game.getCurrentPlayer(), "A második váltás után ismét az első játékosnak kell lennie.");
    }
    @Test
    public void testStart_GameWon() {
        // Beállítjuk a táblát úgy, hogy az első játékos nyerjen
        game.getBoard().dropToken(0, player1.getToken());
        game.getBoard().dropToken(1, player1.getToken());
        game.getBoard().dropToken(2, player1.getToken());
        game.getBoard().dropToken(3, player1.getToken());

        // Mockoljuk a bemenetet a játékos választásához
        String input = "0\n"; // Érvényes oszlop
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Indítsuk el a játékot
        game.start();
    }

    @Test
    public void testStart_Draw() {
        // Beállítjuk a táblát úgy, hogy döntetlen legyen
        for (int col = 0; col < 7; col++) {
            game.getBoard().dropToken(col, player1.getToken());
            game.getBoard().dropToken(col, player2.getToken());
        }

        // Mockoljuk a bemenetet a játékos választásához
        String input = "0\n"; // Érvényes oszlop
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Indítsuk el a játékot
        game.start();
    }


    // Segédfüggvény, hogy ellenőrizzük, tartalmaz-e a kimenet szöveget
    private boolean outputContains(String text) {
        // Implementálni kell a kimenet rögzítését és a vizsgálatot
        // Például használhatod a System.out átirányítását, hogy elmentsd a kimenetet
        return false; // Ide kell implementálni a megfelelő logikát
    }

}
