package com.nerdzlab.themarvelbusiness.utils;

import com.karumi.marvelapiclient.model.ComicDto;

import java.util.List;

/**
 * Created by orcun on 12/02/2017.
 */

public class KnapsackSolution {

    String approach;
    public List<ComicDto> items;
    public double weight;
    public double value;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(approach);
        builder.append(": ");
        builder.append(value);
        builder.append(" ");
        builder.append(weight);
        builder.append("\n");



        for (ComicDto item : items) {
            builder.append(item.getTitle());
            builder.append(" ");
            builder.append("PRICE:" + item.getPrices().get(0).getPrice());
            builder.append("PC:" + item.getPageCount());
        }

        return builder.toString();
    }

    public List<ComicDto> getItems()
    {
        return items;
    }

    public double getWeight() {
        double weight = 0;
        for (ComicDto item : items) {
            weight += item.getPrices().get(0).getPrice();
        }
        return weight;
    }

    public double getValue() {
        double value = 0;
        for (ComicDto item : items) {
            value += item.getPageCount();
        }
        return value;
    }
}
