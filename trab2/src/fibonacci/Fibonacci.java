package fibonacci;

import java.math.BigInteger;

public class Fibonacci {
    static public BigInteger simpleRecursive(long i) {
        if (i < 2) return BigInteger.valueOf(i);
        else return simpleRecursive(i - 1).add(simpleRecursive(i - 2));
    }

    static public BigInteger tailRecursive(long i) {
        return tailRecursive(BigInteger.ONE, BigInteger.ZERO, i);
    }

    static private BigInteger tailRecursive(BigInteger curr, BigInteger prev, long count) {
        if (count <= 0) return curr;
        else return tailRecursive(curr.add(prev), curr, count - 1);
    }

    static private BigInteger linear(long i) {

    }
}
