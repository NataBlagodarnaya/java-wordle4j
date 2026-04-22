package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*
в главном классе нам нужно:
                            создать лог-файл (он должен передаваться во все классы)
                            создать загрузчик словарей WordleDictionaryLoader
                            загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
                            затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static Scanner scanner = new Scanner(System.in);
    private static WordleGame game;
    private static final String dictionaryFileName = "words_ru.txt";

    public static void main(String[] args) {
        PrintWriter logger = null;
        try {
            Path logPath = createLog(dictionaryFileName);
            logger = new PrintWriter(new FileWriter(logPath.toFile(), true));
        } catch (Exception e) {
            // если лог не создается то пользователь продолжает игру просто она не логируется
        }
        try {
            runGame(dictionaryFileName, logger);
        } catch (Exception e) {
            System.out.println("Игра сломалась :(");
            if (logger != null) {
                e.printStackTrace(logger);
            }
        } finally {
            if (logger != null) logger.close();
        }
    }

    public static void runGame(String DICTIONARY_FILE_NAME, PrintWriter logger) throws
            IOException, WordleNotGameException {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        game = new WordleGame(loader.load(DICTIONARY_FILE_NAME), logger);
        System.out.println("Привет! Я загадал слово из словаря. Это существительное в именительном падеже из " +
                WordleDictionaryLoader.LETTERS_COUNT + " русских букв.");
        System.out.println("_".repeat(25));
        while (game.getSteps() < game.TOTAL_ATTEMPTS) {
            System.out.println("У тебя есть " + (game.TOTAL_ATTEMPTS - game.getSteps()) +
                    " попыток чтобы отгадать слово.");
            System.out.println("Введи слово или нажми <Enter> чтобы использовать подсказку.");
            try {
                String input = scanner.nextLine();
                game.checkInput(input);

                if (game.isCorrectAnswer()) {
                    System.out.println("ПОБЕДА! Это было слово: " + game.getCorrectAnswer());
                    return;
                } else {
                    game.checkUserAnswer();
                    System.out.println(game.printGameHistory());
                }
            } catch (WordleGameException e) {
                System.out.println(e.getMessage());//выводим сообщение игроку чтобы он понимал что не так с ответом
                System.out.println("_".repeat(25));
            }
        }
        if (game.getSteps() == game.TOTAL_ATTEMPTS) {
            System.out.println("ПОРАЖЕНИЕ :(. Ваши попытки отгадать слово закончились. Это было слово: " +
                    game.getCorrectAnswer());
        }
    }

    public static Path createLog(String DICTIONARY_FILE_NAME) throws IOException {
        String dictionaryPath = Path.of(DICTIONARY_FILE_NAME).toAbsolutePath().getParent().toString();
        Path logPath = Paths.get(dictionaryPath, "log.txt");
        if (!Files.exists(logPath)) {
            Files.createFile(logPath);
        }
        return logPath;
    }
}
