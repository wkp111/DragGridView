# DragGridView
条目拖拽排序控件（主要用于新闻条目）
<br>
<br>
## Gradle集成<br>
compile 'com.wkp:DragGridView:1.0.1'
<br>
<br>
## 使用讲解<br>
控件DragGridView和DragSortDialog
<br>
* DragGridView<br>
> 布局<br>
< com.peake.draggridview.DragGridView<br>
    android:id="@+id/dialog_dgv_top" <br>
    android:layout_width="match_parent" <br>
    android:layout_height="wrap_content"/><br><br>
> API<br>
1.setHasDrag 设置长按拖拽是否开启<br>
2.setItemViews 设置控件条目<br>
3.addItemView 添加单个条目<br>
4.setColumnCount 设置条目列数<br>
5.setTransitionDuration 设置拖拽动画时长<br>
6.setTextPadding 设置条目文本内间距<br>
7.setTextMargin 设置条目文本外间距<br>
8.setTextSize 设置条目文本字体大小<br>
9.setTextColor 设置条目文本字体颜色<br>
10.setTextNormalBackground 设置条目文本正常背景<br>
11.setTextSelectedBackground 设置条目文本拖拽背景<br>
12.getItem 获取对应文本对象<br>
13.getSortItems 获取排序完成后的所有条目对象<br>
14.getDefaultItems 获取排序完成后的所有条目文本<br>
15.setOnItemClickListener 设置条目点击监听<br><br>
* DragSortDialog<br>
> API<br>
1.setTopItemViews 设置顶部条目<br>
2.setBottomItemViews 设置底部条目<br>
3.setTopHasDrag 设置顶部拖拽功能是否开启<br>
4.setBottomHasDrag 设置底部拖拽功能是否开启<br>
5.getTopItemViews 获取排序后顶部条目对象<br>
6.getTopDefaultItemViews 获取排序后顶部条目文本<br>
7.getTvTitle 获取顶部标题栏<br>
8.getTvDivision 获取分割标题栏<br>
9.setWidth 设置对话框宽度<br>
10.setHeight 设置对话框高度<br>
11.setGravity 设置对话框位置<br><br>
> 代码示例<br>
<code>
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    //点击弹出对话框
    public void showDialog(View view) {
        DragSortDialog dialog = new DragSortDialog(this);
        dialog.setTopItemViews("ABCDEFG".split("\\B"));
        dialog.setBottomItemViews("OPQRST".split("\\B"));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                List<String> list = ((DragSortDialog) dialog).getTopDefaultItemViews();
                for (String s : list) {
                    Log.d("MainActivity", s);
                }
            }
        });
        dialog.show();
    }
}
</code>
