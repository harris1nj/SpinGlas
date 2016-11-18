package com.haddondroyd.spinglas;

/**
 * Created by Jim Harris on 4/22/2014.
 */
import java.util.Random;

public class SpinGlasBondPool {
    private static final int MAXBLUEBONDS = 21;
    private static final int MAXREDBONDS = 11;
    public static final int MAXINDEX = 32;
    private int bondArray[] = new int[MAXINDEX];
    private int bondIndex = 0;
    public void init() {
        int i,j;
        // 21 blue bonds
        int blueBonds = MAXBLUEBONDS;
        // 11 red bonds
        int redBonds = MAXREDBONDS;
        bondIndex = 0;
        for (i=0; i < MAXINDEX; i++) bondArray[i] = 0;
        Random rn = new Random();
        boolean not_done = true;
        int k=0; // number of bondArrays with non-zero values
        // randomize the bonds
        while(not_done) {
            j = rn.nextInt(MAXINDEX); // get a number between 0 and 31
            if (bondArray[j] == 0) {
                if (blueBonds != 0) {
                    bondArray[j] = SpinGlas.BLUEBOND; // blue bond
                    blueBonds--;
                    k++;
                }
                else if (redBonds != 0) {
                    bondArray[j] = SpinGlas.REDBOND; // red bond
                    redBonds--;
                    k++;
                }
                if (k == MAXINDEX) not_done = false; // all done randomizing
            }

        }
    }
    // returns a bond to a player. -1 - error, 1 - red, 2 - blue
    public int getBond() {
        int result = -1; // assume error
        if (bondIndex < MAXINDEX) {
            result = bondArray[bondIndex];
            bondIndex++;
        }

        return result;
    }
    // only used for restoring the game
    public int previousBond() {
        int result = -1; // assume error
        if ((bondIndex <= MAXINDEX)&&(bondIndex > 0)) {
            result = bondArray[bondIndex-1];
        }

        return result;
    }
    public int remaining() {
        return MAXINDEX - bondIndex;
    }
    // used only for saving the game
    public int getBondIndex() {return bondIndex; }
    // used for only restoring the game
    public void setBondIndex(int value) {
        if ((value < MAXINDEX)&&(value >= 0)) bondIndex = value;
    }
    // used only for saving the game
    public int getBondArrayValue(int index) {
        int result = -1;
        if ((index >=0)&&(index < MAXINDEX)) result = bondArray[index];
        return result;
    }
    // used only for restoring the game
    public void setBondArrayValue(int index, int value) {
        if ((index >=0)&&(index < MAXINDEX)) bondArray[index] = value;
    }
}
