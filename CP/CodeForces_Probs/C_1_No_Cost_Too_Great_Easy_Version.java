package CP.CodeForces_Probs;

import java.util.*;

public class C_1_No_Cost_Too_Great_Easy_Version {


    /*Brute force
    
        private static int gcd(int a, int b) {
            // if (b == 0)
            //     return a;
            // return gcd(b, a % b);

            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }
    
         public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();

        while (t-- > 0) {
            int n = sc.nextInt();

            int[] a = new int[n];
            int[] b = new int[n];
            for (int i = 0; i < n; i++)
                a[i] = sc.nextInt();

            for (int i = 0; i < n; i++)
                b[i] = sc.nextInt();

            // Step 1: Check if any pair in arrA already has GCD > 1
            boolean hasCommonGCD = false;

            outer: for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (gcd(a[i], a[j]) > 1) {
                        hasCommonGCD = true;
                        break outer;
                    }
                }
            }

            // If already has a pair with GCD > 1 → no need to add anything
            if (hasCommonGCD) {
                System.out.println(0);
                return;
            }

            // Step 2: Otherwise, try small additions (0–30) to make any pair's GCD > 1
            int minAddition = Integer.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    for (int addA = 0; addA <= 30; addA++) {
                        for (int addB = 0; addB <= 30; addB++) {
                            if (gcd(a[i] + addA, a[j] + addB) > 1) {
                                minAddition = Math.min(minAddition, addA + addB);
                            }
                        }
                    }
                }
            }

            // Step 3 If still no GCD > 1 found (rare case)
            if (minAddition == Integer.MAX_VALUE) {
                minAddition = 2;
            }

            // Step 4: Print the minimum total addition
            System.out.println(minAddition);
            sc.close();

        }

        sc.close();
    
    
    */
    

    /*Using Sieve-Of-Eratosthenes
     
    */

    // pre-compute the factors
    static HashMap<Integer, Integer> totalFactors;     //Keeps track of all primes seen so far among all numbers
    static HashMap<Integer, HashMap<Integer, Integer>> factors;  //Keeps the prime factorisation of the current number

    public static boolean primeFactors(int num, boolean isArrEle) {
        if (isArrEle)
            factors.putIfAbsent(num, new HashMap<>());
        
        int originalNum = num;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                if (totalFactors.containsKey(i))
                    return true;

                int freq = 0;
                while (num % i == 0) {
                    num /= i;
                    freq++;
                }

                if (isArrEle) {
                    factors.get(originalNum).put(i, freq);
                    totalFactors.put(i, freq);
                }
            }
        }
        
        if (num > 1) {
            if (totalFactors.containsKey(num))
                return true;

            if (isArrEle) {
                factors.get(originalNum).put(num, 1);
                totalFactors.put(num, 1);
            }
        }
        return false;
    }



    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t-- > 0) {
            totalFactors = new HashMap<>();
            factors = new HashMap<>();

            int n = sc.nextInt();
            int[] a = new int[n];
            int[] b = new int[n];

            boolean flag = false;
            boolean containEven = false;

            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();

                if (a[i] % 2 == 0) {
                    containEven = true;
                }

                if (!flag) {
                    flag = primeFactors(a[i], true);
                }
            }

            for (int i = 0; i < n; i++) {
                b[i] = sc.nextInt();
            }

            if (flag) {
                System.out.println(0);
                continue;
            }

            if (containEven) {
                System.out.println(1);
                continue;
            }

            for (int i = 0; i < n; i++) {
                // temporarily remove arr[i]'s factors from global map
                for (int j : factors.get(a[i]).keySet()) {
                    totalFactors.remove(j);
                }

                int var = a[i] + 1;
                flag = primeFactors(var, false); // factorize arr[i]+1 but don't store permanently
                if (flag) {
                    System.out.println(1);
                    break;
                }

                // put back the original number's factors
                primeFactors(a[i], true);
            }

            if (flag)
                continue;

            System.out.println(2);

        }
        
        sc.close();


    }


}