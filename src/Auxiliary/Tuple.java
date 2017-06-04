package Auxiliary;

import java.io.Serializable;

/**
 * Tuple class.
 * @author Tiago Oliveira nºmec.: 51687, tiago9@ua.pt
 * @param <A> A
 * @param <B> B
 */
public class Tuple<A, B> implements Serializable {

    private static final long serialVersionUID = 2480L;
    private final A one;
    private final B two;

    /**
     * Tuple constructor.
     * 
     * @param one one
     * @param two Two
     */
    public Tuple(A one, B two) {

        this.one = one;
        this.two = two;
    }

    /**
     * Get Left.
     * @return One
     */
    public A getLeft() {
        return one;
    }

    /**
     * Get Right.
     * @return Two
     */
    public B getRight() {
        return two;
    }

}
