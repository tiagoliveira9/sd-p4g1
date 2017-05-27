/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliary;

import java.io.Serializable;

/**
 *
 * @author Tiago Oliveira nºmec.: 51687 <tiago9@ua.pt>
 */
public class Triple<A, B, C> implements Serializable {

    private static final long serialVersionUID = 2460L;

    private final A one;
    private final B two;
    private final C three;

    public Triple(A one, B two, C three) {

        this.one = one;
        this.two = two;
        this.three = three;
    }

    public A getLeft() {
        return one;
    }

    public B getCenter() {
        return two;
    }

    public C getRight() {
        return three;
    }
}
