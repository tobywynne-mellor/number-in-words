package numberToWords;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberToWordsConverter {

    public NumberToWordsConverter() {
    }

    // add empty strings to make it easier to access
    // i.e. upToNineteen[1] = "one"
    // i.e. tensWords[2] = "twenty"
    // i.e. afterHundred[1] = "thousand"
    private static String[] upToNineteen = { "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
                        "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
                        "eighteen", "nineteen"};

    private static String[] tensWords = { "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    // add spaces in front
    private static String[] afterHundred = {"", " thousand", " million", " billion", " trillion"};

    /**
     * Core algorithm to determine the number in it's alphabetical representation.
     * @param inputLine
     * @return numberInWords
     */
    public static String convert(String inputLine){
        // findNumber() uses regex to find the number from the input line
        Long number = findNumber(inputLine);

        // invalid number base case
        if (number == null) {
            return "number invalid";
        }

        // zero base case
        if (number == 0) {
            return "zero";
        }

        int[] groups = groupIntoThreeDigits(number);

        // NOTE: the groups are in reverse order (in the way that you write them but in correct order in terms of ascending value)
        // i.e. group[0] = 832 units, group[1] = 52 thousands

        // store the text for each group
        String[] textForGroup = new String[groups.length];

        // populate the text for each group using the threeDigitsToWords function
        for (int i = 0; i < textForGroup.length; i++) {
           textForGroup[i] = threeDigitsToWords(groups[i]);
        }

        // once all groups are translated into words return the formed sentence
        return formSentence(groups, textForGroup);
    }

    /**
     * Combine the text for each group
     * @param groups
     * @param textForGroup
     * @return
     */
    private static String formSentence(int[] groups, String[] textForGroup) {
        String sentence = "";

        for (int i = 0; i < groups.length; i++) {
            if (groups[i] != 0) {
                // ignore zero digits
                // append the correct unit word after each group
                // since the groups are reverse ordered the first index is empty
                // i.e. afterHundred[0] = ""
                // i.e. afterHundred[1] = "thousand"
                String words = textForGroup[i] + afterHundred[i];

                // add " and " or ", " only if the sentence contains something
                if (sentence.length() != 0) {
                    // only add "and" between the first and second groups
                    // when the first group is between 0 and 100 exclusive - all others use commas
                    // i.e. one thousand, one hundred and one
                    // i.e. one thousand and twenty-one
                    // i.e. one thousand
                    if (i == 1 && groups[0] < 100 && groups[0] > 0) {
                        words += " and ";
                    } else {
                        words += ", ";
                    }
                }

                // add each group to the beginning of the sentence
                // building the number from right to left
                sentence = words + sentence;
            }
        }
        return sentence;
    }

    /**
     * Returns the number chunked into threes
     * @param number
     * @return
     */
    private static int[] groupIntoThreeDigits(Long number) {
        // calculate how many splits needed for the number to fit it in groups of max-size 3
        Double numOfDigits = Double.parseDouble(String.valueOf(number.toString().length()));
        int numOfGroups = (int) Math.ceil(numOfDigits/3.0);

        int[] groups = new int[numOfGroups];

        // split the number into groups of 3 digits
        for (int i = 0; i < groups.length; i++) {
            // populates each index with 3 digits from right to left
            // i.e. number = 52832, groups[0] = 832, groups[1] = 52
            Long temp = number % 1000;
            // parse long to int
            groups[i] = Integer.parseInt(temp.toString());

            // divide the number by 1000 to move the digits to the right ignoring decimals
            // in other words this is removing the hundreds, tens and under tens units
            // i.e. if number was 52832, number is now 52
            number /= 1000;
        }
        return groups;
    }

    /**
     * Auxillary function for convert.
     * Turns upto 3 digits into words
     * @param group
     * @return
     */
    private static String threeDigitsToWords(int group) {
        String words = "";

        // integer division to find the number of hundreds, tensAndUnits, tens and units
        // i.e. group = 789, hundreds = 7
        int hundreds = group / 100;
        int tensAndUnits = group % 100;
        int tens = tensAndUnits / 10;
        int units = tensAndUnits % 10;

        if (hundreds != 0) {
            words += upToNineteen[hundreds] + " hundred";

            if (tensAndUnits != 0) {
                words += " and ";
            }
        }

        if (tensAndUnits >= 20) {
            // 20 or above
            words += tensWords[tens];
            if (units != 0) {
                // hyphenate tens with units if units it not 0 i.e. sixty-six
                words += "-" + upToNineteen[units];
            }
        } else if (tensAndUnits != 0) {
            // 19 or below
            words += upToNineteen[tensAndUnits];
        }
        return words;
    }


    /**
     * Find a number in the input String using RegEx.
     * Assuming a line can only have one number otherwise it is invalid.
     * @param inputLine
     * @return number from within the input String
     */
    private static Long findNumber(String inputLine) {
        // RegEx to describe a number that is not prefixed
        String regex = "(?<!\\S)\\b\\d+\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputLine);

        List<String> matches = new ArrayList<String>();

        while(matcher.find()) {
            matches.add(matcher.group());
        }

        // if more than one number found on a line, number is invalid
        if(matches.size() == 1) {
            Long number = Long.parseLong(matches.get(0));
            // ensure the number does not require quantilions
            // otherwise number is invalid
            if(number <= 999999999999999L) {
                return number;
            }
        }

        return null;
    }

    /**
     * Given a String path, relative to the resources folder, this method returns the input lines
     * @param path
     * @return scanner for the file input
     */
    private static Scanner getInputReader(String path) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource(path);
            File file = new File(url.getFile());

            return new Scanner(file);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            System.out.println("Make sure url does not contain any encoding of spaces.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String args[]) {

        // get input reader from the resources directory
        Scanner inputReader = getInputReader("fixtures/input.txt");

        // convert each line to words
        String output = "";
        while(inputReader.hasNextLine()) {
            output = output + "\n" + convert(inputReader.nextLine());
        }

        // print output into stdout
        // output all lines together so that it can be piped as one if necessary
        System.out.println(output);
    }
}
