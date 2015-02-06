import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Parse a String into a long.
 * <p>
 * Currently uses a regex pattern to enable support for base 10 (decimal), 16 (hexadecimal), and 8 (octal).
 */
public class LongParser {

    public static final int NEG_GROUP = 1;
    public static final int HEX_GROUP = 5;
    public static final int OCT_GROUP = 8;
    public static final int DEC_GROUP = 9;

    final static String regex = "\\s*([-])?(((0x)([0-9a-f]+))|((0)([0-7]+))|([0-9]+))\\s*";
    final static Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    /**
     * Parse a string and return a long.  Supports base 10 (default), hexadecimal, and octal notation.
     *
     * @param s a string that conforms to
     * @return
     */
    public static long stringToLong(String s) {
        if (s == null) {
            throw new NumberFormatException("Cannot parse null long string.");
        }

        /**
         * By far the most expensive part of this implementation.  If there was a hard performance requirement,
         * this aspect of the implementation could be revisited.
         *
         * Note: the problem assignment said that I could not use the existing "stringToLong" functionality,
         * so I do believe that this implementation meets the spec.
         */
        final Matcher m = p.matcher(s);

        boolean negative = false;

        if (!m.matches()) {
            throw new NumberFormatException("String fails to match supported numerical formats: " + regex + " input: " + s);
        }

        // First look for the negative character in the match groups.
        negative = m.group(NEG_GROUP) != null;

        // Base 10 is the default
        int radix = 10;
        String digits;

        if (m.group(HEX_GROUP) != null) {
            radix = 16;
            digits = m.group(HEX_GROUP);
        } else if (m.group(OCT_GROUP) != null) {
            radix = 8;
            digits = m.group(OCT_GROUP);
        } else {
            // Base 10 is the default
            digits = m.group(DEC_GROUP);
        }

        long accumulator = 0l;

        // Now the fun begins.  Multiply and add from left to right.
        for (int i = 0; i < digits.length(); i++) {
            char c = digits.charAt(i);

            int digit = Character.digit(c, radix);
            if (digit < 0) {
                throw new NumberFormatException("Character is not within the domain of the radix.  Character: " + c);
            }

            // Accumulate using negative arithmetic because abs(Long.MIN_VALUE) > Long.MIN_VALUE
            accumulator = radix * accumulator - digit;

        }

        // Flip negative numbers back to positive if necessary
        return negative ? accumulator : -accumulator;
    }

}
