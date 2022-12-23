package com.fourshape.numbermazes.maze_core.structure;

import com.fourshape.numbermazes.utils.MakeLog;

import java.util.ArrayList;

public class CellActionRecord {

    private static final String TAG = "CellActionRecord";

    private ArrayList<CellAction> cellActionArrayList;

    public CellActionRecord () {
        cellActionArrayList = new ArrayList<>();
    }

    public void reset () {
        if (cellActionArrayList != null) {
            cellActionArrayList.clear();
        } else {
            cellActionArrayList = new ArrayList<>();
        }
    }

    public void addRow (CellAction cellAction) {
        this.cellActionArrayList.add(cellAction);
    }

    public boolean hasRow () {
        return this.cellActionArrayList.size() > 0;
    }

    public CellAction getCellAction () {

        if (hasRow()) {

            try {
                CellAction cellAction = this.cellActionArrayList.get(this.cellActionArrayList.size()-1);
                this.cellActionArrayList.remove(this.cellActionArrayList.size()-1);
                return cellAction;
            } catch (Exception e) {
                MakeLog.error(TAG, "Error in fetching the latest cell action.");
            }

        }

        return null;
    }

    static public class CellAction {

        private int cellRow, cellCol, origValue, cellMoveScore;

        public CellAction (int cellRow, int cellCol, int origValue, int cellMoveScore) {
            this.cellRow = cellRow;
            this.cellCol = cellCol;
            this.origValue = origValue;
            this.cellMoveScore = cellMoveScore;
        }

        public int getCellMoveScore() {
            return cellMoveScore;
        }

        public int getCellRow() {
            return cellRow;
        }

        public int getCellCol() {
            return cellCol;
        }

        public int getOrigValue() {
            return origValue;
        }
    }

}
