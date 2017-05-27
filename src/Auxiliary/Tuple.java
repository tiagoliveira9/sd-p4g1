package Auxiliary;

import java.io.Serializable;

/**
 *
 * @author Tiago Oliveira nºmec.: 51687 <tiago9@ua.pt>
 */
public class Tuple<A, B> implements Serializable {

    private static final long serialVersionUID = 2480L;
    private final A one;
    private final B two;

    public Tuple(A one, B two) {

        this.one = one;
        this.two = two;
    }

    public A getLeft() {
        return one;
    }

    public B getRight() {
        return two;
    }

}
