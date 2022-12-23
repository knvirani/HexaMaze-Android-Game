package com.fourshape.numbermazes.maze_core;

import com.fourshape.numbermazes.listeners.OnCellDataSetListener;
import com.fourshape.numbermazes.listeners.OnInputValidityListener;
import com.fourshape.numbermazes.listeners.OnMatrixReadyListener;
import com.fourshape.numbermazes.listeners.OnSaveLastHexagonIdListener;
import com.fourshape.numbermazes.listeners.OnSaveLastShapeIdListener;
import com.fourshape.numbermazes.listeners.OnShapeMatchForTestListener;
import com.fourshape.numbermazes.maze_core.structure.Cell;
import com.fourshape.numbermazes.maze_core.structure.CellActionRecord;
import com.fourshape.numbermazes.maze_core.structure.FillType;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.maze_core.structure.Hexagon;
import com.fourshape.numbermazes.maze_core.structure.Property;
import com.fourshape.numbermazes.maze_core.structure.Shapes;
import com.fourshape.numbermazes.maze_core.structure.Validity;
import com.fourshape.numbermazes.utils.MakeLog;

import java.util.ArrayList;

public class Matrix {

    private static final String TAG = "Matrix";

    private int selectedCol;
    private int selectedRow;
    private final int MAX = 9;
    private final int MAX_COL = 18;

    private static final int END_NUMBER = 61;
    private static final int BEGIN_NUMBER = 1;

    private Cell[][] board;

    private int gameLevelFactor = -1;
    private int lastShapeId = -1;
    private int lastHexagonShapeId = -1;

    private OnShapeMatchForTestListener onShapeMatchForTestListener;
    private OnSaveLastShapeIdListener onSaveLastShapeIdListener;
    private OnMatrixReadyListener onMatrixReadyListener;
    private OnSaveLastHexagonIdListener onSaveLastHexagonIdListener;
    private OnCellDataSetListener onCellDataSetListener;
    private OnInputValidityListener onInputValidityListener;

    private Cell currentTargetCell;
    private boolean isNewValueAllowed;
    private boolean isInputAllowed = true;

    private CellActionRecord cellActionRecord;

    private GameScore gameScore;

    public Matrix () {

        board = new Cell[MAX][MAX_COL];
        selectedCol = -1;
        selectedRow = -1;

        cellActionRecord = new CellActionRecord();
        gameScore = new GameScore();

        reset();

    }

    public void reset () {

        resetSelectedRC();

        if (cellActionRecord != null) {
            cellActionRecord.reset();
        }

        if (gameScore != null) {
            gameScore.reset();
        }

        int[] totalFillPerRow = {5, 6, 7, 8, MAX, 8, 7, 6, 5};

        // Init -1 to all cells.

        int rank = 1;

        for (int row=0; row<MAX; row++) {

            int totalFill = totalFillPerRow[row];
            final int startsFrom = MAX - totalFill;
            int currentFill = 0;

            for (int col=0; col<MAX_COL; col++) {

                if (col >= startsFrom) {
                    if (startsFrom % 2 == 0) {
                        if (col % 2 == 0 && currentFill < totalFill) {

                            this.board[row][col] = new Cell(row, col, -1, -1, -1, Validity.UNKNOWN, Property.RESERVED, rank);

                            currentFill++;
                            rank++;

                        } else {
                            this.board[row][col] = new Cell(row, col, -1, -1, -1, Validity.UNKNOWN, Property.VOID, rank);
                        }
                    } else {
                        if (col % 2 != 0 && currentFill < totalFill) {

                            this.board[row][col] = new Cell(row, col, -1, -1, -1, Validity.UNKNOWN, Property.RESERVED, rank);
                            currentFill++;
                            rank++;

                        } else {
                            this.board[row][col] = new Cell(row, col, -1, -1, -1, Validity.UNKNOWN, Property.VOID, rank);
                        }
                    }
                } else {
                    this.board[row][col] = new Cell(row, col, -1, -1, -1, Validity.UNKNOWN, Property.VOID, rank);
                }

            }

        }

    }

    public void calculateGameScore (int clockTime) {
        gameScore.calculateScore(this, clockTime, gameLevelFactor);
    }

    public void setCellActionRecord (boolean isOnValidMove) {
        MakeLog.info(TAG + " ActionRecord", "Row: " + selectedRow + " Col: " + selectedCol + " Orig: " + 0 + " Validity: " + (isOnValidMove ? "Valid" : "Invalid"));
        cellActionRecord.addRow(new CellActionRecord.CellAction(selectedRow, selectedCol, 0, isOnValidMove ? gameScore.getMoveScore() : 0));
    }

    public int getGameScore () {
        return this.gameScore.getScore();
    }

    public void setGameScore (int gameScore) {
        this.gameScore.setScore(gameScore);
    }

    public boolean isInputAllowed () {
        return isInputAllowed;
    }

    public void allowInput () {
        isInputAllowed = true;
    }

    public void disallowInput () {
        isInputAllowed = false;
    }

    public boolean performUndo () {

        if (cellActionRecord.hasRow()) {

            CellActionRecord.CellAction cellAction = cellActionRecord.getCellAction();
            if (cellAction != null) {
                if (isValidRC(cellAction.getCellRow(), cellAction.getCellCol())) {

                    this.board[cellAction.getCellRow()][cellAction.getCellCol()].setValue(cellAction.getOrigValue());
                    this.board[cellAction.getCellRow()][cellAction.getCellCol()].setValidity(Validity.UNKNOWN);
                    this.gameScore.modifyScore(cellAction.getCellMoveScore());

                    return true;

                } else {
                    return false;
                }
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    public int getBeginNumber() {
        return BEGIN_NUMBER;
    }

    public int getEndNumber() {
        return END_NUMBER;
    }

    public void setSetGameLevelFactor (int gameLevelFactor) {
        this.gameLevelFactor = gameLevelFactor;
        MakeLog.info(TAG, "Game Level Factor: " + this.gameLevelFactor);
    }

    public void setOnInputValidityListener (OnInputValidityListener onInputValidityListener) {
        this.onInputValidityListener = onInputValidityListener;
    }

    public void setOnCellDataSetListener (OnCellDataSetListener onCellDataSetListener) {
        this.onCellDataSetListener = onCellDataSetListener;
    }

    public void setOnSaveLastHexagonIdListener (OnSaveLastHexagonIdListener onSaveLastHexagonIdListener) {
        this.onSaveLastHexagonIdListener = onSaveLastHexagonIdListener;
    }

    public void setOnMatrixReadyListener (OnMatrixReadyListener onMatrixReadyListener) {
        this.onMatrixReadyListener = onMatrixReadyListener;
    }

    public void setOnSaveLastShapeIdListener (OnSaveLastShapeIdListener onSaveLastShapeIdListener) {
        this.onSaveLastShapeIdListener = onSaveLastShapeIdListener;
    }

    public void setLastShapeId (int lastShapeId) {
        this.lastShapeId = lastShapeId;
    }

    public void setLastHexagonShapeId (int lastHexagonShapeId) {
        this.lastHexagonShapeId = lastHexagonShapeId;
    }

    public int getMAXRow() {
        return MAX;
    }

    public int getMAX_COL () {
        return MAX_COL;
    }

        /*

                    0 1 2 3 4 5 6 7 8 0 1 2 3 4 5 6 7 8
            0   -   O O O O # O # O # O # O # O O O O O
            1   -   O O O # O # O # O # O # O # O O O O
            2   -   O O # O # O # O # O # O # O # O O O
            3   -   O # O # O # O # O # O # O # O # O O
            4   -   # O # O # O # O # O # O # O # O # O
            5   -   O # O # O # O # O # O # O # O # O O
            6   -   O O # O # O # O # O # O # O # O O O
            7   -   O O O # O # O # O # O # O # O O O O
            8   -   O O O O # O # O # O # O # O O O O O

         */
    private void setHexagonShape () {

        Hexagon hexagon = new Hexagon(lastHexagonShapeId);
        int[] shape = hexagon.getShape();
        int index = 0;

        MakeLog.info(TAG, "Last Hexagon Id: " + lastHexagonShapeId);
        MakeLog.info(TAG, "Hexagon Id: " + hexagon.getLastUsedShape());

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                if (this.board[row][col].getProperty() == Property.RESERVED) {

                    if (shape[index] == BEGIN_NUMBER) {
                        this.board[row][col].setFirst(true);
                    } else if (shape[index] == END_NUMBER) {
                        this.board[row][col].setLast(true);
                    }

                    this.board[row][col].setValue(shape[index]);
                    this.board[row][col].setCorrectValue(shape[index]);

                    index++;

                }

            }
        }

        if (onSaveLastHexagonIdListener != null) {
            onSaveLastHexagonIdListener.onSave(hexagon.getLastUsedShape());
        }

    }

    public boolean isTestShapeSolved () {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                Cell cell = this.board[row][col];

                if (cell.getValue() == -1)
                    return false;

            }
        }

        return true;
    }

    public boolean isSolved () {

        boolean hasInvalidCells = false;

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                Cell cell = this.board[row][col];
                if (cell.getProperty() == Property.RESERVED) {
                    hasInvalidCells = cell.getFillType() == FillType.USER_FILLED && (cell.getValidity() == Validity.INVALID || cell.getValidity() == Validity.UNKNOWN) ;
                    if (hasInvalidCells){
                        break;
                    }
                }

            }

            if (hasInvalidCells)
                break;
        }

        return !hasInvalidCells;

    }

    public void setMatrix (Matrix matrix) {

        if (matrix != null) {

            for (int row=0; row<matrix.getMAXRow(); row++) {
                for (int col=0; col<matrix.getMAX_COL(); col++) {

                    this.board[row][col].setValidity(matrix.getBoard()[row][col].getValidity());
                    this.board[row][col].setValue(matrix.getBoard()[row][col].getValue());
                    this.board[row][col].setCorrectValue(matrix.getBoard()[row][col].getCorrectValue());
                    this.board[row][col].setLast(matrix.getBoard()[row][col].isLast());
                    this.board[row][col].setFirst(matrix.getBoard()[row][col].isFirst());
                    this.board[row][col].setFillType(matrix.getBoard()[row][col].getFillType());
                    this.board[row][col].setRowLocation(matrix.getBoard()[row][col].getRowLocation());
                    this.board[row][col].setColLocation(matrix.getBoard()[row][col].getColLocation());
                    this.board[row][col].setRank(matrix.getBoard()[row][col].getRank());
                    this.board[row][col].setShapeId(matrix.getBoard()[row][col].getShapeId());
                    this.board[row][col].setProperty(matrix.getBoard()[row][col].getProperty());
                    this.board[row][col].setCenterXY(matrix.getBoard()[row][col].getCenterX(), matrix.getBoard()[row][col].getCenterY());

                }
            }

            this.setSetGameLevelFactor(matrix.gameLevelFactor);
            this.setGameScore(matrix.getGameScore());

            if (onMatrixReadyListener != null) {
                onMatrixReadyListener.onReady();
            }

        } else {
            if (onMatrixReadyListener != null) {
                onMatrixReadyListener.onMatrixFailed();
            }
        }

    }

    public void setBoard () {

        setHexagonShape();
        //setShapesAndMakeEmpty();
        oddEvenEmpty();

        if (this.onMatrixReadyListener != null) {
            this.onMatrixReadyListener.onReady();
        }

    }

    private void setShapesAndMakeEmpty () {

        if (lastShapeId == -1) {
            lastShapeId = 1;
        }

        Shapes shapes = new Shapes(lastShapeId);
        int shapeId = 100;
        int totalCellsMadeEmpty = 0;

        boolean shouldEndShaping = false;

        // 1. Fill Shape Id with normal procedure
        for (int row=0; row<MAX; row++) {

            for (int col=0; col<MAX_COL; col++) {

                Cell cell = this.board[row][col];

                if (cell.getProperty() == Property.RESERVED) {

                    if (cell.getShapeId() == -1) {

                        int[][] shape = shapes.getShape();
                        boolean isShapePossible = true;

                        // First, calculate the possibility of Shape
                        for (int[] ints: shape) {

                            if (totalCellsMadeEmpty > this.gameLevelFactor) {
                                break;
                            }

                            int shapeRow = row + ints[0];
                            int shapeCol = col + ints[1];

                            isShapePossible = isValidRC(shapeRow, shapeCol) && this.board[shapeRow][shapeCol].getShapeId() == -1;

                            if (!isShapePossible){
                                shapes.reduceShapeId();
                                break;
                            }

                        }

                        if (!isShapePossible)
                            continue;

                        // Now, implement the shape.
                        for (int[] ints: shape) {

                            int shapeRow = row + ints[0];
                            int shapeCol = col + ints[1];

                            this.board[shapeRow][shapeCol].setShapeId(shapeId);
                            this.board[shapeRow][shapeCol].setValue(0);
                            totalCellsMadeEmpty++;

                            if (totalCellsMadeEmpty > gameLevelFactor) {
                                break;
                            }

                        }

                        if (shapeId == 100) {
                            if (onSaveLastShapeIdListener != null) {
                                onSaveLastShapeIdListener.onSave(shapes.getLastUsedShape());
                            }
                        }

                        shapeId++;

                    }

                }

                if (totalCellsMadeEmpty > gameLevelFactor)
                    break;

            }

            if (totalCellsMadeEmpty > gameLevelFactor)
                break;

        }


        if (totalCellsMadeEmpty >= gameLevelFactor) {
            MakeLog.info(TAG, "No Need to apply back to shape as method-1 has already made enough cells empty");
            return;
        }

        boolean stopBackupNow = false;

        // 2. Use backup option to meet the game-level factor requirement
        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                Cell cell = this.board[row][col];

                if (cell.getProperty() == Property.RESERVED && cell.getShapeId() == -1) {

                    for (int shapeList=1; shapeList<=shapes.getTotalShape(); shapeList++) {

                        if (totalCellsMadeEmpty >= gameLevelFactor) {
                            stopBackupNow = true;
                        }

                        shapeId = shapeList;
                        int[][] shape = shapes.getShape(shapeList);

                        boolean isShapePossible = true;

                        // First, calculate the possibility of Shape
                        for (int[] ints: shape) {

                            int shapeRow = row + ints[0];
                            int shapeCol = col + ints[1];

                            isShapePossible = isValidRC(shapeRow, shapeCol) && this.board[shapeRow][shapeCol].getShapeId() == -1;

                            if (!isShapePossible){
                                shapes.reduceShapeId();
                                break;
                            }

                        }

                        if (!isShapePossible)
                            continue;

                        // Now implement a backup shape

                        for (int[] ints: shape) {

                            int shapeRow = row + ints[0];
                            int shapeCol = col + ints[1];

                            if (totalCellsMadeEmpty < gameLevelFactor) {
                                this.board[shapeRow][shapeCol].setShapeId(shapeId);
                                this.board[shapeRow][shapeCol].setValue(0);
                                totalCellsMadeEmpty++;
                            }

                            if (totalCellsMadeEmpty >= gameLevelFactor) {
                                stopBackupNow = true;
                            }

                            if (stopBackupNow)
                                break;

                        }

                        if (stopBackupNow)
                            break;

                    }

                }

                if (stopBackupNow) {
                    break;
                }

            }

            if (stopBackupNow) {
                break;
            }
        }

    }

    private void oddEvenEmpty () {

        int index = 1;

        int normalEmpty = 0;
        int fiveEmpty = 0;
        int sevenEmpty = 0;
        int shapeId = 101;

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                if (this.board[row][col].getProperty() == Property.RESERVED && !this.board[row][col].isFirst() && !this.board[row][col].isLast()) {

                    if (lastShapeId == 2) {

                        if (index % 3 == 0) {
                            this.board[row][col].setFillType(FillType.USER_FILLED);
                            this.board[row][col].setValue(0);
                            this.board[row][col].setShapeId(shapeId);
                            normalEmpty++;
                        }

                        if (onSaveLastShapeIdListener != null && index == 1) {
                            onSaveLastShapeIdListener.onSave(3);
                        }

                    } else {

                        if (index % 2 == 0) {
                            this.board[row][col].setFillType(FillType.USER_FILLED);
                            this.board[row][col].setValue(0);
                            this.board[row][col].setShapeId(shapeId);
                            normalEmpty++;
                        }

                        if (onSaveLastShapeIdListener != null && index == 1) {
                            onSaveLastShapeIdListener.onSave(2);
                        }

                    }

                    if (gameLevelFactor == GameLevel.MEDIUM || gameLevelFactor == GameLevel.HARD || gameLevelFactor == GameLevel.LEGEND) {

                        if (index % 5 == 0) {

                            if (gameLevelFactor == GameLevel.MEDIUM) {
                                if (fiveEmpty <= 5) {
                                    this.board[row][col].setFillType(FillType.USER_FILLED);
                                    this.board[row][col].setValue(0);
                                    this.board[row][col].setShapeId(shapeId);
                                }
                            } else {
                                this.board[row][col].setFillType(FillType.USER_FILLED);
                                this.board[row][col].setValue(0);
                                this.board[row][col].setShapeId(shapeId);
                            }

                            fiveEmpty++;

                        }

                    }

                    if (gameLevelFactor == GameLevel.LEGEND) {
                        if (index % 7 == 0) {
                            this.board[row][col].setFillType(FillType.USER_FILLED);
                            this.board[row][col].setValue(0);
                            this.board[row][col].setShapeId(shapeId);
                            sevenEmpty++;
                        }
                    }

                    index++;

                }

            }
        }

        MakeLog.info(TAG, "Normal Empty: " + normalEmpty);
        MakeLog.info(TAG, "Five Empty: " + fiveEmpty);
        MakeLog.info(TAG, "Seven Empty: " + sevenEmpty);

    }

    public boolean isValidRC (int row, int col) {
        try {
            Cell cell = this.board[row][col];
            return true;
        } catch (Exception e) {
            MakeLog.error(TAG, "Invalid Row Column");
            return false;
        }
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public void selectRC (int row, int col) {
        this.selectedRow = row;
        this.selectedCol = col;
    }

    public Cell getCurrentTargetCell() {
        return currentTargetCell;
    }

    public Cell getCellFromCorrectValue (int cValue) {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                if (this.board[row][col].getCorrectValue() == cValue) {
                    return this.board[row][col];
                }

            }
        }

        return null;

    }

    public Cell getPreviousCellForConnectorLine (int matchCorrectValue) {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                Cell cell = this.board[row][col];

                if (cell.getProperty() == Property.RESERVED) {
                    if (cell.getValue() == (matchCorrectValue-1) && cell.getFillType() == FillType.USER_FILLED && cell.getValidity() == Validity.VALID) {
                        return cell;
                    } else if (cell.getValue() == (matchCorrectValue-1) && cell.getFillType() == FillType.PRE_FILLED && cell.getValidity() == Validity.UNKNOWN) {
                        return cell;
                    }
                }

            }
        }

        return null;

    }

    public void setNewTargetCell () {

        this.currentTargetCell = null;
        isNewValueAllowed = true;

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                Cell cell = this.board[row][col];

                if (cell.getProperty() == Property.RESERVED) {
                    if (cell.getFillType() == FillType.USER_FILLED && cell.getValue() == 0) {

                        if (this.currentTargetCell == null) {
                            this.currentTargetCell = cell;
                        } else {
                            if (cell.getCorrectValue() <= this.currentTargetCell.getCorrectValue()) {
                                this.currentTargetCell = cell;
                            }
                        }
                    }
                }

            }
        }


    }

    public void setCellValue (final int cellValue) {

        isNewValueAllowed = false;

        if (isValidRC(this.selectedRow, this.selectedCol)) {
            if (this.board[this.selectedRow][this.selectedCol].getProperty() == Property.RESERVED) {
                if (this.board[this.selectedRow][this.selectedCol].getFillType() == FillType.USER_FILLED) {

                    boolean isValid = cellValue == this.board[this.selectedRow][this.selectedCol].getCorrectValue();

                    if (isValid) {
                        this.board[this.selectedRow][this.selectedCol].setValidity(Validity.VALID);
                    } else {
                        this.board[this.selectedRow][this.selectedCol].setValidity(Validity.INVALID);
                    }

                    if (cellValue == 0) {
                        this.board[this.selectedRow][this.selectedCol].setValidity(Validity.UNKNOWN);
                    }

                    this.board[this.selectedRow][this.selectedCol].setValue(cellValue);

                    MakeLog.info(TAG, "Set value: " + cellValue);

                    if (onInputValidityListener != null) {
                        if (cellValue == 0) {
                            onInputValidityListener.onUnknown();
                        } else {
                            if (isValid) {
                                onInputValidityListener.onValid();
                            } else {
                                onInputValidityListener.onInvalid();
                            }
                        }
                    }

                    if (onCellDataSetListener != null) {
                        onCellDataSetListener.onDataSet();
                        return;
                    }

                }
            }
        }

        if (onCellDataSetListener != null) {
            onCellDataSetListener.onInvalidSetLocation();
        }

    }

    public boolean isNewValueAllowed () {

        return isNewValueAllowed;

    }

    public void resetSelectedRC () {
        this.selectedRow = -1;
        this.selectedCol = -1;
    }

    public Cell[][] getBoard () {
        return board;
    }

    public void setOnShapeMatchForTestListener(OnShapeMatchForTestListener onShapeMatchForTestListener) {
        this.onShapeMatchForTestListener = onShapeMatchForTestListener;
    }

    public void fillHexagon (int shapeId) {

        int index = 0;
        Hexagon hexagon = new Hexagon();
        int[] shape = hexagon.getShape(shapeId);

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {
                if (this.board[row][col].getProperty() == Property.RESERVED) {

                    this.board[row][col].setValue(shape[index]);
                    index++;

                }
            }
        }

    }

    public void matchShapeForTest () {

        for (int row=0; row<MAX; row++) {
            for (int col=0; col<MAX_COL; col++) {

                if (this.board[row][col].getValue() <= 0 && this.board[row][col].getProperty() == Property.RESERVED) {
                    if (onShapeMatchForTestListener != null) {
                        onShapeMatchForTestListener.onIncompleteFill();
                        return;
                    }
                }

            }
        }

        Hexagon hexagon = new Hexagon();

        boolean isMatch = true;

        for (int i=0; i<hexagon.getTotalShape(); i++) {

            isMatch = true;

            int[] shape = hexagon.getShape(i+1);
            MakeLog.info(TAG, "Shape length: " + shape.length);

            if (shape.length == 0) {
                if (onShapeMatchForTestListener != null) {
                    onShapeMatchForTestListener.onMatch(hexagon.getLastUsedShape());
                }
                break;
            }

            int index = 0;

            for (int row=0; row < MAX; row++) {

                for (int col=0; col<MAX_COL; col++) {
                    if (this.board[row][col].getProperty() == Property.RESERVED) {

                        if (this.board[row][col].getValue() != shape[index] || this.board[row][col].getValue() <= 0 ) {
                            isMatch = false;
                            break;
                        }

                        index++;

                    }
                }

                if (!isMatch)
                    break;
            }

            if (isMatch) {
                if (onShapeMatchForTestListener != null) {
                    onShapeMatchForTestListener.onMatch(i+1);
                }
                break;
            }

        }

        if (!isMatch) {
            if (onShapeMatchForTestListener != null) {
                onShapeMatchForTestListener.onUnique();
            }
        }

    }
}
