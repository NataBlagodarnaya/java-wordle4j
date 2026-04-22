package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    static final int LETTERS_COUNT = 5;//количество букв в загаданном слове

    public WordleDictionary load (String filename) throws IOException, WordleNotGameException {
        List<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase().replace("ё", "е");
                if (!line.isBlank() && line.length() == LETTERS_COUNT && line.matches("[а-я]{" +
                        LETTERS_COUNT + "}")) {
                    words.add(line);
                }
            }
            if (words.isEmpty()) {
                throw new WordleNotGameException("Словарь пуст! Невозможно начать игру.");
            }
        }
        return new WordleDictionary(words);
    }
}
