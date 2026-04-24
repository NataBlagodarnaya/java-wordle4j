package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private WordleGame game;
    private PrintWriter testLogger = new PrintWriter(System.out, true);

    @BeforeEach
    public void beforeEach() {
        List<String> testWords = new ArrayList<>();
        testWords.add("арбуз");
        testWords.add("топор");
        testWords.add("экран");
        testWords.add("ротор");
        testWords.add("копье");
        WordleDictionary testDictionary = new WordleDictionary(testWords);
        List<String> initialDictionary = List.copyOf(testDictionary.getWords());
        game = new WordleGame(testDictionary, testLogger);
        game.setCorrectAnswer("арбуз");
    }

    @Test
    void testHintIsCorrect() {//дается правильная подсказка
        game.setUserAnswer("топор");
        game.checkUserAnswer();//----^
        assertFalse(game.giveHint().contains("т"), "Не должно быть буквы т");
        assertFalse(game.giveHint().contains("о"), "Не должно быть буквы о");
        assertFalse(game.giveHint().contains("п"), "Не должно быть буквы п");
        assertTrue(game.giveHint().contains("р"), "Должна быть буква р");
        assertNotEquals('р', game.giveHint().charAt(4), "Буква р не должна быть последней");
    }

    @Test
    void testValidInputFromFullDictionary() throws WordleGameException {//можно ввести любое слово из первоначаального словаря
        game.checkInput("топор");
        game.checkUserAnswer();//----^, все слова с окончанием 'р' удалятся из изменямого словаря игры
        game.checkInput("ротор");
        assertEquals("ротор", game.getUserAnswer());
    }

    @Test
    void testVictoryConditionOnCorrectWord() {//угадывание
        game.setUserAnswer("арбуз");
        assertTrue(game.isCorrectAnswer());
    }

    @Test
    void testInputNormalization() throws WordleGameException {
        game.checkInput("  Копьё");//нормализация большой буквы, пробелы, ё
        assertEquals("копье", game.getUserAnswer());
    }
}