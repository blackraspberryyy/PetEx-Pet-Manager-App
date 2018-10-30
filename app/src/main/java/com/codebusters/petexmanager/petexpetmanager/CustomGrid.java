package com.codebusters.petexmanager.petexpetmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by asus-pc on 6/25/2017.
 */

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private ArrayList<Adoption> pets;

    public CustomGrid(Context mContext, ArrayList<Adoption> pets) {
        this.mContext = mContext;
        this.pets = pets;

    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.custom_grid, null);
            TextView petName = (TextView) grid.findViewById(R.id.petName);
            ImageView petImage = (ImageView) grid.findViewById(R.id.petImg);
            LinearLayout petbg = (LinearLayout) grid.findViewById(R.id.pet_background);

            petName.setText(pets.get(position).getPet_name());
            Picasso.with(mContext).load(MyConfig.BASE_URL + pets.get(position).getPet_picture()).into(petImage);

            if(pets.get(position).getAdoption_isMissing()){
                petbg.setBackgroundResource(R.color.flat_green);
            }
        }
        else{
            grid = (View) convertView;
        }

        return grid;
    }
}
