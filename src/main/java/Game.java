import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class Game {

    public static boolean inCorrect(String[] str) {
        if (str.length < 2) {
            System.out.println("Please enter more arguments and run the application again with correct data.");
            return false;
        } else if (str.length % 2 == 0) {
            System.out.println("The number of arguments must be odd. For example: rock paper scissors lizard Spock.");
            return false;
        } else {
            return true;
        }

    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static int userMove(String[] str) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter your move: ");
            if (sc.hasNextInt()) {
                int stepUser = sc.nextInt() - 1;
                if (stepUser + 1 <= str.length) {
                    return stepUser;
                } else {
                    System.out.println("You must reach the number with the available moves");
                }
            }
        }
    }

    public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException {
        if (inCorrect(args)) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[16];
            secureRandom.nextBytes(bytes);
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(bytesToHex(bytes).getBytes(), "HmacSHA256");
            hmac.init(keySpec);
            int step = secureRandom.nextInt(args.length);
            String stepComp = args[step];
            byte[] digest = hmac.doFinal(stepComp.getBytes());

            System.out.println("HMAC: " + bytesToHex(digest));

            System.out.println("Available moves:");
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + 1 + " - " + args[i]);
            }
            System.out.println("0 - exit");
            int stepUser = userMove(args);
            System.out.println("Your move: " + args[stepUser]);
            System.out.println("Computer move: " + stepComp);

            printGameResult(step, stepUser, args);
            System.out.println("HMAC key: " + bytesToHex(bytes));


        } else {
            System.out.println("Bye!");
        }

    }

    public static void printGameResult(int stepC, int stepU, String[] mov) {
        if ((stepU - stepC <= (mov.length / 2) && stepU - stepC > 0) || ((mov.length / 2) < stepC - stepU)) {
            System.out.println("You win!");
        } else if (stepC == stepU) {
            System.out.println("Draw");
        } else {
            System.out.println("Comp win!");
        }
    }
}
