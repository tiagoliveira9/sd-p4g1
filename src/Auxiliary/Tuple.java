package Auxiliary;

import java.io.Serializable;

/**
 *
 * @author Tiago Oliveira nºmec.: 51687, tiago9@ua.pt
 * @param <A> A
 * @param <B> B
 */
public class Tuple<A, B> implements Serializable {

    private static final long serialVersionUID = 2480L;
    private final A one;
    private final B two;

    /**
     *
     * @param one one
     * @param two Two
     */
    public Tuple(A one, B two) {

        this.one = one;
        this.two = two;
    }

    /**
     *
     * @return One
     */
    public A getLeft() {
        return one;
    }

    /**
     *
     * @return Two
     */
    public B getRight() {
        return two;
    }

}
