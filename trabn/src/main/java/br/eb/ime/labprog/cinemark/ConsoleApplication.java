package br.eb.ime.labprog.cinemark;

public class ConsoleApplication {
    public static void main(String[] args) {
        for (Film film: Film.getFilms()) {
            System.out.println("Film " + film.getId() + ": " + film.getTitle() + ", " + film.getGenre() + ", img: " + film.getImageUrl());
        }
    }
}
