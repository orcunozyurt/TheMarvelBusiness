package com.nerdzlab.themarvelbusiness.utils;


import com.karumi.marvelapiclient.model.ComicDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by orcun on 12/02/2017.
 */

public class DynamicProgrammingSolver extends KnapsackSolver {

    private double[][] table;

    public DynamicProgrammingSolver(List<ComicDto> items, int capacity) {
        super(items, capacity);
    }

    @Override
    public KnapsackSolution solve() {


        table = new double[capacity + 1][items.size()];

        for (int j = 0; j < capacity + 1; j++)
            for (int i = 0; i < items.size(); i++)
                table[j][i] = -1;

        getCell(capacity, items.size() - 1);

        KnapsackSolution best = traceTable();

        best.approach = "Dynamic Programming solution";
        return best;
    }

    // Traces back table
    private KnapsackSolution traceTable() {

        KnapsackSolution best = new KnapsackSolution();
        best.items = new ArrayList<ComicDto>();

        int i = items.size() - 1, j = capacity;

        while (i >= 0) {
            ComicDto item = items.get(i);

            double without = i == 0 ? 0 : table[j][i - 1];

            if (table[j][i] != without) {
                best.items.add(item);
                best.value += item.getPageCount();
                best.weight += item.getPrices().get(0).getPrice();
                j -= (int) item.getPrices().get(0).getPrice();
            }

            i--;
        }

        return best;
    }

    // Uses recursion with memoization
    private double getCell(int j, int i) {

        if (i < 0 || j < 0) return 0;
        ComicDto item = items.get(i);

        double with, without, cell = table[j][i];

        // If not memoized
        if (cell == -1) {

            if (item.getPrices().get(0).getPrice() > j) with = -1;
            else with = item.getPageCount() + getCell(j - (int) item.getPrices().get(0).getPrice(), i - 1);
            without = getCell(j, i - 1);

            cell = Math.max(with, without);

            table[j][i] = cell; // Memoize
        }

        return cell;
    }
}
