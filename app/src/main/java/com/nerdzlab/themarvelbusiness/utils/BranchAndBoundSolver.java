package com.nerdzlab.themarvelbusiness.utils;



import com.karumi.marvelapiclient.model.ComicDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by orcun on 12.02.2017.
 */

public class BranchAndBoundSolver extends KnapsackSolver {

    private class Node implements Comparable<Node> {

        public int h;
        List<ComicDto> taken;
        public double bound;
        public double value;
        public double weight;

        public Node() {
            taken = new ArrayList<ComicDto>();
        }

        public Node(Node parent) {
            h = parent.h + 1;
            taken = new ArrayList<ComicDto>(parent.taken);
            bound = parent.bound;
            value = parent.value;
            weight = parent.weight;
        }

        // Sort by bound
        public int compareTo(Node other) {
            return (int) (other.bound - bound);
        }

        public void computeBound() {
            int i = h;
            double w = weight;
            bound = value;
            ComicDto item;
            do {
                item = items.get(i);
                if (w + item.getPrices().get(0).getPrice() > capacity) break;
                w += item.getPrices().get(0).getPrice();
                bound += item.getPageCount();
                i++;
            } while (i < items.size());
            bound += (capacity - w) * (item.getPageCount() / item.getPrices().get(0).getPrice());
        }
    }

    public BranchAndBoundSolver(List<ComicDto> items, int capacity) {
        super(items, capacity);
    }


    @Override
    public KnapsackSolution solve() {

        Collections.sort(items, new Comparator<ComicDto>() {
            public int compare(ComicDto i1, ComicDto i2) {
                return Double.compare(i2.getPageCount()/i2.getPrices().get(0).getPrice(),
                        i1.getPageCount()/i1.getPrices().get(0).getPrice());
            }
        });

        Node best = new Node();
        Node root = new Node();
        root.computeBound();

        PriorityQueue<Node> q = new PriorityQueue<Node>();
        q.offer(root);

        while (!q.isEmpty()) {
            Node node = q.poll();

            if (node.bound > best.value && node.h < items.size() - 1) {

                Node with = new Node(node);
                ComicDto item = items.get(node.h);
                with.weight += item.getPrices().get(0).getPrice();

                if (with.weight <= capacity) {

                    with.taken.add(items.get(node.h));
                    with.value += item.getPageCount();
                    with.computeBound();

                    if (with.value > best.value) {
                        best = with;
                    }
                    if (with.bound > best.value) {
                        q.offer(with);
                    }
                }

                Node without = new Node(node);
                without.computeBound();

                if (without.bound > best.value) {
                    q.offer(without);
                }
            }
        }

        KnapsackSolution solution = new KnapsackSolution();
        solution.value = best.value;
        solution.weight = best.weight;
        solution.items = best.taken;
        solution.approach = "Using Branch and Bound the best feasible solution found";

        return solution;
    }
}
