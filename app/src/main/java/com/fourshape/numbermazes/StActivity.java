package com.fourshape.numbermazes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fourshape.numbermazes.listeners.OnShapeMatchForTestListener;
import com.fourshape.numbermazes.maze_core.BoardView;
import com.fourshape.numbermazes.maze_core.Matrix;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.maze_core.structure.Hexagon;
import com.fourshape.numbermazes.maze_core.structure.Shapes;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.SnackBar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class StActivity extends AppCompatActivity {

    private BoardView boardView;
    private MaterialButton checkForMatchMB, setShapeMB, winTestMB;
    private Matrix matrix;
    private TextView shapeIdTV;

    private TextInputEditText shapeIdET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        setContentView(R.layout.activity_st);

        matrix = new Matrix();

        checkForMatchMB = findViewById(R.id.check_match_mb);

        shapeIdET = findViewById(R.id.shape_id_et);
        setShapeMB = findViewById(R.id.set_shape_mb);
        shapeIdTV = findViewById(R.id.shape_id_set_tv);
        winTestMB = findViewById(R.id.win_test_mb);

        winTestMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boardView.toggleWinAnimationForTest();
            }
        });

        setShapeMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    matrix.fillHexagon(Integer.parseInt(shapeIdET.getText().toString().trim()));
                } catch (Exception e) {
                    MakeLog.exception(e);
                }
            }
        });

        checkForMatchMB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ShapeMatchTest().execute();
            }
        });

        matrix.setOnShapeMatchForTestListener(onShapeMatchForTestListener);

        boardView = findViewById(R.id.board_view);
        boardView.enableShapeEditMode();
        boardView.setMatrix(matrix);
        boardView.enableDrawing();

        Hexagon shapes = new Hexagon();
        String shapeIdText = "";

        for (int i=0; i<shapes.getTotalShape(); i++) {

            if (i==shapes.getTotalShape()-1) {
                shapeIdText += String.valueOf(i+1) + " - That's all.";
            } else {
                shapeIdText += String.valueOf(i+1) + ", ";
            }

        }

        shapeIdTV.setText(shapeIdText);


    }

    private final OnShapeMatchForTestListener onShapeMatchForTestListener = new OnShapeMatchForTestListener() {
        @Override
        public void onMatch(int shapeIndex) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SnackBar.show(boardView, "Matched at Shape - " + shapeIndex);
                }
            });

        }

        @Override
        public void onUnique() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SnackBar.show(boardView, "Congrats! for Unique Shape.");
                }
            });

        }

        @Override
        public void onIncompleteFill() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SnackBar.show(boardView, "Incomplete fill.");
                }
            });

        }
    };

    private class ShapeMatchTest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            matrix.matchShapeForTest();
            return null;
        }
    }

}