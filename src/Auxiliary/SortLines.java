/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliary;

/**
 * Sort Lines class.
 * @author Tiago Oliveira nºmec.: 51687, tiago9@ua.pt
 */
public class SortLines implements Comparable<SortLines> {

    private String line;
    private VectorClk localClk;

    /**
     * Sort Lines.
     * @param line Line
     * @param localClk Local clock
     */
    public SortLines(String line, VectorClk localClk) {
        this.line = line;
        this.localClk = localClk;
    }

    /**
     * Get Line
     * @return Line
     */
    public String getLine() {
        return line;
    }

    /**
     * Get vector clock 
     * @return Local clock
     */
    public VectorClk getLocalClk() {
        return localClk;
    }
    /**
     * Sort Lines compare to method.
     * @param objLines
     * @return -1 if it is smaller, 0 if it is equal, 1 if it is bigger.
     */
    @Override
    public int compareTo(SortLines objLines) {
        int[] vtLocal = localClk.getlClk();
        int[] vtNew = objLines.getLocalClk().getlClk();

        boolean localGreater = false;
        boolean newGreater = false;

        for (int i = 0; i < vtLocal.length; i++) {

            if (vtLocal[i] > vtNew[i]) {
                localGreater = true;
            } else if (vtLocal[i] < vtNew[i]) {
                newGreater = true;
            } 
        }

        if (!localGreater && newGreater) {
            return 1; // new object is greater local->new
        } else if ((localGreater && newGreater) || (localGreater && !newGreater) ) {
            return -1; // concurrent or happened before: new->local  
        } else {
            return 0;
        }
    }
}
