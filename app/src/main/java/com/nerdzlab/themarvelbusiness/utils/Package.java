package com.nerdzlab.themarvelbusiness.utils;

import com.karumi.marvelapiclient.model.ComicDto;
import com.nerdzlab.themarvelbusiness.models.Comic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by orcun on 10.02.2017.
 */
// Naive Recursive solution

public class Package {


    static List<ComicDto> my_pack;

    public static int fillPackage(float budget, List<ComicDto> Comic, List<ComicDto> optimalChoice, int n){
        //base case
        if(n == 0 || budget == 0)
            return 0;

        if(Comic.get(n-1).getPrices().get(0).getPrice() > budget) {
            List<ComicDto> subOptimalChoice = new ArrayList<>();
            int optimalCost =fillPackage(budget, Comic, subOptimalChoice, n-1);
            optimalChoice.addAll(subOptimalChoice);
            return optimalCost;
        }
        else{
            List<ComicDto> includeOptimalChoice = new ArrayList<>();
            List<ComicDto> excludeOptimalChoice = new ArrayList<>();
            int include_cost = Comic.get(n-1).getPageCount() +
                    fillPackage((budget-Comic.get(n-1).getPrices().get(0).getPrice()),
                            Comic,
                            includeOptimalChoice, n-1);
            int exclude_cost = fillPackage(budget, Comic, excludeOptimalChoice, n-1);
            if(include_cost > exclude_cost){
                optimalChoice.addAll(includeOptimalChoice);
                optimalChoice.add(Comic.get(n - 1));
                return include_cost;
            }
            else{
                optimalChoice.addAll(excludeOptimalChoice);
                return exclude_cost;
            }
        }
    }

    /*public static void main(String args[]) {
        ArrayList<Comic> ComicList = new ArrayList<>();
        ComicList.add(new Comic(2, 1));
        ComicList.add(new Comic(5, 6));
        ComicList.add(new Comic(3, 2));
        ComicList.add(new Comic(4, 4));
        ComicList.add(new Comic(7, 7));

        printOptimalChoice(ComicList, 9);
        printOptimalChoice(ComicList, 10);
        printOptimalChoice(ComicList, 11);
    }*/

    public static void printOptimalChoice(List<ComicDto> ComicList, float budget) {
        my_pack = new ArrayList<>();
        fillPackage(budget, ComicList, my_pack, ComicList.size());
        System.out.println("Best choice for budget: " + budget);
        for(int i = 0; i < my_pack.size(); i++) {
            System.out.println(my_pack.get(i));
        }
    }
}
