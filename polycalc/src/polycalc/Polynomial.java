package polycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {
    private Double[] coefs;
    private static Pattern term_pattern = Pattern.compile("(.*)\\*?x(?:\\^|\\*\\*)?(.*)");

    // the main constructor is to save the given array as the coeficients
    // in which the index must match the power
    public Polynomial(Double[] coefs) {
        this.coefs = coefs;
    }

    // the order, "1 + 2x + x^2" has order 2
    public int getOrder() {
        return coefs.length - 1;
    }

    public Double[] getCoefs() {
        return coefs;
    }

    // this method will parse a string like "2x1 + 2x4 + 5" to the corresponding
    static public Polynomial parsePolynomial(String input) throws NumberFormatException {
        Map<Integer, Double> terms = new HashMap<>();
        int max_order = 0;

        // generate a list of terms separated by a greater than one number of spaces/plus sign
        String[] raw_terms = input.split("[\\s\\+]+");

        // try to parse each term
        for (String raw_term : raw_terms) {
            // which must yield an order (expoent of x) and a value (coeficient multiplying x)
            int term_order;
            double term_value;

            Matcher m = term_pattern.matcher(raw_term);

            // check if we have a match for a term with x
            if (m.find()) {
                // if we do so, then group(1) has the coef and group(2) the order
                term_value = m.group(1).isEmpty() ? 1.0 : Double.parseDouble(m.group(1));
                term_order = m.group(2).isEmpty() ? 1 : Integer.parseInt(m.group(2));
            } else {
                // if we don't then we'll try to parse it as an independent term (order 0)
                term_order = 0;
                term_value = Double.parseDouble(raw_term);
            }

            // check if we have a stored coeficient for that order
            Double previous_value = terms.get(term_order);
            // if we don't switch from null to zero so we can sum it later
            previous_value = previous_value == null ? 0.0 : previous_value;
            // save the sum of the previous value with the new one,
            // that is, aggregate coeficients with same powers
            terms.put(term_order, term_value + previous_value);

            // keep a track of the highest order found so far
            if (term_order > max_order)
                max_order = term_order;
        }

        // allocate a polynomial with the highest order needed
        Double[] coefs = new Double[max_order + 1];
        // and populate it with found coeficients, terms not present
        // on our map we allocate a zero for it
        for (int i = 0; i <= max_order; i++) {
            Double term = terms.get(i);
            coefs[i] = term == null ? new Double(0.0) : term;
        }

        return new Polynomial(coefs);
    }

    public String toString() {
        // if the polynomial is a null polynomial
        // then print a 0.0 indicating there is something
        if (coefs.length == 0) {
            return "0.0";
        } else {
            String out = new String();
            // show the first term, no power of x here, since it's x0
            if (coefs[0] != 0.0)
                out += coefs[0];
            if (coefs.length != 1)
                out += " ";
            // since the length is greater then zero
            // show all other terms, if they exist
            for (int i = 1; i < coefs.length; i++) {
                if (coefs[i] != 0.0) {
                    // show the coefficient, except when it's 1.0
                    if (coefs[i] != 1.0)
                        out += coefs[i];
                    // show an "x"
                    out += "x";
                    // followed by the power when not 1
                    if (i != 1)
                        out += i;
                    // don't put a space after the last term
                    if (i != coefs.length - 1)
                        out += " ";
                }
            }
            return out;
        }
    }

    public Polynomial sum(Polynomial other) {
        //TODO
        return this;
    }

    public Polynomial subtract(Polynomial other) {
        //TODO
        return this;
    }

    public Polynomial multiply(Polynomial other) {
        //TODO
        return this;
    }

    public Polynomial divide(Polynomial other) {
        //TODO
        return this;
    }

    public Polynomial power(int i) {
        //TODO
        return this;
    }
}
