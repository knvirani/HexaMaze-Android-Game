package com.fourshape.numbermazes.maze_core.structure;

public class Shapes {

    private int lastUsedShape;
    private static final int TOTAL_SHAPES = 23;

    public Shapes (int lastUsedShape) {
        this.lastUsedShape = lastUsedShape;
    }

    public int getLastUsedShape() {
        return lastUsedShape;
    }

    public int getTotalShape () {
        return TOTAL_SHAPES;
    }

    public void reduceShapeId () {
        lastUsedShape--;
    }

    public int[][] getShape (int shapeIndex) {

        switch (shapeIndex) {

            case 2:
                return SHAPE_2;
            case 3:
                return SHAPE_3;
            case 4:
                return SHAPE_4;
            case 5:
                return SHAPE_5;
            case 6:
                return SHAPE_6;
            case 7:
                return SHAPE_7;
            case 8:
                return SHAPE_8;
            case 9:
                return SHAPE_9;
            case 10:
                return SHAPE_10;
            case 11:
                return SHAPE_11;
            case 12:
                return SHAPE_12;
            case 13:
                return SHAPE_13;
            case 14:
                return SHAPE_14;
            case 15:
                return SHAPE_15;
            case 16:
                return SHAPE_16;
            case 17:
                return SHAPE_17;
            case 18:
                return SHAPE_18;
            case 19:
                return SHAPE_19;
            case 20:
                return SHAPE_20;
            case 21:
                return SHAPE_21;
            case 22:
                return SHAPE_22;
            case 23:
                return SHAPE_23;

            case 1:
            default:
                return SHAPE_1;

        }
    }

    public int[][] getShape () {

        lastUsedShape++;

        if (lastUsedShape <= 0 || lastUsedShape > getTotalShape())
            lastUsedShape = 1;

        switch (lastUsedShape) {

            case 2:
                return SHAPE_2;
            case 3:
                return SHAPE_3;
            case 4:
                return SHAPE_4;
            case 5:
                return SHAPE_5;
            case 6:
                return SHAPE_6;
            case 7:
                return SHAPE_7;
            case 8:
                return SHAPE_8;
            case 9:
                return SHAPE_9;
            case 10:
                return SHAPE_10;
            case 11:
                return SHAPE_11;
            case 12:
                return SHAPE_12;
            case 13:
                return SHAPE_13;
            case 14:
                return SHAPE_14;
            case 15:
                return SHAPE_15;
            case 16:
                return SHAPE_16;
            case 17:
                return SHAPE_17;
            case 18:
                return SHAPE_18;
            case 19:
                return SHAPE_19;
            case 20:
                return SHAPE_20;
            case 21:
                return SHAPE_21;
            case 22:
                return SHAPE_22;
            case 23:
                return SHAPE_23;

            case 1:
            default:
                return SHAPE_1;

        }

    }

    /*
    # #
    #
     */

    private static final int[][] SHAPE_1 = { {0,0}, {0,2}, {1,-1} };

    /*
    # #
     #
     */

    private static final int[][] SHAPE_2 = { {0,0}, {0,2}, {1,1} };

    /*
    # # #
       #
     */

    private static final int[][] SHAPE_3 = { {0,0}, {0,2}, {0,4}, {1,3} };

    /*
    #
     # #
      #
     */

    private static final int[][] SHAPE_4 = { {0,0}, {1,1}, {1,3}, {2,2} };

    /*
    #   #
     # #
     */

    private static final int[][] SHAPE_5 = { {0,0}, {1,1}, {1,3}, {0,4} };

    /*
     #
    # #
     #
     */

    private static final int[][] SHAPE_6 = { {0,0}, {1,-1}, {1,1}, {2,0} };

    /*
       # #
    # #
     */

    private static final int[][] SHAPE_7 = { {0,0}, {0,2}, {1,-3}, {1,-1} };

    /*
     #
    #
     #
     */

    private static final int[][] SHAPE_8 = { {0,0}, {1,-1}, {2,0} };

    /*
     # #
    #   #
     # #
     */

    private static final int[][] SHAPE_9 = { {0,0}, {0,2}, {1,-1}, {1,3}, {2,0}, {2,2} };

    /*
        #
       #
      #
     #
    #
     */

    private static final int[][] SHAPE_10 = { {0,0}, {1,-1}, {2,-2}, {3,-3}, {4,-4} };

    /*
       #
    # #
     #
     */

    private static final int[][] SHAPE_11 = { {0,0}, {0,2}, {-1,3}, {1,1} };

    /*
    # # # #
     */

    private static final int[][] SHAPE_12 = { {0,0}, {0,2}, {0,4}, {0,6} };

    /*
    # #
     #
    # #
     */

    private static final int[][] SHAPE_13 = { {0,0}, {0,2}, {1,1}, {2,0}, {2,2} };

    /*
    #
     # #
    #
     #
     */

    private static final int[][] SHAPE_14 = { {0,0}, {1,1}, {1,3}, {2,0}, {3,1} };

    /*
     #
    #
     */

    private static final int[][] SHAPE_15 = { {0,0}, {1,-1} };

    /*
    #
     #
     */

    private static final int[][] SHAPE_16 = { {0,0}, {1,1} };

    /*
    #
     #
    #
     */

    private static final int[][] SHAPE_17 = { {0,0}, {1,1}, {2,0} };

    /*
    # #
     */

    private static final int[][] SHAPE_18 = { {0,0}, {0,2} };

    /*
      #
     # #
    # #
     */

    private static final int[][] SHAPE_19 = { {0,0}, {1,-1}, {2,-2}, {1,1}, {2,0} };

    /*
       #
    # #
       #
     */

    private static final int[][] SHAPE_20 = { {0,0}, {0,2}, {-1,3}, {1,3} };

    /*
     # #
    # #
     */

    private static final int[][] SHAPE_21 = { {0,0}, {-1,1}, {0,2}, {-1,3} };

    /*
      #
     # #
    #
     */

    private static final int[][] SHAPE_22 = { {0,0}, {1,-1}, {2,-2}, {1,1} };

    /*
    # #
     #
    #
     */

    private static final int[][] SHAPE_23 = { {0,0}, {0,2}, {1,1}, {0,2} };

}
