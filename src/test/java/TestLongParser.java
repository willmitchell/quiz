
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import junit.framework.TestCase;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.LongUnaryOperator;

/**
 * Test the LongParser implementation.
 * <p>
 * Will Mitchell
 * 2015
 */
public class TestLongParser extends TestCase {

    public static final Logger log = LoggerFactory.getLogger(TestLongParser.class);

    void parseGoodInput(String inputString, long knownValue) {
        log.debug("inputString = " + inputString);
        log.debug("knownValue = " + knownValue);
        long parsedValue = LongParser.stringToLong(inputString);
        log.debug("parsedValue = " + parsedValue);

        assert parsedValue == knownValue;
    }

    public void testGoodInputs() {
        parseGoodInput("-33", -33);
        parseGoodInput("0x4e", 0x4e);
        parseGoodInput("-0xabcdef", -0xabcdef);
        parseGoodInput("2", 2);
        parseGoodInput(" 3 ", 3);
        parseGoodInput(" -4 ", -4);
        parseGoodInput("022", 022);
        parseGoodInput(new Long(Long.MAX_VALUE).toString(), Long.MAX_VALUE);
        parseGoodInput(new Long(Long.MIN_VALUE).toString(), Long.MIN_VALUE);
    }

    void parseAndExpectFail(String inputString) {
        log.info("inputString = " + inputString);
        boolean failed = false;
        try {
            LongParser.stringToLong(inputString);
        } catch (NumberFormatException e) {
            failed = true;
        }
        assert failed;
    }

    public void testBadInputs() {
        parseAndExpectFail("- 33");
        parseAndExpectFail("0x 4e");
        parseAndExpectFail("-0xabcdefg");
        parseAndExpectFail("2s");
        parseAndExpectFail(" 3x ");
        parseAndExpectFail(null);
    }

    public void testPerformance() {

        int count = 1000000;
        // Generate a bunch of random longs and convert them to strings
        Stopwatch stopwatch1 = Stopwatch.createStarted();
        Random r = new Random(37l);

        // Baseline: Use the JDK
        r.longs(count, Long.MIN_VALUE, Long.MAX_VALUE).forEach(value -> {
            Long.parseLong(Long.toString(value));
        });
        long duration1ms = stopwatch1.elapsed(TimeUnit.MILLISECONDS);
        log.info("Baseline time: " + stopwatch1.toString());

        // Test the LongParser and compare.  NOTE that we create new Stopwatches and Random generators.
        Stopwatch stopwatch2 = Stopwatch.createStarted();
        r = new Random(37l);
        r.longs(count, Long.MIN_VALUE, Long.MAX_VALUE).forEach(value -> {
            LongParser.stringToLong(Long.toString(value));
        });
        long duration2ms = stopwatch2.elapsed(TimeUnit.MILLISECONDS);
        log.info("LongParser time: " + stopwatch2.toString());
        log.info("Performance factor: " + (double) duration2ms / duration1ms);
    }


}
