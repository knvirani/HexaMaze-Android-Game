package com.fourshape.numbermazes.maze_core.structure;

import com.fourshape.numbermazes.maze_core.Matrix;

public class GameConfig {

    private Matrix matrix;
    private int gameLevel;
    private int gameType;
    private int gameSeconds;
    private boolean hasWon;

    private int day, month, year;

    public GameConfig (Matrix matrix, int gameLevel, int gameType, int gameSeconds, boolean hasWon) {
        this.matrix = matrix;
        this.gameLevel = gameLevel;
        this.gameType = gameType;
        this.gameSeconds = gameSeconds;
        this.hasWon = hasWon;
    }

    public void setDate (int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public int getGameType() {
        return gameType;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public int getGameSeconds() {
        return gameSeconds;
    }

    public Matrix getMatrix() {
        return matrix;
    }
}
