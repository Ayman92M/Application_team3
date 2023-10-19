package com.example.application_team3;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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


import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ViewBuilder implements Serializable {

    private Controller control;

    public ViewBuilder() {
    }

    public void setController(Controller _control){
        control = _control;
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

    public void buildListView(boolean isMealList, List<String> content, Context context, ListView listView, int layoutResourceID, int objectResourceID1, int objectResourceID2) {
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
                if(isMealList){
                    TextView txt_backround = row.findViewById(R.id.TextView_background);
                    mealListChangeColor(txt_backround, position, row.getContext());
                    if("false".equals(itemParts[2]) && control.isTimeUp(itemParts[3], itemParts[1], 135)){
                        textView1.setError("miss");
                    }
                }

                return row;
            }
        };
        listView.setAdapter(adapter);
    }
    public void mealListChangeColor(TextView txt_bakground, int position, Context context){

        int[] rowColors = {
                Color.rgb(121, 165, 123),   // Grön
                Color.rgb(255, 193, 7), // Gul
                Color.rgb(164, 120, 241) , // Lila
                Color.rgb(33, 150, 243)   // Blå
        };

        //txt_bakground.setBackgroundColor(rowColors[position % rowColors.length]);
        GradientDrawable gradientDrawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.rounded_corner);
        //GradientDrawable gradientDrawable = (GradientDrawable) getResources().getDrawable(R.drawable.rounded_corner);
        gradientDrawable.setColor(rowColors[position % rowColors.length]);
        txt_bakground.setBackground(gradientDrawable);
    }

}
