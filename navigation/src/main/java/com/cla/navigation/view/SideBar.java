package com.cla.navigation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class SideBar extends View {

    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private String[] labels = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int choose = -1;
    private Paint paint = new Paint();

    private TextView mTextDialog;

    private int textColor;
    private int textSize;
    private int textSelectColor;

    /**
     * 为SideBar设置显示字母的TextView
     * @param textDialog textDialog
     */
    public void setTextView(TextView textDialog) {
        this.mTextDialog = textDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SideBar(Context context) {
        this(context, null);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTextSelectColor(int textSelectColor) {
        this.textSelectColor = textSelectColor;
    }

    public void refreshLabels(List<String> list) {
        String[] strings = new String[list.size()];
        list.toArray(strings);

        if (strings.length == 0) {
            return;
        }

        labels = strings;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / labels.length;// 获取每一个字母的高度

        for (int i = 0; i < labels.length; i++) {
            paint.setColor(textColor);
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(textSize);
            if (i == choose) {// 选中的状态
                paint.setColor(textSelectColor);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半
            float xPos = width / 2 - paint.measureText(labels[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(labels[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * labels.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数

        switch (action) {
            case MotionEvent.ACTION_UP:
//                setBackground(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < labels.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(labels[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(labels[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 触摸事件
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}