package com.nerdzlab.themarvelbusiness.models;

import com.karumi.marvelapiclient.model.ComicDto;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by orcun on 08.02.2017.
 */

public class Comic extends ComicDto {

    double ratio;
    public double page;
    public double price;


    public static Comparator<Comic> byRatio() {
        return new Comparator<Comic>() {
            public int compare(Comic i1, Comic i2) {
                return Double.compare(i2.getRatio(), i1.getRatio());
            }
        };
    }


    public double getRatio() {
        return page / price;
    }
}
