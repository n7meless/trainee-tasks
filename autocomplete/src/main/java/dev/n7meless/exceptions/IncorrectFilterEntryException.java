package dev.n7meless.exceptions;

public class IncorrectFilterEntryException extends Exception {
    public IncorrectFilterEntryException() {
        System.out.println("Введен некорректный фильтр. Попробуйте еще раз.");
    }
}
