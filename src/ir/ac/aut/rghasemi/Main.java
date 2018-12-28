package ir.ac.aut.rghasemi;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = null;
        Vector<Vector<Integer>> graph  = new Vector<Vector<Integer>>();
        String casename = "TestCase9-N10000-k20-mu45";
        File file = new File("TestCase/"+casename+ "/network.txt");
        String prefile = new String("TestCase/"+casename+ "/community.txt");
        try {
            scanner = new Scanner((file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int min = 0;
        while (scanner.hasNext())
        {
            String index = scanner.next();
            if (Integer.valueOf(index) > min)
                min = Integer.valueOf(index);
        }
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i=0;i<=min;i++)
            graph.add(new Vector<>());
        int edges = 0;
        while (scanner.hasNext())
        {
            String index = scanner.next();
            String adj = scanner.next();
            int ind = Integer.valueOf(index);
            int ad = Integer.valueOf(adj);
            graph.get(ind).add(ad);
            edges ++;

        }

        for (int i=0;i<graph.size();i++)
        {
            System.out.print(i+ " ->");
            for (int j=0;j<graph.get(i).size();j++){
                System.out.print(graph.get(i).get(j) +"->");
            }
            System.out.println();
        }
        Scanner x= new Scanner(System.in);
        System.out.println("\n\n");
        System.out.println("Now Choose on of this options.");
        System.out.println("1 : Simple LPA ");
        System.out.println("2 : Node Influence");
        System.out.println("\n");
        int choice = x.nextInt();
        if (choice == 1 ) {
            Vector<Integer> predict = new Vector<>();
            float nmi = 0;
            LPA lp = new LPA();
            long t1 = System.currentTimeMillis();
            predict = lp.algorithm(graph);
            long t2 = System.currentTimeMillis();
            try {
                nmi = lp.NMI(predict, prefile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Final Similarity is : " + nmi);
            System.out.println("Whole Algorithm time taken : " + (t2 - t1) + "(ms)");
            System.out.println("has " + edges / 2 + " edges");
        }
        else {
            Vector<Integer> predict = new Vector<>();
            float nmi = 0;

            double alpha = 0;
            for (int i=100;i<=100;i++) {
                NodeInfluence graph1 = new NodeInfluence();
                long t1 = System.currentTimeMillis();
                graph1.kshell(graph);
                graph1.findNI(graph, alpha);
                graph1.sortNI();
                predict = graph1.algotithm(graph);
                long t2 = System.currentTimeMillis();
                try {
                    nmi = nmi +graph1.NMI(predict, prefile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Final Similarity is : " + nmi);
                System.out.println("Whole Algorithm time taken : " + (t2 - t1) + " (ms)");
                System.out.println("has " + edges / 2 + " edges");
                if (i%20 == 0)
                    alpha = alpha + 0.2;
            }
            System.out.println("Avg Final Similarity is : " + nmi);

        }
    }

}
