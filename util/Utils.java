package EPAM2015_task1_7.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Utils {
    public static void clearConsole() {
        for (int i = 0; i < 20; i++) {
            System.out.println("\n");
        }
    }

    public static int getIntFromConsole(int minBorder) {
        if (minBorder == Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        Pattern pat = Pattern.compile("\\p{Digit}+");
        Scanner sc = new Scanner(System.in);
        String input;
        while (!pat.matcher(input = sc.nextLine()).matches() || Integer.parseInt(input) < minBorder) {
            System.out.printf("Incorrect input.\nEnter integer value greater than or equal to %s: ", String.valueOf(minBorder));
        }
        return Integer.parseInt(input);
    }

    public static int getIntFromConsole(int minBorder, int maxBorder) {
        if (minBorder > maxBorder) {
            throw new IllegalArgumentException();
        }
        Pattern pat = Pattern.compile("\\p{Digit}+");
        Scanner sc = new Scanner(System.in);
        String input;
        while (!pat.matcher(input = sc.nextLine()).matches() ||
                Integer.parseInt(input) < minBorder || Integer.parseInt(input) > maxBorder) {
            System.out.printf("Incorrect input.\nEnter integer value within acceptable range [%d .. %d]: ", minBorder, maxBorder);
        }
        return Integer.parseInt(input);
    }
}