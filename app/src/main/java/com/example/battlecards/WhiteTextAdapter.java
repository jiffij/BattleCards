package com.example.battlecards;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WhiteTextAdapter extends ArrayAdapter<String> {
    private int textColor;
    private float textSize;
    private int itemPadding;
    private int gravity;

    public WhiteTextAdapter(Context context, List<String> items) {
        super(context, android.R.layout.simple_list_item_1, items);
        textColor = Color.WHITE;
        textSize = 40;
        itemPadding = 20;
        gravity = Gravity.CENTER;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        textView.setGravity(gravity);
        textView.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
        return textView;
    }

    public void setTextColor(int color) {
        textColor = color;
    }
    public void setTextSize(float size) {
        textSize = size;
    }
    public void setItemPadding(int padding) {
        itemPadding = padding;
    }
    public void setGravity(int grav){this.gravity = grav;}
}