package pl.edu.pk.biblioteka.controllers.model;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import pl.edu.pk.biblioteka.data.Book;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Item {
    private static final Logger logger = Logger.getLogger(Item.class.getName());
    private String title;
    private String author;
    private String description;

    private Item() {

    }

    public Item(String title, String author, String description) {
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static Item valueOf(Book book) {
        Item i = new Item();

//        i.setAuthor(String.valueOf(book.getAuthorId()));
        i.setAuthor(String.valueOf(book.getAuthor()));
        i.setTitle(String.valueOf(book.getTitle()));
        i.setDescription(book.getKeywords() + ":" + String.valueOf(book.getKeywords()));

        return i;
    }

    public static Item valueOf(JSONObject object) {
        Item i = new Item();

        Method[] methods = Item.class.getMethods();
        object.keys().forEachRemaining(s -> {
            for (Method m: methods) {
                if (m.getName().equalsIgnoreCase(String.format("set%s", s))) {
                    try {
                        m.invoke(i, object.get(s));
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        logger.error(e);
                    }
                }
            }
        });

        return i;
    }
}