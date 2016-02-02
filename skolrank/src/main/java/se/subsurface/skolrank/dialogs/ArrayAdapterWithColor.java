package se.subsurface.skolrank.dialogs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class ArrayAdapterWithColor extends ArrayAdapter<String> {

    private final Integer[] colors;

    public ArrayAdapterWithColor(Context context, String[] items, Integer[] colors) {
        super(context, android.R.layout.select_dialog_item, items);
        this.colors = colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setTextColor(colors[position]);
        textView.setMaxLines(2);
        return view;
    }

}