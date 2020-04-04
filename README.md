# plateNum
一个选择车牌号组件

简单使用：

~~~xm
<com.zhaopanlong.platenum.PlateNumView
    android:layout_width="match_parent"
    android:layout_height="50dp"
    app:pnvTextSize="18dp"
    app:pnvShowProvince="true"
    app:pnvTextColor="@color/colorPrimary"
    app:pnvLineNormalColor="@color/colorAccent"
    app:pnvLineCheckedColor="@color/colorPrimary"
    app:pnvitemMargin="10dp"
    app:pnvLineWidth="2dp"
    />
~~~

获取车牌号

plateNumView.getPlateNum();

# gradle



# 效果图

<img src="demo.gif" style="zoom:50%;" />



## Attributes

| name                | format    | description            |
| ------------------- | --------- | ---------------------- |
| pnvTextColor        | color     | 车牌文字颜色           |
| pnvTextSize         | dimension | 车牌文字大小           |
| pnvLineNormalColor  | color     | 车牌边框未选中状态颜色 |
| pnvLineCheckedColor | color     | 车牌边框选中状态颜色   |
| pnvLineWidth        | dimension | 边框的宽度             |
| pnvitemMargin       | dimension | 文字间距               |
| pnvShowProvince     | boolean   | 是否默认展示省份选择框 |