package com.xcolorpicker.android;

/**
 * 颜色选中的回调接口。
 *
 * @author wuzhen
 * @version 2017-08-23
 */
public interface OnColorSelectListener {

    /**
     * 颜色选中的监听事件。
     *
     * @param newColor 新颜色
     * @param oldColor 原来的颜色
     */
    void onColorSelected(int newColor, int oldColor);
}
