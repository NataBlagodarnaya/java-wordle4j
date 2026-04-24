package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {
    private final PrintWriter logger;
    private String correctAnswer;
    private String userAnswer;
    private int steps;
    static final int TOTAL_ATTEMPTS = 6;//количество попыток отгадать слово
    WordleDictionary dictionary;
    private final ArrayList<String> workingDictionaryList;
    private LinkedHashMap<String, String> history;

    public WordleGame(WordleDictionary dictionary, PrintWriter logger) {
        this.logger = logger;
        this.correctAnswer = dictionary.getRandomWord();
        this.userAnswer = null;
        this.steps = 0;
        this.dictionary = dictionary;
        this.workingDictionaryList = new ArrayList<>(dictionary.getWords());
        this.workingDictionaryList.remove(this.correctAnswer);
        this.history = new LinkedHashMap<>();
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public int getSteps() {
        return this.steps;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public void checkInput(String input) throws WordleGameException { //проверка строки, введенной пользователем
        if (input.isBlank()) {
            this.steps++;
            this.userAnswer = giveHint() + " - подсказка";
            logGameProgress();
            return;
        } else {
            input = input.trim().toLowerCase().replace("ё", "е");

            if (!input.matches("[а-я]{" + WordleDictionaryLoader.LETTERS_COUNT + "}")) {
                throw new WordleGameException("Это слово не подходит по условиям игры :(." +
                        "Должно быть существительное в именительном падеже из " +
                        WordleDictionaryLoader.LETTERS_COUNT + " русских букв.");
            }
            if (!dictionary.getWords().contains(input) && !input.equals(this.correctAnswer)) {
                throw new WordleGameException("Cлово " + input + " не подходит по условиям игры - его нет в словаре.");
            }
        }
        this.userAnswer = input;
        this.steps++;
        logGameProgress();
    }

    private void logGameProgress() { //логирование основных переменных игры
        logger.println("\n" + "Попытка: " + this.steps + "\n" + "Ответ игрока: " + this.userAnswer + "\n");
    }

    public String giveHint() { //даем подсказку
        if (this.workingDictionaryList.isEmpty() || this.steps == TOTAL_ATTEMPTS) {
            return this.correctAnswer;
        } else {
            return this.workingDictionaryList.get(new Random().nextInt(this.workingDictionaryList.size()));
        }
    }

    public boolean isCorrectAnswer() { //проверка был ли дан верный ответ
        if (this.userAnswer.substring(0, WordleDictionaryLoader.LETTERS_COUNT).equals(this.correctAnswer)) {
            return true;
        } else {
            return false;
        }
    }

    public LinkedHashMap<String, String> checkUserAnswer() { //проверка на сколько данный ответ совпадает с загаданным словом
        StringBuilder matchResult = new StringBuilder("-----");
        for (int i = 0; i < WordleDictionaryLoader.LETTERS_COUNT; i++) {
            if (this.userAnswer.charAt(i) == (this.correctAnswer.charAt(i))) {
                matchResult.setCharAt(i, '+');
                char iChar = this.userAnswer.charAt(i);
                int currentIndex = i;
                this.workingDictionaryList.removeIf(word -> word.charAt(currentIndex) != iChar);
            } else {
                if (this.correctAnswer.indexOf(this.userAnswer.charAt(i)) != -1) {
                    matchResult.setCharAt(i, '^');
                    char iChar = this.userAnswer.charAt(i);
                    int currentIndex = i;
                    this.workingDictionaryList.removeIf(word -> word.charAt(currentIndex) == iChar);
                    this.workingDictionaryList.removeIf(word -> !word.contains(String.valueOf(iChar)));
                } else {
                    String iChar = String.valueOf(this.userAnswer.charAt(i));
                    this.workingDictionaryList.removeIf(word -> word.contains(iChar));
                }
            }
        }
        this.history.put(this.userAnswer, matchResult.toString());
        return this.history;
    }

    public String printGameHistory() { // вывод истории проверок ответов
        StringBuilder sb = new StringBuilder();
        sb.append("История ходов: \n");
        for (Map.Entry<String, String> entry : this.history.entrySet()) {
            sb.append(entry.getKey());
            sb.append("\n");
            sb.append(entry.getValue());
            sb.append("\n");
        }
        return sb.toString();
    }
}
