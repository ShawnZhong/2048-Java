package com.shawnzhong.game2048;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private GameView gameView;
    private static MainActivity mainActivity = null;
    private int score = 0;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        gameView = findViewById(R.id.gameView);
    }


    public void clearScore() {
        showScore(score = 0);
    }

    public void addScore(int score) {
        showScore(this.score += score);
    }

    public void showScore(int score) {
        ((TextView) findViewById(R.id.tvScore)).setText(String.format("%s", score));
    }

    public void restart(View view) {
        gameView.startGame();
    }

    public void setting(View view) {
        final Dialog dSetting = new Dialog(MainActivity.this);
        dSetting.setContentView(R.layout.setting);

        final NumberPicker np = dSetting.findViewById(R.id.numberPicker1);
        np.setMaxValue(10);
        np.setMinValue(3);
        np.setValue(gameView.getSize());
        np.setWrapSelectorWheel(false);


        final Button b1 = dSetting.findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.setSize(np.getValue());
                dSetting.dismiss();
            }
        });

        dSetting.show();
    }
}
