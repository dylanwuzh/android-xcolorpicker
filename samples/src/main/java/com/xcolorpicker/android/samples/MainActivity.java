package com.xcolorpicker.android.samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.xcolorpicker.android.OnColorSelectListener;
import com.xcolorpicker.android.XColorPicker;

public class MainActivity extends AppCompatActivity
        implements OnColorSelectListener {

    View viewColorNew;
    View viewColorOld;
    XColorPicker colorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewColorNew = findViewById(R.id.view_color_new);
        viewColorOld = findViewById(R.id.view_color_old);

        colorPicker = (XColorPicker) findViewById(R.id.color_picker);
        colorPicker.setOnColorSelectListener(this);
    }

    @Override
    public void onColorSelected(int newColor, int oldColor) {
        viewColorNew.setBackgroundColor(newColor);
        viewColorOld.setBackgroundColor(oldColor);
    }
}
