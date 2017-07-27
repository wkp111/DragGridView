package com.peake.draggridview;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wkp on 2017/5/4.
 */

public class DragSortDialog<T extends ItemTitle> extends Dialog {
    private static final int DEFAULT_WIDTH = WindowManager.LayoutParams.MATCH_PARENT;
    private static final int DEFAULT_HEIGHT = WindowManager.LayoutParams.WRAP_CONTENT;
    private static final int DEFAULT_GRAVITY = Gravity.TOP;
    private int mWidth = DEFAULT_WIDTH;
    private int mHeight = DEFAULT_HEIGHT;
    private int mGravity = DEFAULT_GRAVITY;
    private Context mContext;
    private TextView mTvTitle;
    private TextView mTvDivision;
    private DragGridView<T> mTopDragGridView;
    private DragGridView<T> mBottomDragGridView;

    public DragSortDialog(@NonNull Context context) {
        this(context, R.style.DragSortDialog);
    }

    public DragSortDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.lib_dialog_drag_sort);
        mContext = context;
        initDialog();
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTvTitle = ((TextView) findViewById(R.id.dialog_tv_title));
        mTvDivision = ((TextView) findViewById(R.id.dialog_tv_division));
        mTopDragGridView = ((DragGridView<T>) findViewById(R.id.dialog_dgv_top));
        mBottomDragGridView = ((DragGridView<T>) findViewById(R.id.dialog_dgv_bottom));
        mTopDragGridView.setHasDrag(true);
        mBottomDragGridView.setHasDrag(false);
        mTopDragGridView.setOnItemClickListener(mTopOnItemClickListener);
        mBottomDragGridView.setOnItemClickListener(mBottomOnItemClickListener);
    }

    /**
     * 顶部条目点击监听
     */
    private DragGridView.OnItemClickListener mTopOnItemClickListener = new DragGridView.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, ViewGroup parent, String text, int position) {
            if (parent.getChildCount() <= 1) {
                return;
            }
            mTopDragGridView.removeView(itemView);
            mBottomDragGridView.addItemView(text);
        }
    };
    /**
     * 底部条目点击监听
     */
    private DragGridView.OnItemClickListener mBottomOnItemClickListener = new DragGridView.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, ViewGroup parent, String text, int position) {
            mBottomDragGridView.removeView(itemView);
            mTopDragGridView.addItemView(text);
        }
    };

    /**
     * 初始化基本布局参数
     */
    private void initDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = mWidth;
        params.height = mHeight;
        params.gravity = mGravity;
        setCanceledOnTouchOutside(true);
    }

    public void setTopItemViews(@NonNull List<T> itemViews) {
        mTopDragGridView.setItemViews(itemViews);
    }

    public void setTopItemViews(@NonNull ArrayList<String> itemViews) {
        mTopDragGridView.setItemViews(itemViews);
    }

    public void setTopItemViews(@NonNull T[] itemViews) {
        mTopDragGridView.setItemViews(itemViews);
    }

    public void setTopItemViews(@NonNull String[] itemViews) {
        mTopDragGridView.setItemViews(itemViews);
    }

    public void setBottomItemViews(@NonNull List<T> itemViews) {
        mBottomDragGridView.setItemViews(itemViews);
    }

    public void setBottomItemViews(@NonNull ArrayList<String> itemViews) {
        mBottomDragGridView.setItemViews(itemViews);
    }

    public void setBottomItemViews(@NonNull T[] itemViews) {
        mBottomDragGridView.setItemViews(itemViews);
    }

    public void setBottomItemViews(@NonNull String[] itemViews) {
        mBottomDragGridView.setItemViews(itemViews);
    }

    @Nullable
    public List<T> getTopItemViews() {
        return mTopDragGridView.getSortItems();
    }

    public List<String> getTopDefaultItemViews() {
        return mTopDragGridView.getDefaultItems();
    }

    /**
     * 是否开启顶部拖拽，默认开启
     *
     * @param topHasDrag
     */
    public void setTopHasDrag(boolean topHasDrag) {
        mTopDragGridView.setHasDrag(topHasDrag);
    }

    /**
     * 是否开启底部拖拽，默认不开启
     *
     * @param bottomHasDrag
     */
    public void setBottomHasDrag(boolean bottomHasDrag) {
        mBottomDragGridView.setHasDrag(bottomHasDrag);
    }

    /**
     * 获取头标题栏
     *
     * @return
     */
    public TextView getTvTitle() {
        return mTvTitle;
    }

    /**
     * 获取分隔层标题栏
     *
     * @return
     */
    public TextView getTvDivision() {
        return mTvDivision;
    }

    /**
     * 设置对话框宽
     *
     * @param width
     */
    public void setWidth(int width) {
        if (width == WindowManager.LayoutParams.MATCH_PARENT || width == WindowManager.LayoutParams.WRAP_CONTENT) {
            mWidth = width;
        } else {
            mWidth = pxForDp(width);
        }
        initDialog();
    }

    /**
     * 设置对话框高
     *
     * @param height
     */
    public void setHeight(int height) {
        if (height == WindowManager.LayoutParams.MATCH_PARENT || height == WindowManager.LayoutParams.WRAP_CONTENT) {
            mHeight = height;
        } else {
            mHeight = pxForDp(height);
        }
        initDialog();
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    private int pxForDp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, mContext.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     *
     * @param px
     * @return
     */
    private int pxForSp(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, mContext.getResources().getDisplayMetrics());
    }

    @IntDef({Gravity.TOP, Gravity.BOTTOM, Gravity.CENTER, Gravity.LEFT, Gravity.RIGHT, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL})
    @Target({ElementType.PARAMETER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DragGravity {
    }

    /**
     * 设置对话框位置
     *
     * @param gravity
     */
    public void setGravity(@DragGravity int gravity) {
        mGravity = gravity;
        initDialog();
    }
}
