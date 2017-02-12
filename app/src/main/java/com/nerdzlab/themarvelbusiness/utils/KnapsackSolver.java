package com.nerdzlab.themarvelbusiness.utils;

import com.karumi.marvelapiclient.model.ComicDto;
import com.nerdzlab.themarvelbusiness.models.Comic;

import java.util.List;

/**
 * Created by orcun on 12/02/2017.
 */

public abstract class KnapsackSolver {

    protected List<ComicDto> items;
    protected int capacity;

    protected KnapsackSolver(List<ComicDto> items, int capacity) {
        this.items = items;
        this.capacity = capacity;
    }

    public abstract KnapsackSolution solve();

    public double getWeight(List<ComicDto> items) {
        double weight = 0;
        for (ComicDto item : items) {
            weight += item.getPrices().get(0).getPrice();
        }
        return weight;
    }

    public double getValue(List<ComicDto> items) {
        double value = 0;
        for (ComicDto item : items) {
            value += item.getPageCount();
        }
        return value;
    }
}
