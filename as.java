import java.math.BigInteger;
import java.util.*;

public class as {

    static BigInteger decodeValue(String base, String value) {
        BigInteger bigBase = BigInteger.valueOf(Integer.parseInt(base));
        BigInteger result = BigInteger.ZERO;
        for (char ch : value.toLowerCase().toCharArray()) {
            int digit = (ch >= 'a') ? (ch - 'a' + 10) : (ch - '0');
            result = result.multiply(bigBase).add(BigInteger.valueOf(digit));
        }
        return result;
    }

    static BigInteger lagrangeAt0(long[] xs, BigInteger[] ys) {
        int k = xs.length;
        BigInteger secretNum = BigInteger.ZERO;
        BigInteger secretDen = BigInteger.ONE;

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(xs[i]);
            BigInteger termNum = ys[i];
            BigInteger termDen = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = BigInteger.valueOf(xs[j]);
                termNum = termNum.multiply(xj.negate());
                termDen = termDen.multiply(xi.subtract(xj));
            }

            BigInteger newNum = secretNum.multiply(termDen).add(termNum.multiply(secretDen));
            BigInteger newDen = secretDen.multiply(termDen);
            BigInteger g = newNum.abs().gcd(newDen.abs());
            secretNum = newNum.divide(g);
            secretDen = newDen.divide(g);
        }

        return secretNum.divide(secretDen);
    }

    static List<int[]> combinations(int n, int k) {
        List<int[]> result = new ArrayList<>();
        combinationsHelper(n, k, 0, new int[k], 0, result);
        return result;
    }

    static void combinationsHelper(int n, int k, int start, int[] current, int idx, List<int[]> result) {
        if (idx == k) { result.add(current.clone()); return; }
        for (int i = start; i < n; i++) {
            current[idx] = i;
            combinationsHelper(n, k, i + 1, current, idx + 1, result);
        }
    }

    static BigInteger findSecret(long[] allX, BigInteger[] allY, int threshold) {
        Map<BigInteger, Integer> freq = new HashMap<>();
        for (int[] combo : combinations(allX.length, threshold)) {
            long[] xs = new long[threshold];
            BigInteger[] ys = new BigInteger[threshold];
            for (int i = 0; i < threshold; i++) {
                xs[i] = allX[combo[i]];
                ys[i] = allY[combo[i]];
            }
            freq.merge(lagrangeAt0(xs, ys), 1, Integer::sum);
        }
        return freq.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public static void main(String[] args) {

        // TEST CASE 1
        long[] x1 = {1, 2, 3, 6};
        String[][] raw1 = {{"10","4"},{"2","111"},{"10","12"},{"4","213"}};
        BigInteger[] y1 = new BigInteger[x1.length];
        for (int i = 0; i < x1.length; i++)
            y1[i] = decodeValue(raw1[i][0], raw1[i][1]);
        BigInteger secret1 = lagrangeAt0(new long[]{x1[0],x1[1],x1[2]}, new BigInteger[]{y1[0],y1[1],y1[2]});

        // TEST CASE 2
        long[] x2 = {1,2,3,4,5,6,7,8,9,10};
        String[][] raw2 = {
                {"6",  "13444211440455345511"},
                {"15", "aed7015a346d635"},
                {"15", "6aeeb69631c227c"},
                {"16", "e1b5e05623d881f"},
                {"8",  "316034514573652620673"},
                {"3",  "2122212201122002221120200210011020220200"},
                {"3",  "20120221122211000100210021102001201112121"},
                {"6",  "20220554335330240002224253"},
                {"12", "45153788322a1255483"},
                {"7",  "1101613130313526312514143"}
        };
        BigInteger[] y2 = new BigInteger[x2.length];
        for (int i = 0; i < x2.length; i++)
            y2[i] = decodeValue(raw2[i][0], raw2[i][1]);
        BigInteger secret2 = findSecret(x2, y2, 7);

        // OUTPUT ONLY CONSTANT VALUES
        System.out.println("Test Case 1 Secret: " + secret1);
        System.out.println("Test Case 2 Secret: " + secret2);
    }
}
