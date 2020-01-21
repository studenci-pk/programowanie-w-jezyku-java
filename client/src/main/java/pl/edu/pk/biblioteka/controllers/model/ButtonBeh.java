package pl.edu.pk.biblioteka.controllers.model;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import pl.edu.pk.biblioteka.data.BookDto;
import pl.edu.pk.biblioteka.data.Moveable;

public class ButtonBeh {
    private String text;
    private Moveable move;

    public ButtonBeh(String text, Moveable move) {
        this.text = text;
        this.move = move;
    }

    public void move(BookDto book) {
        move.move(book);
    }

    public String getText() {
        return text;
    }
}
