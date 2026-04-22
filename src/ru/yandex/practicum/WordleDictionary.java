package ru.yandex.practicum;

import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private static List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public static List<String> getWords() {
        return words;
    }

    public String getRandomWord() {
        return words.get(new Random().nextInt(words.size()));
    }


}
