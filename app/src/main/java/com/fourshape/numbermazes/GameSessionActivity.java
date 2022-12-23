package com.fourshape.numbermazes;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourshape.numbermazes.analytics.TrackScreen;
import com.fourshape.numbermazes.custom_calender.data_formats.DateData;
import com.fourshape.numbermazes.custom_calender.utils.CurrentCalendar;
import com.fourshape.numbermazes.game_db.DailyGameDB;
import com.fourshape.numbermazes.game_db.stats.StatsDB;
import com.fourshape.numbermazes.listeners.OnBoardStartAnimationListener;
import com.fourshape.numbermazes.listeners.OnCellDataSetListener;
import com.fourshape.numbermazes.listeners.OnCellTapListener;
import com.fourshape.numbermazes.listeners.OnExitSessionPopupDismissListener;
import com.fourshape.numbermazes.listeners.OnGameWonAnimationStatusListener;
import com.fourshape.numbermazes.listeners.OnInputValidityListener;
import com.fourshape.numbermazes.listeners.OnMatrixReadyListener;
import com.fourshape.numbermazes.listeners.OnMazeBoxGetListener;
import com.fourshape.numbermazes.listeners.OnPauseStatusListener;
import com.fourshape.numbermazes.listeners.OnSaveLastHexagonIdListener;
import com.fourshape.numbermazes.listeners.OnSaveLastShapeIdListener;
import com.fourshape.numbermazes.listeners.OnSessionClockTickListener;
import com.fourshape.numbermazes.listeners.OnSessionStatusListener;
import com.fourshape.numbermazes.listeners.OnThemeChangeListener;
import com.fourshape.numbermazes.maze_core.BoardView;
import com.fourshape.numbermazes.maze_core.GameCompletion;
import com.fourshape.numbermazes.maze_core.Matrix;
import com.fourshape.numbermazes.maze_core.SavedGame;
import com.fourshape.numbermazes.maze_core.SessionClock;
import com.fourshape.numbermazes.maze_core.THView;
import com.fourshape.numbermazes.maze_core.structure.ControlLimits;
import com.fourshape.numbermazes.maze_core.structure.GameConfig;
import com.fourshape.numbermazes.maze_core.structure.GameLevel;
import com.fourshape.numbermazes.maze_core.structure.GameLives;
import com.fourshape.numbermazes.ui_popups.PopupExitSession;
import com.fourshape.numbermazes.ui_popups.PopupGamePause;
import com.fourshape.numbermazes.ui_popups.PopupGetMazeBox;
import com.fourshape.numbermazes.ui_popups.PopupMazeBoxStatus;
import com.fourshape.numbermazes.ui_popups.PopupRatings;
import com.fourshape.numbermazes.ui_popups.PopupSoundControl;
import com.fourshape.numbermazes.ui_popups.PopupThemeControl;
import com.fourshape.numbermazes.ui_views.LifeCounterView;
import com.fourshape.numbermazes.ui_views.ShareGameView;
import com.fourshape.numbermazes.utils.ActionSnackbar;
import com.fourshape.numbermazes.utils.AppColor;
import com.fourshape.numbermazes.utils.BitmapFromView;
import com.fourshape.numbermazes.utils.BundleParams;
import com.fourshape.numbermazes.utils.FormattedData;
import com.fourshape.numbermazes.utils.GameType;
import com.fourshape.numbermazes.utils.MakeLog;
import com.fourshape.numbermazes.utils.ScreenConfiguration;
import com.fourshape.numbermazes.utils.SharedData;
import com.fourshape.numbermazes.utils.VariablesControl;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.gson.Gson;

import java.io.OutputStream;

public class GameSessionActivity extends AppCompatActivity {

    private static final String TAG = "BoardActivity";

    private ConstraintLayout parent;
    private LinearLayoutCompat gameStatusView, bottomControlView, gameBoardContainerView, afterWonContainer;
    private MaterialCardView undoCV, backCV, soundCV, themeCV, pauseCV, newGameCV, newSessionCV, shareGameCV;
    private ImageView lifeIV1, lifeIV2, lifeIV3, undoIcoIV, backIcoIV, soundIcoIV, themeIcoIV, pauseIcoIV;
    private TextView gameTimeTV, undoLimitTV, gameScoreTV, gameLevelTV, newGameTV, newSessionTV, shareGameTV;
    private LinearProgressIndicator progressIndicator;
    private Matrix matrix;
    private THView thView;
    private BoardView gameBoardView;
    private LifeCounterView lifeCounterView;
    private AppColor appColor;
    private PopupExitSession popupExitSession;
    private PopupGamePause popupGamePause;
    private CircularProgressIndicator shareImgProgressIndicator;

    private boolean hasGameStarted = false;
    private String savedDBSession = null;
    private int gameType = -1, gameLevel = -1, dailyDay = -1, dailyMonth = -1, dailyYear = -1;
    private GameConfig gameConfig = null;
    private boolean shouldSaveDailyToSharedPref = false;
    private boolean isGameWon = false;
    private boolean isEligibleForWinStreak = true;
    private int dailyGameRecordId = -1;
    private boolean isFreshSession = true;

    private static final int GAME_BEGIN_ANIMATION_TIME = 3000;

    private ActivityResultLauncher<String> activityResultLauncher;

    private AudioAttributes audioAttributes;
    private SoundPool soundPool;
    private int[] soundId = new int[4];
    private Vibrator vibrator;

    @Override
    protected void onCreate (Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        ScreenConfiguration.set(this, this);

        setContentView(R.layout.activity_game_session);

        appColor = new AppColor();
        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        parent = findViewById(R.id.parent);

        gameStatusView = findViewById(R.id.game_status_view);
        bottomControlView = findViewById(R.id.bottom_control_bar_view);
        gameBoardContainerView = findViewById(R.id.game_board_view_container);
        progressIndicator = findViewById(R.id.linear_progress_indicator);
        thView = findViewById(R.id.th_view);
        afterWonContainer = findViewById(R.id.after_won_container);

        pauseCV = findViewById(R.id.pause_action_cv);
        undoCV = findViewById(R.id.undo_action_cv);
        backCV = findViewById(R.id.go_back_action_cv);
        soundCV = findViewById(R.id.sound_action_cv);
        themeCV = findViewById(R.id.theme_action_cv);
        newGameCV = findViewById(R.id.new_game_cv);
        newSessionCV = findViewById(R.id.new_session_cv);
        shareGameCV = findViewById(R.id.share_game_cv);

        pauseIcoIV = findViewById(R.id.pause_ico);
        undoIcoIV = findViewById(R.id.undo_ico);
        backIcoIV = findViewById(R.id.back_ico);
        soundIcoIV = findViewById(R.id.sound_ico);
        themeIcoIV = findViewById(R.id.theme_ico);
        lifeIV1 = findViewById(R.id.life_img_1);
        lifeIV2 = findViewById(R.id.life_img_2);
        lifeIV3 = findViewById(R.id.life_img_3);

        shareGameTV = findViewById(R.id.share_game_tv);
        gameLevelTV = findViewById(R.id.game_level_tv);
        gameScoreTV = findViewById(R.id.game_score_tv);
        gameTimeTV = findViewById(R.id.game_time_tv);
        undoLimitTV = findViewById(R.id.undo_action_status_tv);
        newGameTV = findViewById(R.id.new_game_tv);
        newSessionTV = findViewById(R.id.new_session_tv);

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(8)
                .setAudioAttributes(audioAttributes)
                .build();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        soundId[0] = soundPool.load(this, R.raw.board_start, 1);
        soundId[1] = soundPool.load(this, R.raw.board_number_tap, 1);
        soundId[2] = soundPool.load(this, R.raw.board_undo, 1);
        soundId[3] = soundPool.load(this, R.raw.board_game_win, 1);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (!new SharedData(this).getGameTimeStatus())
            gameTimeTV.setVisibility(View.INVISIBLE);

        if (!new SharedData(this).getGameScoreStatus())
            gameScoreTV.setVisibility(View.INVISIBLE);

        shareImgProgressIndicator = findViewById(R.id.share_img_progress_circular);

        popupExitSession = new PopupExitSession(this);
        popupExitSession.setOnSessionStatusListener(onSessionStatusListener);
        popupExitSession.setOnExitSessionPopupDismissListener(onExitSessionPopupDismissListener);
        popupGamePause = new PopupGamePause(this);
        popupGamePause.setOnPauseStatusListener(onPauseStatusListener);

        if (VariablesControl.IS_PRODUCTION) {
            gameStatusView.setVisibility(View.INVISIBLE);
            bottomControlView.setVisibility(View.INVISIBLE);
        } else {
            progressIndicator.setVisibility(View.GONE);
        }

        Intent intent = getIntent();

        if (intent != null) {
            gameType = intent.getIntExtra(BundleParams.GAME_TYPE, -1);
            gameLevel = intent.getIntExtra(BundleParams.GAME_LEVEL, -1);
            savedDBSession = intent.getStringExtra(BundleParams.GAME_SESSION);
            dailyDay = intent.getIntExtra(BundleParams.DAILY_GAME_DAY, -1);
            dailyMonth = intent.getIntExtra(BundleParams.DAILY_GAME_MONTH, -1);
            dailyYear = intent.getIntExtra(BundleParams.DAILY_GAME_YEAR, -1);
            dailyGameRecordId = intent.getIntExtra(BundleParams.DAILY_GAME_RECORD_ID, -1);
        }

        if (gameType == GameType.GENERAL) {

            gameConfig = null;

        } else if (gameType == GameType.SAVED_GENERAL) {

            gameConfig = SavedGame.getSavedGeneralGame(this);
            isFreshSession = false;

        } else if (gameType == GameType.DAILY) {

            DateData cDateData = CurrentCalendar.getCurrentDateData();
            if (dailyDay == cDateData.getDay() && dailyMonth == cDateData.getMonth() && dailyYear == cDateData.getYear()) {
                shouldSaveDailyToSharedPref = true;
            }

        } else if (gameType == GameType.SAVED_DAILY) {

            gameConfig = SavedGame.getSavedDailyGame(this);
            isFreshSession = false;

        } else if (gameType == GameType.SAVED_DAILY_FROM_DB) {

            if (savedDBSession != null) {
                gameConfig = SavedGame.convertToGameConfig(savedDBSession);
            }
            isFreshSession = false;

            DateData cDateData = CurrentCalendar.getCurrentDateData();
            if (dailyDay == cDateData.getDay() && dailyMonth == cDateData.getMonth() && dailyYear == cDateData.getYear()) {
                shouldSaveDailyToSharedPref = true;
            }

        } else if (gameType == GameType.NEW_DAILY_WITH_DB) {

            gameConfig = null;

            DateData cDateData = CurrentCalendar.getCurrentDateData();

            if (dailyDay == cDateData.getDay() && dailyMonth == cDateData.getMonth() && dailyYear == cDateData.getYear()) {
                shouldSaveDailyToSharedPref = true;
            }

        } else {

            gameType = GameType.GENERAL;
            gameLevel = GameLevel.EASY;

        }

        matrix = new Matrix();
        matrix.setOnMatrixReadyListener(onMatrixReadyListener);
        matrix.setOnSaveLastHexagonIdListener(onSaveLastHexagonIdListener);
        matrix.setOnSaveLastShapeIdListener(onSaveLastShapeIdListener);
        matrix.setOnCellDataSetListener(onCellDataSetListener);
        matrix.setOnInputValidityListener(onInputValidityListener);

        lifeCounterView = new LifeCounterView(lifeIV1, lifeIV2, lifeIV3);
        lifeCounterView.setLife(new SharedData(this).getGameLives());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            shareSessionBitmap();
                        } else {
                            new ActionSnackbar().show(bottomControlView, false, "Unable to share");
                        }
                    }
                });

        SessionClock.init();
        SessionClock.setOnSessionClockTickListener(onSessionClockTickListener);

        refreshViewColors();

        prepareMatrix();
        setViewListeners();

    }

    @Override
    protected void onPause() {
        super.onPause();

        MakeLog.info(TAG, "onPause");
        saveGameSession(true);
        disableGamePlay();

        if (!isDestroyed() && !isFinishing()) {
            if (popupGamePause != null)
                popupGamePause.setData(matrix.getGameScore(), new SharedData(this).getGameLives(), SessionClock.getSeconds(), getGameLevel()).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (soundPool != null)
            soundPool.release();

    }

    @Override
    public void onBackPressed() {

        if (popupExitSession != null) {

            if (gameBoardView != null)
                gameBoardView.disableDrawing();

            popupExitSession.show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        TrackScreen.now(this);
    }

    private void vibrate () {

        if (new SharedData(this).getGameVibrationStatus()) {
            int vibrationMillisecs = 25;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(vibrationMillisecs, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(vibrationMillisecs);
            }
        }

    }

    private void playSoundGameUndo () {
        if (new SharedData(this).getGameSoundStatus()) {
            if (soundPool != null && audioAttributes != null) {
                soundPool.play(soundId[2], 1, 1, 1, 0, 1.0f);
            }
        }
    }

    private void playSoundGameStart () {
        if (new SharedData(this).getGameSoundStatus()) {
            if (soundPool != null && audioAttributes != null) {
                soundPool.play(soundId[0], 1, 1, 1, 0, 1.0f);
            }
        }
    }

    private void playSoundGameCellTap () {
        if (new SharedData(this).getGameSoundStatus()) {
            if (soundPool != null && audioAttributes != null) {
                soundPool.play(soundId[1], 1, 1, 1, 0, 1.0f);
            }
        }
    }

    private void playSoundGameWin () {
        if (new SharedData(this).getGameSoundStatus()) {
            if (soundPool != null && audioAttributes != null) {
                soundPool.play(soundId[3], 1, 1, 1, 0, 1.0f);
            }
        }
    }


    private void shareSessionBitmap () {

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Bitmap gameBoardBitmap = BitmapFromView.createBitmapFromView(gameBoardView, gameBoardView.getDimension(), gameBoardView.getDimension());

            Bitmap bitmap = BitmapFromView.getBitmapFromView(ShareGameView.getView(this, gameBoardBitmap));

            if (shareImgProgressIndicator != null) {
                if (shareImgProgressIndicator.getVisibility() == View.VISIBLE)
                    shareImgProgressIndicator.setVisibility(View.GONE);
            }

            if (shareGameTV != null) {
                if (shareGameTV.getVisibility() != View.VISIBLE)
                    shareGameTV.setVisibility(View.VISIBLE);
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);

            OutputStream outputStream;

            try {
                outputStream = this.getContentResolver().openOutputStream(uri);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
                share.putExtra(Intent.EXTRA_STREAM, uri);
                this.startActivity(Intent.createChooser(share, "Share Image"));
            } catch (Exception e) {
                MakeLog.exception(e);
                new ActionSnackbar().show(bottomControlView, false, "Unable to share image.");
            }


        } else {
            activityResultLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

    }

    private void updateStatsOnSave (boolean isGameCompleted) {
        if (isGameCompleted) {
            new StatsDB(this).updateGameStuff(matrix.getGameScore(), SessionClock.getSeconds(), GameCompletion.COMPLETED);
        } else {
            new StatsDB(this).updateGameStuff(matrix.getGameScore(), SessionClock.getSeconds(), GameCompletion.INCOMPLETE);
        }
    }

    private void markNewStatsEntry () {
        new StatsDB(this).markGameRecordEntry(gameLevel, SessionClock.getSeconds(), matrix.getGameScore(), CurrentCalendar.getCurrentDateData());
    }

    private void setViewListeners () {

        if (shareGameCV != null) {
            shareGameCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (shareGameTV != null) {
                        if (shareGameTV.getVisibility() == View.VISIBLE)
                            shareGameTV.setVisibility(View.INVISIBLE);
                    }

                    if (shareImgProgressIndicator != null) {
                        if (shareImgProgressIndicator.getVisibility() != View.VISIBLE) {
                            shareImgProgressIndicator.setVisibility(View.VISIBLE);
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            shareSessionBitmap();
                        }
                    }, 1200);

                }
            });
        };

        if (pauseCV != null) {
            pauseCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SessionClock.stop();
                    gameBoardView.disableDrawing();

                    popupGamePause.setData(matrix.getGameScore(), new SharedData(view.getContext()).getGameLives(), SessionClock.getSeconds(), getGameLevel()).show();

                }
            });
        }

        if (undoCV != null) {
            undoCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    playSoundGameUndo();

                    try {

                        int undoActions = new SharedData(GameSessionActivity.this).getUndoActions();
                        if (undoActions != 0) {
                            if (matrix.performUndo()) {

                                new SharedData(GameSessionActivity.this).setUndoActions(--undoActions);
                                updateUndoControl();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        setTargetNumber();
                                    }
                                }, 200);

                            }
                        } else {
                            new PopupGetMazeBox(GameSessionActivity.this, GameSessionActivity.this).setOnMazeBoxGetListener(onMazeBoxGetListener).show();
                        }

                    } catch (Exception e) {

                        MakeLog.error(TAG, "Unable to convert UNDO str to integer.");
                        MakeLog.exception(e);
                    }

                }
            });
        }

        if (backCV != null) {
            backCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    gameBoardView.disableDrawing();

                    if (popupExitSession != null)
                        popupExitSession.show();

                }
            });
        }

        if (soundCV != null) {
            soundCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PopupSoundControl(view.getContext()).show(view);
                }
            });
        }

        if (themeCV != null) {
            themeCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new PopupThemeControl(view.getContext()).setOnThemeChangeListener(onThemeChangeListener).show(view);
                }
            });
        }

        if (newGameCV != null) {
            newGameCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        if (newSessionCV != null) {
            newSessionCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNewSession();
                }
            });
        }

    }

    private void startNewSession () {

        hasGameStarted = false;

        afterWonContainer.setVisibility(View.INVISIBLE);
        isGameWon = false;
        isFreshSession = true;
        toggleLinearProgressIndicator();
        afterWonContainer.setVisibility(View.GONE);
        gameBoardContainerView.setVisibility(View.INVISIBLE);
        gameStatusView.setVisibility(View.INVISIBLE);
        bottomControlView.setVisibility(View.INVISIBLE);

        matrix.reset();
        gameBoardView.reset();
        lifeCounterView.setLife(new SharedData(this).getGameLives());

        SessionClock.resetClock();
        prepareMatrix();

    }

    private void prepareMatrix () {

        undoLimitTV.setText(String.valueOf(new SharedData(this).getUndoActions()));

        if (gameConfig != null) {

            gameLevel = gameConfig.getGameLevel();
            gameType = gameConfig.getGameType();
            SessionClock.resetClock();
            SessionClock.init();
            SessionClock.setSeconds(gameConfig.getGameSeconds());
            setGameLevelText();
            matrix.setMatrix(gameConfig.getMatrix());

        } else {

            matrix.setLastShapeId(new SharedData(this).getLastShapeId());
            matrix.setLastHexagonShapeId(new SharedData(this).getLastHexagonId());
            matrix.setSetGameLevelFactor(gameLevel);
            setGameLevelText();
            matrix.setBoard();

        }

    }

    private void setGameLevelText () {

        if (gameLevel == GameLevel.EASY) {
            gameLevelTV.setText("EASY");
        } else if (gameLevel == GameLevel.MEDIUM) {
            gameLevelTV.setText("MEDIUM");
        } else if (gameLevel == GameLevel.HARD) {
            gameLevelTV.setText("HARD");
        } else if (gameLevel == GameLevel.LEGEND) {
            gameLevelTV.setText("LEGEND");
        }

    }

    public String getGameLevel () {
        if (gameLevel == GameLevel.EASY) {
            return "EASY";
        } else if (gameLevel == GameLevel.MEDIUM) {
            return "MEDIUM";
        } else if (gameLevel == GameLevel.HARD) {
            return "HARD";
        } else if (gameLevel == GameLevel.LEGEND) {
            return "LEGEND";
        } else {
            return "NONE";
        }
    }

    private void prepareBoard () {

        gameBoardView = null;
        gameBoardView = findViewById(R.id.board_view);
        gameBoardView.setOnGameWonAnimationStatusListener(onGameWonAnimationStatusListener);
        gameBoardView.setOnBoardStartAnimationListener(onBoardStartAnimationListener);
        gameBoardView.setOnCellTapListener(onCellTapListener);
        gameBoardView.setMatrix(matrix);

        updateScore();
        updateUndoControl();

        startGameBeginAnimation();

    }

    private void toggleLinearProgressIndicator () {

        if (progressIndicator != null) {
            if (progressIndicator.getVisibility() == View.VISIBLE) {
                progressIndicator.setVisibility(View.GONE);
            } else {
                progressIndicator.setVisibility(View.VISIBLE);
            }
        }

    }

    private void startGameBeginAnimation () {

        if (gameBoardContainerView != null) {
            if (gameBoardContainerView.getVisibility() != View.VISIBLE) {
                gameBoardContainerView.setVisibility(View.VISIBLE);
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                enableGamePlay();
            }
        }, GAME_BEGIN_ANIMATION_TIME);

        markNewStatsEntry();

    }
    private void enableGamePlay () {

        toggleLinearProgressIndicator();
        playSoundGameStart();
        gameBoardView.enableDrawing();

        SessionClock.start();

    }

    private void disableGamePlay () {
        gameBoardView.disableDrawing();
        SessionClock.stop();
    }

    private void setTargetNumber () {

        matrix.setNewTargetCell();

        if (matrix.getCurrentTargetCell() != null) {
            if (thView != null) {
                thView.setLetter(matrix.getCurrentTargetCell().getCorrectValue());
                MakeLog.info(TAG, "Letter set");
            }
        } else {
            // No target means, all empty cells are filled and need validation.
            MakeLog.info(TAG, "All Cells are filled.");
        }

    }

    private void saveGameSession (boolean shouldStopClock) {

        if (shouldStopClock) {
            SessionClock.stop();
        }

        if (gameType == GameType.GENERAL || gameType == GameType.SAVED_GENERAL) {

            if (isGameWon) {
                new SharedData(this).saveGeneralGame(null);
            } else {
                new SharedData(this).saveGeneralGame(new Gson().toJson(new GameConfig(matrix, gameLevel, gameType, SessionClock.getSeconds(), isGameWon)));
            }

        } else if (gameType == GameType.DAILY) {

            GameConfig gameConfig = new GameConfig(matrix, gameLevel, gameType, SessionClock.getSeconds(), isGameWon);
            gameConfig.setDate(dailyDay, dailyMonth, dailyYear);

            String jsonStr = new Gson().toJson(gameConfig);

            // Save first in shared pref.
            if (shouldSaveDailyToSharedPref) {
                if (isGameWon) {
                    new SharedData(this).saveDailyGame(null);
                } else {
                    new SharedData(this).saveDailyGame(jsonStr);
                }
            }

            // Save second in database.
            saveToDatabase(jsonStr);

        } else if (gameType == GameType.SAVED_DAILY) {

            if (isGameWon) {

                new SharedData(this).saveDailyGame(null);

            } else {

                GameConfig gameConfig = new GameConfig(matrix, gameLevel, gameType, SessionClock.getSeconds(), isGameWon);
                gameConfig.setDate(dailyDay, dailyMonth, dailyYear);
                new SharedData(this).saveDailyGame(new Gson().toJson(gameConfig));

            }

        } else if (gameType == GameType.SAVED_DAILY_FROM_DB) {

            GameConfig gameConfig = new GameConfig(matrix, gameLevel, gameType, SessionClock.getSeconds(), isGameWon);
            gameConfig.setDate(dailyDay, dailyMonth, dailyYear);

            String jsonStr = new Gson().toJson(gameConfig);
            saveToDatabase(jsonStr);

        }

        updateStatsOnSave(isGameWon);

    }

    private void saveToDatabase (String jsonData) {

        MakeLog.info(TAG, "onSaveGameToDatabase");
        DailyGameDB dailyGameDB = new DailyGameDB(this);

        if (dailyGameRecordId != -1) {
            dailyGameDB.updateData(dailyGameRecordId, dailyDay, dailyMonth, dailyYear, isGameWon ? GameCompletion.COMPLETED : GameCompletion.INCOMPLETE, jsonData);
        } else {

            dailyGameDB.addRecord(new DateData(dailyYear, dailyMonth, dailyDay), isGameWon ? GameCompletion.COMPLETED : GameCompletion.INCOMPLETE, jsonData);
            MakeLog.info(TAG, "onAddGameDataToDB");

            dailyGameRecordId = dailyGameDB.getLastRowId();

            dailyGameDB.updateData(dailyGameRecordId, dailyDay, dailyMonth, dailyYear, isGameWon ? GameCompletion.COMPLETED : GameCompletion.INCOMPLETE, jsonData);
            MakeLog.info(TAG, "onUpdateGameDataToDB");

        }

    }

    private void refreshViewColors () {

        MakeLog.info(TAG, "onRefreshViewColors");

        appColor.setThemeId(new SharedData(this).getAppCurrentTheme());

        if (gameStatusView != null) {
            gameStatusView.setBackgroundColor(this.getColor(appColor.getGameStatusBarBackgroundColor()));
        }

        if (themeCV != null) {
            themeCV.setCardBackgroundColor(this.getColor(appColor.getGameBottomBarBackgroundColor()));
        }

        if (undoCV != null) {
            undoCV.setCardBackgroundColor(this.getColor(appColor.getGameBottomBarBackgroundColor()));
        }

        if (soundCV != null) {
            soundCV.setCardBackgroundColor(this.getColor(appColor.getGameBottomBarBackgroundColor()));
        }

        if (backCV != null) {
            backCV.setCardBackgroundColor(this.getColor(appColor.getGameBottomBarBackgroundColor()));
        }

        if (pauseCV != null) {
            pauseCV.setCardBackgroundColor(this.getColor(appColor.getGameBottomBarBackgroundColor()));
        }

        if (gameLevelTV != null) {
            gameLevelTV.setTextColor(this.getColor(appColor.getGameTimeTextColor()));
        }

        if (gameScoreTV != null) {
            gameScoreTV.setTextColor(this.getColor(appColor.getGameTimeTextColor()));
        }

        if (gameTimeTV != null) {
            gameTimeTV.setTextColor(this.getColor(appColor.getGameTimeTextColor()));
        }

        if (undoIcoIV != null) {
            undoIcoIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getUndoControlTintColor())));
        }

        if (backIcoIV != null) {
            backIcoIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getGameControlIconTintColor())));
        }

        if (soundIcoIV != null) {
            soundIcoIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getGameControlIconTintColor())));
        }

        if (themeIcoIV != null) {
            themeIcoIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getGameControlIconTintColor())));
        }

        if (pauseIcoIV != null) {
            pauseIcoIV.setImageTintList(ColorStateList.valueOf(this.getColor(appColor.getGameControlIconTintColor())));
        }

        if (undoLimitTV != null) {
            undoLimitTV.setTextColor(this.getColor(appColor.getUndoControlLimitTextColor()));
        }

        if (lifeCounterView != null) {
            lifeCounterView.refreshViewColor();
        }

        if (gameBoardContainerView != null) {
            gameBoardContainerView.setBackgroundColor(this.getColor(appColor.getAppBackgroundColor()));
        }

        if (gameBoardView != null) {
            gameBoardView.refreshViewColors();
        }

        if (thView != null) {
            thView.refreshViewColors();
        }

        if (newGameCV != null) {
            newGameCV.setCardBackgroundColor(getColor(this.appColor.getPrimaryBtnBackgroundColor()));
        }

        if (newSessionCV != null) {
            newSessionCV.setCardBackgroundColor(getColor(this.appColor.getPrimaryBtnBackgroundColor()));
        }

        if (newGameTV != null) {
            newGameTV.setTextColor(getColor(this.appColor.getPrimaryBtnTextColor()));
        }

        if (newSessionTV != null) {
            newSessionTV.setTextColor(getColor(this.appColor.getPrimaryBtnTextColor()));
        }

        if (progressIndicator != null) {
            progressIndicator.setIndicatorColor(getColor(this.appColor.getGameSessionLinearProgressIndicatorColor()));
            progressIndicator.setTrackColor(getColor(this.appColor.getGameSessionLinearProgressTrackColor()));
        }

        if (gameType != GameType.GENERAL && gameType != GameType.DAILY) {
            if (newSessionCV != null)
                newSessionCV.setVisibility(View.GONE);
        }

        if (parent != null) {
            parent.setBackgroundColor(this.getColor(this.appColor.getAppBackgroundColor()));
        }

    }

    private void updateScore () {

        if (gameScoreTV != null) {
            gameScoreTV.setText(String.valueOf(matrix.getGameScore()));
        }

    }

    private void updateUndoControl () {

        if (undoLimitTV != null) {
            undoLimitTV.setText(String.valueOf(new SharedData(GameSessionActivity.this).getUndoActions()));
        }

    }

    private void updateLife () {
        if (lifeCounterView != null) {
            lifeCounterView.setLife(new SharedData(this).getGameLives());
        }
    }

    private final OnExitSessionPopupDismissListener onExitSessionPopupDismissListener = new OnExitSessionPopupDismissListener() {
        @Override
        public void onPopupDismiss() {
            if (gameBoardView != null) {
                gameBoardView.enableDrawing();
            }
        }
    };

    private final OnGameWonAnimationStatusListener onGameWonAnimationStatusListener = new OnGameWonAnimationStatusListener() {
        @Override
        public void onFinish() {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (afterWonContainer != null) {

                        if (gameStatusView != null) {
                            gameStatusView.setVisibility(View.GONE);
                        }

                        if (bottomControlView != null) {
                            bottomControlView.setVisibility(View.GONE);
                        }

                        afterWonContainer.setVisibility(View.VISIBLE);
                    }

                    if (new SharedData(GameSessionActivity.this).getAppRatings() ==-0.1f) {
                        new PopupRatings(GameSessionActivity.this).show();
                    }

                }
            }, 1500);

        }
    };

    private final OnMazeBoxGetListener onMazeBoxGetListener = new OnMazeBoxGetListener() {
        @Override
        public void onReceive() {

            matrix.allowInput();

            new SharedData(GameSessionActivity.this).setGameLives(GameLives.MAXIMUM);
            new SharedData(GameSessionActivity.this).setUndoActions(ControlLimits.UNDO_ACTION);
            updateLife();
            updateUndoControl();
            new PopupMazeBoxStatus(GameSessionActivity.this, true, null).show();

        }

        @Override
        public void onCancel(String reasonText) {
            new PopupMazeBoxStatus(GameSessionActivity.this, false, reasonText).show();
        }
    };

    private final OnPauseStatusListener onPauseStatusListener = new OnPauseStatusListener() {
        @Override
        public void onExit() {
            gameBoardView.enableDrawing();
            finish();
        }

        @Override
        public void onResume() {
            gameBoardView.enableDrawing();
            SessionClock.resume();
        }
    };

    private final OnSessionStatusListener onSessionStatusListener = new OnSessionStatusListener() {
        @Override
        public void onExit() {
            gameBoardView.enableDrawing();
            finish();
        }

        @Override
        public void onContinue() {
            gameBoardView.enableDrawing();
        }
    };

    private final OnSessionClockTickListener onSessionClockTickListener = new OnSessionClockTickListener() {
        @Override
        public void onTick(int seconds) {

            if (gameTimeTV != null) {
                try {
                    gameTimeTV.setText(FormattedData.getFormattedTime(seconds));
                } catch (Exception e) {
                    MakeLog.exception(e);
                    gameTimeTV.setText("--:--");
                }
            }

        }
    };

    private final OnCellTapListener onCellTapListener = new OnCellTapListener() {

        @Override
        public void onRemoveCellValue() {

            if (matrix.isNewValueAllowed()) {
                matrix.setCellValue(0);
                MakeLog.info(TAG, "onRemoveCellValue");
            }

        }

        @Override
        public void onSetCellValue() {

            playSoundGameCellTap();

            if (matrix.isInputAllowed()) {
                if (matrix.isNewValueAllowed()) {
                    if (matrix.getCurrentTargetCell() != null) {
                        matrix.setCellValue(matrix.getCurrentTargetCell().getCorrectValue());
                        MakeLog.info(TAG, "onSetCellValue");

                        if (matrix.isSolved()) {

                            playSoundGameWin();

                            if (isFreshSession){
                                new StatsDB(GameSessionActivity.this).markPerfectWin(isEligibleForWinStreak);
                            }

                            isGameWon = true;

                            gameBoardView.enableGameWinAnimation();
                            SessionClock.stop();

                        }
                    }
                }
            } else {
                new PopupGetMazeBox(GameSessionActivity.this, GameSessionActivity.this).setOnMazeBoxGetListener(onMazeBoxGetListener).show();
            }

        }

    };

    private final OnInputValidityListener onInputValidityListener = new OnInputValidityListener() {
        @Override
        public void onValid() {

            matrix.calculateGameScore(SessionClock.getSeconds());
            matrix.setCellActionRecord(true);
            updateScore();

        }

        @Override
        public void onInvalid() {

            isEligibleForWinStreak = false;

            vibrate();

            int totalLives = new SharedData(GameSessionActivity.this).getGameLives();

            if (totalLives != 0) {
                matrix.setCellActionRecord(false);
                new SharedData(GameSessionActivity.this).setGameLives(--totalLives);
                updateLife();
            }

            if (new SharedData(GameSessionActivity.this).getGameLives() == 0) {
                matrix.disallowInput();
                new PopupGetMazeBox(GameSessionActivity.this, GameSessionActivity.this).setOnMazeBoxGetListener(onMazeBoxGetListener).show();
            }

        }

        @Override
        public void onUnknown() {

        }
    };

    private final OnCellDataSetListener onCellDataSetListener = new OnCellDataSetListener() {
        @Override
        public void onDataSet() {

            MakeLog.info(TAG, "OnDataSet");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTargetNumber();
                }
            }, 200);

        }

        @Override
        public void onInvalidSetLocation() {
            MakeLog.info(TAG, "OnInvalidSetLocation");
        }
    };

    private final OnThemeChangeListener onThemeChangeListener = new OnThemeChangeListener() {
        @Override
        public void onChange() {
            refreshViewColors();
        }
    };

    private final OnBoardStartAnimationListener onBoardStartAnimationListener = new OnBoardStartAnimationListener() {
        @Override
        public void onFinish() {

            MakeLog.info(TAG, "OnBoardStartListener");

            int lives = new SharedData(GameSessionActivity.this).getGameLives();

            if (lives == 0)
                matrix.disallowInput();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    hasGameStarted = true;

                    if (lives == 0) {
                        new PopupGetMazeBox(GameSessionActivity.this, GameSessionActivity.this).setOnMazeBoxGetListener(onMazeBoxGetListener).show();
                    }

                    if (gameStatusView != null) {
                        if (gameStatusView.getVisibility() != View.VISIBLE)
                            gameStatusView.setVisibility(View.VISIBLE);
                    }

                    if (bottomControlView != null) {
                        if (bottomControlView.getVisibility() != View.VISIBLE)
                            bottomControlView.setVisibility(View.VISIBLE);
                    }

                    setTargetNumber();

                }
            }, 540);

        }
    };

    private final OnMatrixReadyListener onMatrixReadyListener = new OnMatrixReadyListener() {
        @Override
        public void onReady() {
            MakeLog.info(TAG, "OnMatrixReadyListener");
            prepareBoard();
        }

        @Override
        public void onMatrixFailed() {
            MakeLog.error(TAG, "OnMatrixFailed");
            gameConfig = null;
            prepareMatrix();
        }
    };

    private final OnSaveLastShapeIdListener onSaveLastShapeIdListener = new OnSaveLastShapeIdListener() {
        @Override
        public void onSave(int shapeId) {
            MakeLog.info(TAG, "OnSaveLastShapeIdListener");
            new SharedData(GameSessionActivity.this).setLastShapeId(shapeId);
        }
    };

    private final OnSaveLastHexagonIdListener onSaveLastHexagonIdListener = new OnSaveLastHexagonIdListener() {
        @Override
        public void onSave(int shapeId) {
            MakeLog.info(TAG, "OnSaveLastHexagonIdListener");
            new SharedData(GameSessionActivity.this).setLastHexagonId(shapeId);
        }
    };

}