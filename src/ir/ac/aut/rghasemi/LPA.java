package ir.ac.aut.rghasemi;

/**
 * Created by Roozbeh Ghasemi on 7/9/2018.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

public class LPA {
    private Vector <Integer> randomindexes = new Vector<>();
    Vector<Vector<Integer>> ci = new Vector<>();

    public Vector<Integer> algorithm(Vector <Vector<Integer>> graph ){
        Vector<Integer> num = new Vector<>();
        for (int i = 0; i < graph.size(); i++) {
            num.add(0);
            ci.add(new Vector<>());
            ci.get(i).add(i);
        }
        randomindexes = new Vector<>();
        for (int i = 0; i < graph.size(); i++) {
            randomindexes.add(0);
            randomindexes.set(i, i);
        }

        Vector<Integer> licalc = new Vector<>();
        int change = 0;
        while (change != 1) {
            change = 1;
            random_shuffle(randomindexes);
            for (int i = 1; i < randomindexes.size(); i++) {
                int realindex = randomindexes.get(i);
                Vector<Integer> adj = graph.get(randomindexes.get(i));
                num = new Vector<>();
                for (int ii = 0; ii < graph.size(); ii++) {
                    num.add(0);
                }
                for (int k = 0; k < adj.size(); k++) {
                    num.set(ci.get(adj.get(k)).get(ci.get(adj.get(k)).size() - 1), num.get(ci.get(adj.get(k)).get(ci.get(adj.get(k)).size() - 1)) + 1);
                }
                int mux = 0;
                int muxindex = 0;
                for (int k = 0; k < num.size(); k++) {
                    if (num.get(k) >= mux) {
                        mux = num.get(k);
                    }
                }
                licalc = new Vector<>();
                for (int k = 0; k < num.size(); k++) {
                    if (num.get(k) == mux) {
                        licalc.add(k);
                    }
                }
                if (licalc.size() == 1) {
                    if (!licalc.get(0) .equals( ci.get(realindex).get(ci.get(realindex).size() - 1))) {
                        ci.get(realindex).add(licalc.get(0));
                        change = 0;
                    }
                } else {


                    int random =  (int)(Math.random()*licalc.size());

                    if (!licalc.get(random) .equals( ci.get(realindex).get(ci.get(realindex).size() - 1))) {
                        ci.get(realindex).add(licalc.get(random));
                        change = 0;
                    }

                }

            }

        }

        Vector <Integer> finalvec = new Vector<>();
        for (int i=0;i<ci.size();i++)
        {
            finalvec.add(0);
            finalvec.set(i,ci.get(i).get(ci.get(i).size()-1));
        }
        for (int i=0;i<ci.size();i++)
        {
            System.out.println(i + " -> "+finalvec.get(i));
        }
        return finalvec;
    }
    public Vector<Integer>  random_shuffle(Vector<Integer> input) {
        for (int i = 1; i < input.size(); i++) {
            int random = i + (int) (Math.random() * ((input.size() - 1 - i) + 1));
            int temp = input.get(random);
            input.set(random, input.get(i));
            input.set(i, temp);
        }
        return input;

    }
    public float NMI(Vector<Integer> Prediction, String TrueCommunityPathTXT )throws Exception {
        Vector<Integer> TrueLabel = new Vector<Integer>();
        int countGuess = 0, countGold = 0;
        float NMI = 0, up = 0, down = 0;
        int n = 0;
        TrueLabel.add(0);
        BufferedReader br = new BufferedReader(new FileReader(TrueCommunityPathTXT));
        String line = br.readLine();
        while (line != null) {
            String[] parts = line.split("\\s+");
            TrueLabel.add(Integer.parseInt(parts[1]));
            n++;
            line = br.readLine();
        }
        br.close();
        if (n != Prediction.size() - 1)
            return -1;
        Hashtable<Integer, Integer> temp = new Hashtable<Integer, Integer>();
        int k = 1;
        for (int i = 1; i <= n; i++) {
            if (temp.containsKey((Integer) Prediction.get(i)))
                Prediction.set(i, temp.get(Prediction.get(i)));
            else {
                temp.put(Prediction.get(i), k);
                Prediction.set(i, temp.get(Prediction.get(i)));
                k++;
            }
        }
        for (int i = 1; i <= n; i++) {
            if (Prediction.get(i) > countGuess)
                countGuess = Prediction.get(i);
            if (TrueLabel.get(i) > countGold)
                countGold = TrueLabel.get(i);
        }
        float NRow[] = new float[countGold];
        float NCol[] = new float[countGuess];
        float matrix[][] = new float[countGold][countGuess];
        for (int i = 0; i < countGold; i++)
            matrix[i] = new float[countGuess];
        for (int i = 1; i <= n; i++) {
            matrix[TrueLabel.get(i) - 1][Prediction.get(i) - 1]++;
            NRow[TrueLabel.get(i) - 1]++;
            NCol[Prediction.get(i) - 1]++;
        }
        for (int i = 0; i < countGold; i++)
            if (NRow[i] != 0)
                down += NRow[i] * Math.log10((NRow[i] / (float) (n)))/Math.log10(2);
        for (int i = 0; i < countGuess; i++)
            if (NCol[i] != 0)
                down += NCol[i] * Math.log10(NCol[i] / (float) (n))/Math.log10(2);
        for (int i = 0; i < countGold; i++)
            for (int j = 0; j < countGuess; j++)
                if (matrix[i][j] != 0)
                    up += matrix[i][j] * (Math.log10((matrix[i][j] * (float) (n) / ((NRow[i] * NCol[j]))))/Math.log10(2));
        up *= (float) -2;
        NMI = up / down;
        return NMI;
    }



}
