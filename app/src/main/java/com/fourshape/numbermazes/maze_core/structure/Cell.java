package com.fourshape.numbermazes.maze_core.structure;

public class Cell {

    private int value;
    private int correctValue;
    private int validity;
    private int property;
    private int shapeId;
    private int rowLocation;
    private int colLocation;
    private int rank;
    private boolean isFirst, isLast;
    private int fillType;
    private float centerX;
    private float centerY;

    public Cell (int rowLocation, int colLocation, int shapeId, int value, int correctValue, int validity, int property, int rank) {
        this.rowLocation = rowLocation;
        this.colLocation = colLocation;
        this.shapeId = shapeId;
        this.value = value;
        this.correctValue = correctValue;
        this.validity = validity;
        this.property = property;
        this.rank = rank;
        this.fillType = FillType.PRE_FILLED;
        this.centerY = -1;
        this.centerX = -1;
    }

    public void setCenterXY (float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setFillType(int fillType) {
        this.fillType = fillType;
    }

    public int getFillType() {
        return fillType;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setCorrectValue(int correctValue) {
        this.correctValue = correctValue;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColLocation(int colLocation) {
        this.colLocation = colLocation;
    }

    public void setProperty(int property) {
        this.property = property;
    }

    public void setRowLocation(int rowLocation) {
        this.rowLocation = rowLocation;
    }

    public void setShapeId(int shapeId) {
        this.shapeId = shapeId;
    }

    public int getCorrectValue() {
        return correctValue;
    }

    public int getShapeId() {
        return shapeId;
    }

    public int getValue() {
        return value;
    }

    public int getValidity() {
        return validity;
    }

    public int getRowLocation() {
        return rowLocation;
    }

    public int getColLocation() {
        return colLocation;
    }

    public int getProperty() {
        return property;
    }
}
