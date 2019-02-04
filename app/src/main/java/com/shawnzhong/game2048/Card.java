package com.shawnzhong.game2048;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Shawn on 2017/6/5.
 * Card
 */

public class Card extends FrameLayout {
    private int num = 0;
    private int size;
    private TextView label;


    public Card(@NonNull Context context) {
        this(context, 4);
    }

    public Card(@NonNull Context context, int size) {
        super(context);

        this.size = size;
        label = new TextView(getContext());
        label.setGravity(Gravity.CENTER);

        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 10, 10);
        addView(label, lp);

        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        label.setText(num <= 0 ? "" : String.format("%s", num));
        label.setTextSize(num >= 128 ? 160 / size : 192 / size);
        label.setTextColor(num >= 8 ? 0xfff9f6f2 : 0xff776e65);


        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(50 / size);

        switch (num) {
            case 0:
                shape.setColor(0xffcdc1b4);
                break;
            case 2:
                shape.setColor(0xffeee4da);
                break;
            case 4:
                shape.setColor(0xffede0c8);
                break;
            case 8:
                shape.setColor(0xfff2b179);
                break;
            case 16:
                shape.setColor(0xfff59563);
                break;
            case 32:
                shape.setColor(0xfff67c5f);
                break;
            case 64:
                shape.setColor(0xfff65e3b);
                break;
            case 128:
                shape.setColor(0xffedcf72);
                break;
            case 256:
                shape.setColor(0xffedcc61);
                break;
            case 512:
                shape.setColor(0xffedc850);
                break;
            case 1024:
                shape.setColor(0xffedc53f);
                break;
            case 2048:
                shape.setColor(0xffedc22e);
                break;
        }

        label.setBackground(shape);
    }
}
