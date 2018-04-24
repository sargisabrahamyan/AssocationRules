import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static String DATA_FILE = "retail.dat";
    public static String RESOURCES = "resources";
    public static double POPULARITY = 0.6;
    Set<Set<Integer>> data;
    Set<Integer> c1;
    /**
     * Loads data file from the given path
     * Assuming that each line is a new transaction
     * @param path of the data file
     * @return List of transaction where transaction is a list
     */
    private Set<Set<Integer>> loadData(Path path) {
         data = new HashSet<>();
        try (FileReader fr = new FileReader(path.toString());
             BufferedReader br = new BufferedReader(fr)) {
            String line = br.readLine();
            while(line != null) {
                Set<Integer> current = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt)
                        .collect(HashSet::new, HashSet::add, HashSet::addAll);
                data.add(current);
                line = br.readLine();
            }
        } catch (IOException e) {
        }
        return data;
    }

    /**
     * Gets unique item set from given data
     * @return
     */
    private Set<Integer> getUniqueItems() {
        Set<Integer> c1 = new HashSet<>();
        for (Set<Integer> transaction : data) {
            c1.addAll(transaction);
        }
        System.out.println(c1.size());
        return c1;
    }
    private Set<Set<Integer>> combination(int k, Set<Integer> c1) {
        Set<Set<Integer>> nextColl = new HashSet<>();
        if(k == 1 ) {
            // Return list of one element sets
            for(Integer c : c1) {
                Set<Integer> s = new HashSet<>();
                s.add(c);
                nextColl.add(s);
            }
            return nextColl;
        }
        Set<Set<Integer>> prevColl  = combination(k-1, c1);

            for (Integer i : c1) {
                for(Set<Integer> prevCollItem : prevColl) {
                if (!prevCollItem.contains(i)){
                    Set<Integer> nextCollItem = new HashSet<>(prevCollItem);
                    nextCollItem.add(i);
                    nextColl.add(nextCollItem);
                }
            }
        }
        return nextColl;
    }

    private Set<Set<Integer>> findPopular(Set<Set<Integer>> combination, double popularity) {
        Set<Set<Integer>> popular = new HashSet<>();
        for(Set<Integer> group : combination) {
            int k = 0;
            for(Set<Integer>  transaction : data) {
                if (transaction.contains(group)) {
                    k++;
                }
            }
            if (k/data.size() >= popularity) {
                popular.add(group);
            }
        }
        return popular;
    }

    public static void main(String args[] ) {
        Main m = new Main();
        Path path = Paths.get(RESOURCES, DATA_FILE);
        m.loadData(path);
        Set<Integer> c1 = m.getUniqueItems();
        Set<Set<Integer>> combinationList = m.combination(3, c1);
        m.findPopular(combinationList, POPULARITY);
        System.out.println(combinationList);
    }

}

