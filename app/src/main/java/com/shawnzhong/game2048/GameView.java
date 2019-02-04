package com.shawnzhong.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shawn on 2017/6/5.
 * It's the main view of the game.
 */

public class GameView extends GridLayout {
    private int size = 4;
    private Card[][] cardsMap;

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }


    public void initGameView() {
        setBackgroundColor(0xffbbada0);
        setOnTouchListener(new OnTouchListener() {
            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = motionEvent.getX();
                        startY = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        view.performClick();

                        offsetX = motionEvent.getX() - startX;
                        offsetY = motionEvent.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                if (swipeLeft()) addRandomCard();
                            } else if (offsetX > 5) {
                                if (swipeRight()) addRandomCard();
                            }
                        } else {
                            if (offsetY < -5) {
                                if (swipeUp()) addRandomCard();
                            } else if (offsetY > 5) {
                                if (swipeDown()) addRandomCard();
                            }
                        }
                        break;
                }
                return true;
            }
        });

        post(new Runnable() {
            @Override
            public void run() {
                setSize(size);
            }
        });
    }

    public void setSize(int size) {
        removeAllViews();
        setColumnCount(this.size = size);
        cardsMap = new Card[size][size];

        int cardSize = getWidth() / size;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                cardsMap[x][y] = new Card(getContext(), size);
                addView(cardsMap[x][y], cardSize, cardSize);
            }
        }

        startGame();
    }

    public int getSize() {
        return size;
    }

    public void startGame() {
        MainActivity.getMainActivity().clearScore();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomCard();
        addRandomCard();
    }


    private void addRandomCard() {
        List<Point> emptyPoints = new ArrayList<>(size * size);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.get((int) (Math.random() * emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

        int check = checkEnd();
        if (check == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Uh-oh...")
                    .setMessage("You Die!")
                    .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startGame();
                        }
                    }).show();
        } else if (check == 1) {
            new AlertDialog.Builder(getContext()).
                    setTitle("WOW!").
                    setMessage("You Win!").
                    setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startGame();
                        }
                    }).show();
        }
    }

    private int checkEnd() {

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int num = cardsMap[x][y].getNum();
                if (num == 1024) {
                    return 1;
                }
                if (num <= 0 ||
                        (x > 0 && num == cardsMap[x - 1][y].getNum()) ||
                        (x < size - 1 && num == cardsMap[x + 1][y].getNum()) ||
                        (y > 0 && num == cardsMap[x][y - 1].getNum()) ||
                        (y < size - 1 && num == cardsMap[x][y + 1].getNum())) {
                    return -1;
                }
            }
        }

        return 0;
    }

    private boolean swipeLeft() {
        boolean move = false;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                for (int y1 = y + 1; y1 < size; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        int check = checkSwitch(cardsMap[x][y], cardsMap[x][y1]);
                        move = move || check == 1 || check == 2;
                        y -= (check == 1) ? 1 : 0;

                        break;
                    }
                }

            }
        }
        return move;
    }


    private boolean swipeRight() {
        boolean move = false;
        for (int x = 0; x < size; x++) {
            for (int y = size - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        int check = checkSwitch(cardsMap[x][y], cardsMap[x][y1]);
                        move = move || check == 1 || check == 2;
                        y += (check == 1) ? 1 : 0;

                        break;
                    }
                }

            }
        }
        return move;
    }


    private boolean swipeUp() {
        boolean move = false;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {

                for (int x1 = x + 1; x1 < size; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        int check = checkSwitch(cardsMap[x][y], cardsMap[x1][y]);
                        move = move || check == 1 || check == 2;
                        x -= (check == 1) ? 1 : 0;

                        break;
                    }
                }

            }
        }

        return move;
    }

    private boolean swipeDown() {
        boolean move = false;
        for (int y = 0; y < size; y++) {
            for (int x = size - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        int check = checkSwitch(cardsMap[x][y], cardsMap[x1][y]);
                        move = move || check == 1 || check == 2;
                        x += (check == 1) ? 1 : 0;
                        break;
                    }
                }

            }
        }
        return move;
    }


    private int checkSwitch(Card card1, Card card2) {
        int num1 = card1.getNum();
        int num2 = card2.getNum();

        if (num1 <= 0) {
            card1.setNum(num2);
            card2.setNum(0);

            return 1;
        } else if (num1 == num2) {
            card1.setNum(num1 * 2);
            card2.setNum(0);
            MainActivity.getMainActivity().addScore(num1 * 2);

            return 2;
        }
        return 0;
    }
}


