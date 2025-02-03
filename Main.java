import java.io.*;
import java.util.*;

public class Main {
    private static final String[] COLORS = {
            "Not a color", "RED", "BLUE", "GREEN", "YELLOW", "WHITE", "BLACK"
    };
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        //generate permutation
        List<int[]> permutations = readCSV();

        //print OG guess
        System.out.println("-------------Guess 1---------------");
        int[] guess = new int[] {1, 1, 2, 2};
        printOutGuess(guess);
        int guessCount = 1;

        while(true) {
            //get pegs
            // index 0 will be white pegs
            //index 1 will be black pegs
            int [] pegs = getPegs(scan);

            if(pegs[1] == 4) {
                System.out.println("yay we did it in: " + guessCount + " rounds");
                return;
            }
            Iterator<int[]> iterator = permutations.iterator();
            while (iterator.hasNext()) {
                int[] potentialAnswer = iterator.next();
                int[] pegsFromSimulation = simulateRound(guess, potentialAnswer);
                if (!Arrays.equals(pegsFromSimulation, pegs)) {
                    iterator.remove();
                }
            }
            System.out.println();
            for(int[] p : permutations) {
                System.out.print(Arrays.toString(p) + ", ");
            }



            //print out the guess
            if(permutations.size() == 1) {
                printOutGuess(permutations.get(0));
                continue;

            }
            int[] nextGuess = miniMax(permutations);
            for (int num : nextGuess) {
                if (num < 1 || num >= COLORS.length) {
                    System.err.println("Error: Generated an invalid guess: " + Arrays.toString(nextGuess));
                    System.exit(1);
                }
            }
            guess = nextGuess;
            guessCount = guessCount + 1;
            System.out.println("-------------Guess " + guessCount + " ---------------");
            printOutGuess(nextGuess);
        }




    }
    public static int[] miniMax(List<int[]> T) {
        int lowestScore = Integer.MAX_VALUE;
        int[] lowestGuess = new int[4];
        for(int i = 0; i < T.size(); i++) {
            int[] g = T.get(i);
            HashMap<String, Integer> littleTable = new HashMap<>();
            String maxKey = null;
            for(int j = 0; j < T.size(); j++) {
                int[] c = T.get(j);
                int[] s = simulateRound(g, c);
                String key = Arrays.toString(s);
                if(littleTable.containsKey(key)) {
                    littleTable.put(key, littleTable.get(key) + 1);
                } else {
                    littleTable.put(key, 1);
                }
                if (maxKey == null || littleTable.get(key) > littleTable.get(maxKey)) {
                    maxKey = key;
                }
            }
            if (maxKey != null && littleTable.get(maxKey) < lowestScore) {
                lowestScore = littleTable.get(maxKey);
                lowestGuess = g;
            }

        }
        return lowestGuess;
    }
    public static int[] simulateRound(int[] guess, int[] actualCode) {
        int black = 0;
        int white = 0;

        Map<Integer, Integer> colorCount = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            colorCount.put(actualCode[i],
                    colorCount.getOrDefault(actualCode[i], 0) + 1);
        }

        boolean[] isBlack = new boolean[4];
        for (int i = 0; i < 4; i++) {
            if (guess[i] == actualCode[i]) {
                black++;
                isBlack[i] = true;
                colorCount.put(guess[i], colorCount.get(guess[i]) - 1);
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!isBlack[i]) {  
                int color = guess[i];
                if (colorCount.containsKey(color) && colorCount.get(color) > 0) {
                    white++;
                    colorCount.put(color, colorCount.get(color) - 1);
                }
            }
        }

        return new int[]{white, black};
    }
    public static int[] getPegs(Scanner scan) {
        int[] pegArray = new int[2];
        while (true) {
            try {
                System.out.println("How many white pegs?");
                pegArray[0] = Integer.parseInt(scan.nextLine());

                System.out.println("How many black pegs?");
                pegArray[1] = Integer.parseInt(scan.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter integers.");
            }
        }
        return pegArray;
    }
    public static void printOutGuess(int[] guess) {
        System.out.print("GUESS: ");
        for(int i = 0; i < 4; i++) {
            System.out.print(COLORS[guess[i]] + "(" + guess[i] + "), ");
        }
        System.out.println();

    }
    public static List<int[]> readCSV() {
        List<int[]> permutations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("permutations.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int[] permutation = new int[values.length];
                for (int i = 0; i < values.length; i++) {
                    permutation[i] = Integer.parseInt(values[i]);
                }
                permutations.add(permutation);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return permutations;
    }

}
