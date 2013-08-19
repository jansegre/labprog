package polycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {
    private final double[] coefs;
    private static final double[] null_coefs = new double[0];
    private static final Pattern term_pattern = Pattern.compile("(.*)\\*?x(?:\\^|\\*\\*)?(.*)");

    // the main constructor is to save the given array as the coefficients
    // in which the index must match the power
    public Polynomial(double[] coefficients) {
        this.coefs = reduced(coefficients);
    }

    // a copy constructor, to allow duplication of polynomials
    public Polynomial(Polynomial other) {
        this.coefs = other.coefs.clone();
    }

    // the default arguments will yield a null degree polynomial
    public Polynomial() {
        coefs = null_coefs;
    }

    // the degree, for example, "1 + 2x + x^2" has order 2
    // on our definition, a null polynomial has null degree
    public Integer degree() {
        return coefs.length == 0 ? null : coefs.length - 1;
    }

    public Double coefficient(int i) {
        return coefs[i];
    }

    public Double leadCoefficient() {
        return coefficient(degree());
    }

    // this method will parse a string like "2x1 + 2x4 + 5" to the corresponding
    static public Polynomial parsePolynomial(String input) throws NumberFormatException {
        Map<Integer, Double> terms = new HashMap<>();
        int max_order = 0;

        // generate a list of terms separated by a greater than one number of spaces/plus sign
        String[] raw_terms = input.split("[\\s\\+]+");

        // try to parse each term
        for (String raw_term : raw_terms) {
            // which must yield an order (exponent of x) and a value (coefficient multiplying x)
            int term_order;
            double term_value;

            Matcher m = term_pattern.matcher(raw_term);

            // check if we have a match for a term with x
            if (m.find()) {
                // if we do so, then group(1) has the coefficient and group(2) the order
                term_value = m.group(1).isEmpty() ? 1.0 : Double.parseDouble(m.group(1));
                term_order = m.group(2).isEmpty() ? 1 : Integer.parseInt(m.group(2));
            } else {
                // if we don't then we'll try to parse it as an independent term (order 0)
                term_order = 0;
                term_value = Double.parseDouble(raw_term);
            }

            // check if we have a stored coefficient for that order
            Double previous_value = terms.get(term_order);
            // if we don't switch from null to zero so we can sum it later
            previous_value = previous_value == null ? 0.0 : previous_value;
            // save the sum of the previous value with the new one,
            // that is, aggregate coefficients with same powers
            terms.put(term_order, term_value + previous_value);

            // keep a track of the highest order found so far
            if (term_order > max_order)
                max_order = term_order;
        }

        // allocate a polynomial with the highest order needed
        double[] coefs = new double[max_order + 1];
        // and populate it with found coefficients, terms not present
        // on our map we allocate a zero for it
        for (int i = 0; i <= max_order; i++) {
            Double term = terms.get(i);
            coefs[i] = term == null ? 0.0 : term;
        }

        return new Polynomial(coefs);
    }

    public String toString() {
        // if the polynomial is a null polynomial
        // then print a 0.0 indicating there is something
        if (coefs.length == 0) {
            return "0.0";
        } else {
            String out = "";
            // show the first term, no power of x here, since it's x0
            if (coefs[0] != 0.0)
                out += coefs[0] + " ";
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
                    // separate terms with a space
                    out += " ";
                }
            }
            return out.trim();
        }
    }

    // this will return a list with all trailing zeros removed
    private static double[] reduced(double[] list) {
        int zeros = 0;
        for (int i = list.length - 1; i >= 0; i--)
            if (list[i] == 0.0)
                zeros++;
            else
                break;
        if (zeros > 0) {
            int new_size = list.length - zeros;
            double[] new_list = new double[new_size];
            System.arraycopy(list, 0, new_list, 0, new_size);
            return new_list;
        } else {
            return list;
        }
    }

    public Polynomial sum(Polynomial other) {
        double[] sum_coefs, shorter;
        if (coefs.length > other.coefs.length) {
            shorter = other.coefs;
            sum_coefs = this.coefs.clone();
        } else {
            shorter = this.coefs;
            sum_coefs = other.coefs.clone();
        }
        for (int i = 0; i < shorter.length; i++)
            sum_coefs[i] += shorter[i];
        return new Polynomial(reduced(sum_coefs));
    }

    // return a polynomial with each coefficient negated
    public Polynomial multiply(Double scalar) {
        double[] ncoefs = coefs.clone();
        for (int i = 0; i < ncoefs.length; i++)
            ncoefs[i] *= scalar;
        return new Polynomial(ncoefs);
    }

    public Polynomial negative() {
        return multiply(-1.0);
    }

    // return the subtraction of other from this, that is (this - other)
    public Polynomial subtract(Polynomial other) {
        return sum(other.negative());
    }

    public Polynomial multiply(Polynomial other) {
        // the new degree will always be the sum of the multiplicands degrees
        double[] out_coefs = new double[degree() + other.degree() + 1];
        // for each coefficient of the new polynomial of power "i"
        for (int i = 0; i < out_coefs.length; i++) {
            // its value will be the sum of the products
            // of all coefficients of which the sum of the powers is "i"
            for (int j = 0; j <= i; j++) {
                // it may be that we don't have both coefficients, in that case
                // whenever the desired coefficient is out of reach we use 0.0 instead
                Double a = j <= other.degree() ? other.coefficient(j) : 0.0;
                Double b = (i - j) <= degree() ? coefficient(i - j) : 0.0;
                out_coefs[i] += a * b;
            }
        }
        // no need to reduce it, the first term will never be zero
        return new Polynomial(out_coefs);
    }

    // implemented as kindly suggested on wikipedia:
    // http://en.wikipedia.org/wiki/Polynomial_long_division#Pseudo-code
    public Polynomial divide(Polynomial divisor) throws IllegalArgumentException {
        if (divisor.degree() == null)
            throw new IllegalArgumentException("division by null polynomial cannot occur");
        Polynomial quotient = new Polynomial();
        Polynomial rest = this;
        while (rest.degree() != null && rest.degree() >= divisor.degree()) {
            double[] terms = new double[rest.degree() - divisor.degree() + 1];
            terms[terms.length - 1] = rest.leadCoefficient() / divisor.leadCoefficient();
            Polynomial t = new Polynomial(terms);
            quotient = quotient.sum(t);
            rest = rest.subtract(divisor.multiply(t));
        }
        return quotient;
    }

    // to the n-th power, that is: this^n
    // optimized powering by squaring [reduced from O(n) to O(log(n))]
    // http://en.wikipedia.org/wiki/Exponentiation_by_squaring#Basic_method
    public Polynomial power(int n) throws IllegalArgumentException {
        if (n < 1)
            throw new IllegalArgumentException("cannot operate powers lower than 1");
        if (n == 1)
            return this;
        Polynomial partial = power(n / 2);
        Polynomial squared = partial.multiply(partial);
        return n % 2 == 0 ? squared : squared.multiply(this);
    }
}
