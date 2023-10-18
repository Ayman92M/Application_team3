package com.example.application_team3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ViewBuilder implements Serializable {

    public ViewBuilder() {
    }

    public void notis(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public PopupWindow buildPopup(View _popupView) {
        // Create a PopupWindow
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // let taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(_popupView, width, height, focusable);
        // Set a click listener for the close button in the popup
        Button closePopupBtn = _popupView.findViewById(R.id.Button_cancel);
        closePopupBtn.setOnClickListener(v -> {
            // Dismiss the popup
            popupWindow.dismiss();
        });
        return popupWindow;
    }

    public void buildListView(List<String> content, Context context, ListView listView, int layoutResourceID, int objectResourceID1, int objectResourceID2) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context.getApplicationContext(),
                layoutResourceID,
                objectResourceID1,
                content
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                String[] itemParts = Objects.requireNonNull(getItem(position)).split(", ");
                TextView textView1 = row.findViewById(objectResourceID1);
                TextView textView2 = row.findViewById(objectResourceID2);

                textView1.setText(itemParts[0]); // item
                textView2.setText(itemParts[1]); // subitem

                return row;
            }
        };
        listView.setAdapter(adapter);
    }
}
