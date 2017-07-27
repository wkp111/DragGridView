package com.peake.draggridview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wkp on 2017/5/3.
 */

public class DragGridView<T extends ItemTitle> extends GridLayout implements View.OnLongClickListener, View.OnClickListener {
    private static final int DEFAULT_COLUMN_COUNT = 4;
    private static final long DEFAULT_TRANSITION_DURATION = 150;
    private static final float DEFAULT_TEXT_PADDING = 4.0f;
    private static final float DEFAULT_TEXT_SIZE = 10.0f;
    private static final float MIN_SIZE = 8.0f;
    private static final float DEFAULT_TEXT_MARGIN = 4.0f;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_NORMAL_BACKGROUND = R.drawable.lib_tv_bg_normal;
    private static final int DEFAULT_TEXT_SELECTED_BACKGROUND = R.drawable.lib_tv_bg_selected;
    private int mColumnCount = DEFAULT_COLUMN_COUNT;
    private long mTransitionDuration = DEFAULT_TRANSITION_DURATION;
    private float mTextPadding = DEFAULT_TEXT_PADDING;
    private float mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextNormalBackground = DEFAULT_TEXT_NORMAL_BACKGROUND;
    private int mTextSelectedBackground = DEFAULT_TEXT_SELECTED_BACKGROUND;
    private float mTextMargin = DEFAULT_TEXT_MARGIN;
    private Map<String, T> mItems;
    private List<String> mDefaultItems;
    private boolean mHasDrag;
    private View mDragingView;
    private boolean mIsDefault;

    /**
     * 是否开启长按拖拽
     *
     * @param hasDrag
     */
    public void setHasDrag(boolean hasDrag) {
        mHasDrag = hasDrag;
    }

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayoutParams();
    }

    /**
     * 初始化基本布局参数
     */
    private void initLayoutParams() {
        //设置条目字段数
        super.setColumnCount(mColumnCount);
        //设置布局动画效果
        LayoutTransition transition = new LayoutTransition();
        transition.setDuration(mTransitionDuration);
        setLayoutTransition(transition);
        //设置拖拽监听
        setOnDragListener(mOnDragListener);
    }

    /**
     * 初始化条目控件
     */
    private void initItemViews() {
        removeAllViews();
        if (mIsDefault) {
            if (mDefaultItems == null || mDefaultItems.isEmpty()) {
                return;
            }
            for (String defaultItem : mDefaultItems) {
                addItemView(defaultItem);
            }
        } else {
            if (mItems == null || mItems.isEmpty()) {
                return;
            }
            for (Map.Entry<String, T> entry : mItems.entrySet()) {
                addItemView(entry.getKey());
            }
        }
    }

    /**
     * 拖拽监听
     */
    private OnDragListener mOnDragListener = new OnDragListener() {
        private Rect[] mRects;

        private void initRects() {
            int childCount = getChildCount();
            mRects = new Rect[childCount];
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                mRects[i] = new Rect(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
            }
        }

        private int getChildIndex(int x, int y) {
            for (int i = 0; i < mRects.length; i++) {
                if (mRects[i].contains(x, y)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (!mHasDrag) {
                return true;
            }
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    initRects();
                    mDragingView.setBackgroundResource(mTextSelectedBackground);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    int childIndex = getChildIndex((int) event.getX(), (int) event.getY());
                    if (childIndex >= 0 && getChildAt(childIndex) != mDragingView) {
                        removeView(mDragingView);
                        addView(mDragingView, childIndex);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    mDragingView.setBackgroundResource(mTextNormalBackground);
                    break;
            }
            return true;
        }
    };

    /**
     * 创建TextView条目
     *
     * @param item
     * @return
     */
    private TextView getTextView(String item) {
        TextView textView = new TextView(getContext());
        int textPadding = pxForDp(mTextPadding);
        int textSize = pxForSp(mTextSize);
        textView.setText(item);
        textView.setTextSize(textSize);
        textView.setTextColor(mTextColor);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(textPadding, textPadding, textPadding, textPadding);
        textView.setBackgroundResource(mTextNormalBackground);
        LayoutParams params = new LayoutParams();
        int textMargin = pxForDp(mTextMargin);
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        params.width = widthPixels / getColumnCount() - textPadding * 2;
        params.height = LayoutParams.WRAP_CONTENT;
        params.setMargins(textMargin, textMargin, textMargin, textMargin);
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    private int pxForDp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     *
     * @param px
     * @return
     */
    private int pxForSp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, getResources().getDisplayMetrics());
    }

    /**
     * 设置条目
     *
     * @param items
     */
    public void setItemViews(@NonNull List<T> items) {
        if (mItems == null) {
            mItems = new HashMap<>();
        } else {
            mItems.clear();
        }
        for (T item : items) {
            mItems.put(item.getTitle(), item);
        }
        mIsDefault = false;
        initItemViews();
    }

    /**
     * 设置条目
     *
     * @param items
     */
    public void setItemViews(@NonNull T[] items) {
        if (mItems == null) {
            mItems = new HashMap<>();
        } else {
            mItems.clear();
        }
        for (T item : items) {
            mItems.put(item.getTitle(), item);
        }
        mIsDefault = false;
        initItemViews();
    }

    /**
     * 设置条目
     *
     * @param items
     */
    public void setItemViews(@NonNull ArrayList<String> items) {
        mDefaultItems = items;
        mIsDefault = true;
        initItemViews();
    }

    /**
     * 设置条目
     *
     * @param items
     */
    public void setItemViews(@NonNull String[] items) {
        mDefaultItems = Arrays.asList(items);
        mIsDefault = true;
        initItemViews();
    }

    /**
     * 添加单个条目
     *
     * @param item
     */
    public void addItemView(@NonNull String item) {
        addItemView(item, -1);
    }

    /**
     * 添加单个条目
     *
     * @param item
     */
    public void addItemView(@NonNull T item) {
        addItemView(item, -1);
    }

    /**
     * 添加单个条目
     *
     * @param item
     */
    public void addItemView(@NonNull T item, int index) {
        mIsDefault = false;
        if (mItems == null) {
            mItems = new HashMap<>();
        }
        mItems.put(item.getTitle(), item);
        if (TextUtils.isEmpty(item.getTitle())) {
            return;
        }
        TextView textView = getTextView(item.getTitle());
        addView(textView, index);
        textView.setOnClickListener(this);
        textView.setOnLongClickListener(this);
    }

    /**
     * 添加单个条目
     *
     * @param item
     */
    public void addItemView(@NonNull String item, int index) {
        mIsDefault = true;
        if (TextUtils.isEmpty(item)) {
            return;
        }
        TextView textView = getTextView(item);
        addView(textView, index);
        textView.setOnClickListener(this);
        textView.setOnLongClickListener(this);
    }

    /**
     * 设置条目字段
     *
     * @param columnCount
     */
    @Override
    public void setColumnCount(int columnCount) {
        mColumnCount = columnCount < 1 ? 1 : columnCount;
        initLayoutParams();
    }

    /**
     * 设置动画时长
     *
     * @param transitionDuration
     */
    public void setTransitionDuration(long transitionDuration) {
        mTransitionDuration = transitionDuration < 0 ? 0 : transitionDuration;
        initLayoutParams();
    }

    /**
     * 设置文本内间距
     *
     * @param textPadding
     */
    public void setTextPadding(float textPadding) {
        mTextPadding = textPadding < 0 ? 0 : textPadding;
        initItemViews();
    }

    /**
     * 设置文本外间距
     *
     * @param textMargin
     */
    public void setTextMargin(float textMargin) {
        mTextMargin = textMargin < 0 ? 0 : textMargin;
        initItemViews();
    }

    /**
     * 设置文本字体大小
     *
     * @param textSize
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize < MIN_SIZE ? MIN_SIZE : textSize;
        initItemViews();
    }

    /**
     * 设置文本字体颜色
     *
     * @param textColor
     */
    public void setTextColor(@ColorRes int textColor) {
        mTextColor = textColor;
        initItemViews();
    }

    /**
     * 设置文本普通状态下背景
     *
     * @param textNormalBackground
     */
    public void setTextNormalBackground(@DrawableRes int textNormalBackground) {
        mTextNormalBackground = textNormalBackground;
        initItemViews();
    }

    /**
     * 设置文本选中状态下背景
     *
     * @param textSelectedBackground
     */
    public void setTextSelectedBackground(@DrawableRes int textSelectedBackground) {
        mTextSelectedBackground = textSelectedBackground;
    }

    /**
     * 获取文本对象
     * @param text
     * @return
     */
    @Nullable
    public T getItem(String text){
        if (mItems == null) {
            return null;
        }
        return mItems.get(text);
    }

    /**
     * 获取排序条目，可能包含null元素
     *
     * @return
     */
    @Nullable
    public List<T> getSortItems() {
        if (mItems == null || mItems.isEmpty()) {
            return null;
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            String text = ((TextView) getChildAt(i)).getText().toString();
            result.add(mItems.get(text));
        }
        return result;
    }

    /**
     * 获取排序条目文本
     *
     * @return
     */
    public List<String> getDefaultItems() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            String text = ((TextView) getChildAt(i)).getText().toString();
            result.add(text);
        }
        return result;
    }


    /**
     * 长按监听
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        if (!mHasDrag) {
            return true;
        }
        v.startDrag(null, new DragShadowBuilder(v), null, 0);
        mDragingView = v;
        return true;
    }

    /**
     * 点击监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, this, ((TextView) v).getText().toString(),indexOfChild(v));
        }
    }

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, ViewGroup parent, String text,int position);
    }

    /**
     * 设置条目点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
