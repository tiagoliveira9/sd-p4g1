package Auxiliary;

import java.io.Serializable;

/**
 *
 * @author Tiago Oliveira nºmec.: 51687, tiago9@ua.pt
 */
// to do the marshall and unmarshall it needs to be serializable 
public class VectorClk implements Serializable {

    private static final long serialVersionUID = 2490L;
    private final int index;
    private int[] lClk;

    /**
     * Vector clock
     * 
     * @param index Index
     * @param size Size
     */
    public VectorClk(int index, int size) {

        this.index = index;
        lClk = new int[size];

        // always initialize regarding the language (prof.)
        for (int i = 0; i < lClk.length; i++) {
            lClk[i] = 0;
        }
    }

    /**
     * Increment Clock 
     */
    public void incrementClk() {

        lClk[index]++;
    }

    /**
     * Increment Clock out
     * @param in In
     */
    public void incrementClkOut(int in) {

        lClk[in]++;
    }

    /**
     * Update Clock
     * @param ts Vector Clock
     */
    public void updateClk(VectorClk ts) {

        for (int i = 0; i < lClk.length; i++) {

            if (lClk[i] < ts.lClk[i]) {
                lClk[i] = ts.lClk[i];
            }
        }

    }

    /**
     * Get vector clock copy
     * @return Clock copy
     */
    public VectorClk getCopyClk() {

        VectorClk copyClk = new VectorClk(index, lClk.length);

        for (int i = 0; i < lClk.length; i++) {
            copyClk.lClk[i] = lClk[i];
        }

        return copyClk;
    }

    /**
     * Get clock
     * @return Local Clock
     */
    public int[] getlClk() {
        return lClk;
    }
}
