package com.fourshape.numbermazes.maze_core;

import com.fourshape.numbermazes.maze_core.structure.Cell;
import com.fourshape.numbermazes.maze_core.structure.FillType;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.maze_core.structure.Property;
import com.fourshape.numbermazes.utils.MakeLog;

public class GameScore {

    private static final String TAG = "GameScore";

    private int score;
    private int moveScore;
    private int previousRecordedTime = 0;

    private static final int BASIC_SCORE = 100;
    private static final int SCORE_PER_EMPTY_CELL = 10;
    private static final int MAXIMUM_TIME_PER_MOVE = 10;
    private static final int SCORE_PER_MOVE_WITHIN_TIME = 50;
    private static final int NO_SCORE = 0;

    private static final float EASY_LEVEL_FACTOR = 0.10f;
    private static final float MEDIUM_LEVEL_FACTOR = 0.15f;
    private static final float HARD_LEVEL_FACTOR = 0.31f;
    private static final float LEGEND_LEVEL_FACTOR = 0.47f;

    public GameScore () {
        score = 0;
    }

    public void reset () {
        score = 0;
    }

    public int getScore () {
        return score;
    }

    public void setScore (int score) {
        this.score = score;
    }

    public void modifyScore (int moveScore) {
        score -= moveScore;
        MakeLog.info(TAG, "Modified Score: " + moveScore);
    }

    public int getMoveScore () {
        final int tempMoveScore = moveScore;
        moveScore = 0;
        return tempMoveScore;
    }

    public void calculateScore (Matrix matrix, int currentTime, int gameLevel) {

        int timeScore = 0;

        int selectedRow = matrix.getSelectedRow();
        int selectedCol = matrix.getSelectedCol();

        if (currentTime - previousRecordedTime <= MAXIMUM_TIME_PER_MOVE) {
            timeScore += SCORE_PER_MOVE_WITHIN_TIME;
            previousRecordedTime = currentTime;
        }

        int scoreFromEmptyCells = 0;

        for (int row=0; row<matrix.getMAXRow(); row++) {
            for (int col=0; col< matrix.getMAX_COL(); col++) {

                Cell cell = matrix.getBoard()[row][col];

                if (cell.getProperty() == Property.RESERVED && cell.getValue() == 0 && cell.getFillType() == FillType.USER_FILLED && row != selectedRow && col != selectedCol) {
                    scoreFromEmptyCells += SCORE_PER_EMPTY_CELL;
                }

            }
        }

        float basicScoreFromLevel = 0;

        if (gameLevel == GameLevel.EASY) {
            basicScoreFromLevel = BASIC_SCORE + BASIC_SCORE * EASY_LEVEL_FACTOR;
        } else if (gameLevel == GameLevel.MEDIUM) {
            basicScoreFromLevel = BASIC_SCORE + BASIC_SCORE * MEDIUM_LEVEL_FACTOR;
        } else if (gameLevel == GameLevel.HARD) {
            basicScoreFromLevel = BASIC_SCORE + BASIC_SCORE * HARD_LEVEL_FACTOR;
        } else if (gameLevel == GameLevel.LEGEND) {
            basicScoreFromLevel = BASIC_SCORE + BASIC_SCORE * LEGEND_LEVEL_FACTOR;
        } else {
            basicScoreFromLevel = NO_SCORE;
        }

        MakeLog.info(TAG, "TimeScore: " + timeScore);
        MakeLog.info(TAG, "ScoreFromEmptyCells: " + scoreFromEmptyCells);
        MakeLog.info(TAG, "basicScoreFromLevels: " + Math.round(basicScoreFromLevel));

        int totalScore = timeScore + scoreFromEmptyCells + Math.round(basicScoreFromLevel);

        MakeLog.info(TAG, "MoveScore: " + totalScore);

        moveScore = totalScore;
        score += totalScore;

        MakeLog.info(TAG, "GrossScore: " + score);

    }

}
