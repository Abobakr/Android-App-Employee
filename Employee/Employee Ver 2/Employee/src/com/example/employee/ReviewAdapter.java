package com.example.employee;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ReviewAdapter extends BaseAdapter {

    private final Context context;
    private final List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return this.reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return this.reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = this.reviews.get(position);
        return new ReviewListView(this.context,review.id , review.name, review.surname);
    }


    private final class ReviewListView extends LinearLayout {
    	
    	private LinearLayout right;
    	private LinearLayout left;
    	private TextView id;
    	private TextView id_val;
        private TextView name;
        private TextView surname;

        public ReviewListView(Context context,int id, String name, String surname) {

            super(context);                       
            this.setOrientation(HORIZONTAL);            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            		ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 3, 5, 0);
            
            left=new LinearLayout(context);
            left.setOrientation(HORIZONTAL);           
            
            this.id = new TextView(context);
            this.id.setText("ID : ");
            this.id.setTextSize(16f);
            this.id.setTextColor(Color.YELLOW);
            left.addView(this.id,params);
            
            this.id_val = new TextView(context);
            this.id_val.setText(String.valueOf(id));
            this.id_val.setTextSize(19f);
            this.id_val.setTextColor(Color.YELLOW);
            left.addView(this.id_val,params);
            
            //-----------------------------
            
            right=new LinearLayout(context);
            right.setOrientation(VERTICAL);
            
            this.name = new TextView(context);
            this.name.setText("Name: "+name);
            this.name.setTextSize(16f);
            this.name.setTextColor(Color.WHITE);
            right.addView(this.name, params);

            this.surname = new TextView(context);
            this.surname.setText("Surname: "+surname);
            this.surname.setTextSize(16f);
            this.surname.setTextColor(Color.WHITE);
            right.addView(this.surname, params);
            
            this.addView(left);this.addView(right);
        }
    }
}
