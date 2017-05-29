package Auxiliary;

import java.io.Serializable;

/**
 *
 * @author Tiago Oliveira nºmec.: 51687 <tiago9@ua.pt>
 * @param <A>
 * @param <B>
 */
public class Tuple<A, B> implements Serializable {

    private static final long serialVersionUID = 2480L;
    private final A one;
    private final B two;

    /**
     *
     * @param one
     * @param two
     */
    public Tuple(A one, B two) {

        this.one = one;
        this.two = two;
    }

    /**
     *
     * @return
     */
    public A getLeft() {
        return one;
    }

    /**
     *
     * @return
     */
    public B getRight() {
        return two;
    }

}
