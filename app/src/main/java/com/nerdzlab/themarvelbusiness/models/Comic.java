package com.nerdzlab.themarvelbusiness.models;

import java.util.Date;

/**
 * Created by orcun on 08.02.2017.
 */

public class Comic {
    private int id;
    private int page_count;
    private float price;

    public Comic(int id, int page_count, float price) {
        this.id = id;
        this.page_count = page_count;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id=" + id +
                ", page_count=" + page_count +
                ", price=" + price +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
