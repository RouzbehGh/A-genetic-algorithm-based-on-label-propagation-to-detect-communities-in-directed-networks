package ir.ac.aut.rghasemi;

/**
 * Created by Roozbeh Ghasemi on 7/9/2018.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.Vector;

public class NodeInfluence {

    Vector<Integer> kshellvec  = new Vector<>();
    Vector<Double> NIvec  = new Vector<>();
    Vector<Integer > sortindexes = new Vector<>();
    Vector<Vector<Integer>> ci = new Vector<>();
    public NodeInfluence( )
    {

    }
    public Vector<Vector<Integer>> kshell(Vector<Vector<Integer>> graph){
        int x = 1;

        Vector<Vector<Integer>> tempgraph  = new Vector<>();

        for(int i=0;i<graph.size();i++)
        {
            tempgraph.add(new Vector<>());
            for(int j=0;j<graph.get(i).size();j++){
                tempgraph .get(i).add( graph.get(i).get(j));
            }
        }

        Vector <Integer>k1 = new Vector();
        for (int z=0;z<graph.size();z++){
            k1.add(0);
        }

        int flag = 0;
        while(flag!=1) {
            int change = 0;
            flag = 1;
            while (change != 1) {
                change = 1;
                for (int i = 1; i < tempgraph.size(); i++) {
                    if (tempgraph.get(i).size() <= x ) {
                        if (k1.get(i)==0)
                            k1.set(i,x);

                        for (int j = 0; j < tempgraph.get(i).size(); j++) {
                            change = 0;
                            int t = tempgraph.get(i).get(j);
                            for (int k = 0; k < tempgraph.get(t).size(); k++) {
                                if (tempgraph.get(t).get(k) == i)
                                    tempgraph.get(t).remove(k);
                            }
                            tempgraph.get(i).remove(j);
                        }

                    }
                }
            }
            x++;

            for (int i=0;i<tempgraph.size();i++)
            {
                if (tempgraph.get(i).size()!=0)
                    flag =0;
            }
        }
        kshellvec = k1;
        return graph;
    }
    public void findNI(Vector<Vector<Integer>> graph,double alpha){

        Vector <Double> NI = new Vector<Double>();
        for (int i=0;i<graph.size();i++)
            NI.add(0.0);


        for (int i=0;i<graph.size();i++)
        {
            double sum = 0;
            for (int k=0;k<graph.get(i).size();k++)
            {

                sum = sum + (kshellvec.get( graph.get(i).get(k))+0.0)/graph.get(graph.get(i).get(k)).size();

            }
            NI.set(i, kshellvec.get(i) + (alpha*sum));
        }
        NIvec = NI;
    }
    public void sortNI(){
        Vector<Integer > indexes = new Vector<>();
        Vector<Double > NItemp = new Vector<>();
        Vector<Integer > indexes2 = new Vector<>();
        for (int k=0;k<NIvec.size();k++)
        {
            NItemp.add(0.0);
            indexes.add(k);
            indexes2.add(0);
            NItemp.set(k,NIvec.get(k)) ;
        }
        for (int i=1;i<NIvec.size();i++){
            for (int j=i+1;j<NIvec.size();j++){
                if (NItemp.get(i)>=NItemp.get(j))
                {

                    Double temp = NItemp.get(i);
                    NItemp.set(i,NItemp.get(j));
                    NItemp.set(j,temp);
                    if (NItemp.get(i)!=NItemp.get(j)) {
                        int temp2 = indexes.get(i);
                        indexes.set(i, indexes.get(j));
                        indexes.set(j, temp2);
                    }
                }
            }
        }
        for (int i=1;i<indexes.size();i++)
        {
            indexes2.set(i,indexes.get(indexes.size()-i));
        }
        sortindexes = indexes2;
    }
    public Vector<Integer> algotithm(Vector <Vector<Integer>> graph){
        Vector<Integer> num = new Vector<>();
        for (int i=0;i<graph.size();i++)
        {
            num.add(0);
            ci.add(new Vector<>());
            ci.get(i).add(i);
        }


        Vector<Integer> licalc = new Vector<>();
        int change = 0;
        while (change != 1) {
            change = 1;
            for (int i = 1; i < sortindexes.size(); i++) {
                int realindex = sortindexes.get(i);
                Vector<Integer> adj = graph.get(sortindexes.get(i));
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
                    if (!licalc.get(0) .equals(ci.get(realindex).get(ci.get(realindex).size() - 1))) {
                        ci.get(realindex).add(licalc.get(0));
                        change = 0;
                    }
                } else {

                    Double maxsum = 0.0;
                    int maxlable = 0;
                    for (int v = 0; v < licalc.size(); v++) {
                        Double sum = 0.0;
                        for (int k = 0; k < adj.size(); k++) {

                            if (ci.get(adj.get(k)).get(ci.get(adj.get(k)).size() - 1) .equals( licalc.get(v))) {
                                sum = sum + (NIvec.get(adj.get(k)) + 0.0) / graph.get(adj.get(k)).size();
                            }

                        }
                        if (maxsum < sum) {
                            maxsum = sum;
                            maxlable = v;
                        }
                    }

                    if (!licalc.get(maxlable) .equals( ci.get(realindex).get(ci.get(realindex).size() - 1))) {
                        ci.get(realindex).add(licalc.get(maxlable));
                        change = 0;
                    }

                }

            }
            for (int i=0;i<graph.size();i++)
            {
                System.out.print(i+ " ->");
                for (int j=0;j<ci.get(i).size();j++){
                    System.out.print(ci.get(i).get(j) +"->");
                }
                System.out.println();
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
