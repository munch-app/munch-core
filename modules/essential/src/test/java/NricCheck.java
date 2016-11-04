import org.junit.Test;

/**
 * Created by: Fuxing
 * Date: 25/2/2016
 * Time: 9:00 PM
 * Project: vital-core
 */
public class NricCheck {

    @Test
    public void testNric() throws Exception {
        String nric = "S0000001I";
        int[] weight = {2, 7, 6, 5, 4, 3, 2};
        char[] alphabet = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'Z', 'J'};

        String number = nric.substring(1, 8);
        String checkSum = nric.substring(8, 9);

        System.out.println(number);
        System.out.println(checkSum);

        int total = 0;
        for (int i = 0; i < number.length(); i++) {
            int value = Integer.parseInt(number.substring(i, i + 1)) * weight[i];
            total += value;
        }

        int checkDigit = 11 - (total % 11);

        System.out.println(checkDigit);
        System.out.println(alphabet[checkDigit - 1]);

        assert Character.toString(alphabet[checkDigit - 1]).equals(checkSum);
    }
}
