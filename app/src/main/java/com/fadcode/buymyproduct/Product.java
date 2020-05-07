package com.fadcode.buymyproduct;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Product implements Serializable {

    private String id;
    private String title;
    private String filename;

    public Product(String id, String title, String filename) {
        this.id = id;
        this.title = title;
        this.filename = filename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @NotNull
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
