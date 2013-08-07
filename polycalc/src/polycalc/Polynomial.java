package polycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {
    private Double[] coefs;

    // One option is to pass the
    public Polynomial(Vector<Double> coefs) {
        this.coefs = new Double[coefs.size()];
        for (int i = 0; i < coefs.size(); i++)
            this.coefs[i] = coefs.get(i);
    }

    public Polynomial(int order) {
        coefs = new Double[order + 1];
        for (int i = 0; i < order; i++)
            coefs[i] = new Double(0.0);
    }

    public Double[] getCoefs() {
        return coefs;
    }

    // this method will parse a string like "2x1 + 2x4 + 5" to the corresponding
    static public Polynomial parsePolynomial(String input) throws NumberFormatException {
        Map<Integer, Double> terms = new HashMap<>();
        int max_order = 0;

        String[] raw_terms = input.split("[\\s\\+]+");

        for (String raw_term : raw_terms) {
            int term_order;
            double term_value;

            Pattern p = Pattern.compile("(.*)\\*?x(?:\\^|\\*\\*)?(.*)");
            Matcher m = p.matcher(raw_term);

            if (m.find()) {
                term_value = m.group(1).isEmpty() ? 1.0 : Double.parseDouble(m.group(1));
                term_order = m.group(2).isEmpty() ? 1 : Integer.parseInt(m.group(2));
            } else {
                term_order = 0;
                term_value = Double.parseDouble(raw_term);
            }

            Double previous_value = terms.get(term_order);
            previous_value = previous_value == null ? 0.0 : previous_value;

            terms.put(term_order, term_value + previous_value);

            if (term_order > max_order)
                max_order = term_order;
        }

        Polynomial poly = new Polynomial(max_order);
        for (int i = 0; i <= max_order; i++) {
            Double term = terms.get(i);
            poly.coefs[i] = term == null ? new Double(0.0) : term;
        }

        return poly;
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
