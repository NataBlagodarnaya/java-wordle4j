package ru.yandex.practicum;

public class WordleNotGameException extends RuntimeException {
    public WordleNotGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public WordleNotGameException(String message) {
        super(message);
    }
}
