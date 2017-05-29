/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Auxiliary;

/**
 *
 * @author Tiago Oliveira nºmec.: 51687 <tiago9@ua.pt>
 */
public class SortLines implements Comparable<SortLines> {

    private String line;
    private VectorClk localClk;

    public SortLines(String line, VectorClk localClk) {
        this.line = line;
        this.localClk = localClk;
    }

    public String getLine() {
        return line;
    }

    public VectorClk getLocalClk() {
        return localClk;
    }

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
