package com.fourshape.numbermazes.maze_core.structure;

import com.fourshape.numbermazes.utils.RandNumber;

public class GameLevel {

    public static final int EASY = 10;
    public static final int MEDIUM = 11;
    public static final int HARD = 12;
    public static final int LEGEND = 13;

    public static int getFactor (int level) {
        if (level == EASY) {
            return RandNumber.get(15,25);
        } else if (level == MEDIUM) {
            return RandNumber.get(25,35);
        } else if (level == HARD) {
            return RandNumber.get(35,40);
        } else if (level == LEGEND) {
            return RandNumber.get(40,45);
        } else {
            return 20;
        }
    }

}
