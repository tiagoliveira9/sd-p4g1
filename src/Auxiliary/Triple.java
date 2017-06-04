/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliary;

import java.io.Serializable;

/**
 * Triple class.
 * 
 * @author Tiago Oliveira nºmec.: 51687, tiago9@ua.pt
 * @param <A> A
 * @param <B> B
 * @param <C> C
 */
public class Triple<A, B, C> implements Serializable {

    private static final long serialVersionUID = 2460L;

    private final A one;
    private final B two;
    private final C three;

    /**
     *
     * @param one one
     * @param two two 
     * @param three three
     */
    public Triple(A one, B two, C three) {

        this.one = one;
        this.two = two;
        this.three = three;
    }

    /**
     * Get Left 
     * @return One
     */
    public A getLeft() {
        return one;
    }

    /**
     * Get Center
     * @return Two
     */
    public B getCenter() {
        return two;
    }

    /**
     * Get Right
     * @return Three
     */
    public C getRight() {
        return three;
    }
}
