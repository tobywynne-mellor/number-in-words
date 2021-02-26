package numberToWords;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


public class NumberToWordsConverterTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private NumberToWordsConverter numberToWordsConverter = new NumberToWordsConverter();

    public NumberToWordsConverterTest() {
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testConvertFunction() {
        String out = numberToWordsConverter.convert("The pump is 536 deep underground.");
        assertEquals("five hundred and thirty-six", out);

        out = NumberToWordsConverter.convert("We processed 9121 records.");
        assertEquals("nine thousand, one hundred and twenty-one", out);

        out = NumberToWordsConverter.convert("Variables reported as having a missing type #65678.");
        assertEquals("number invalid", out);

        out = NumberToWordsConverter.convert("Interactive and printable 10022 ZIP code.");
        assertEquals("ten thousand and twenty-two", out);

        out = NumberToWordsConverter.convert("The database has 66723107008 records.");
        assertEquals("sixty-six billion, seven hundred and twenty-three million, one hundred and seven thousand and eight", out);

        out = NumberToWordsConverter.convert("I received 23 456,9 KGs.");
        assertEquals("number invalid", out);

        out = NumberToWordsConverter.convert("500hrs");
        assertEquals("number invalid", out);

        out = NumberToWordsConverter.convert("1000000");
        assertEquals("one million", out);

        out = NumberToWordsConverter.convert("0");
        assertEquals("zero", out);

        out = NumberToWordsConverter.convert("219");
        assertEquals("two hundred and nineteen", out);

        out = NumberToWordsConverter.convert("1");
        assertEquals("one", out);

        out = NumberToWordsConverter.convert("999999999999999");
        assertEquals("nine hundred and ninety-nine trillion, nine hundred and ninety-nine billion, nine hundred and ninety-nine million, nine hundred and ninety-nine thousand, nine hundred and ninety-nine", out);

        out = NumberToWordsConverter.convert("1000000000000000");
        assertEquals("number invalid", out);
    }

}