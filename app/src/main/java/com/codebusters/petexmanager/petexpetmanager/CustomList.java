package com.codebusters.petexmanager.petexpetmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends BaseAdapter{
    Toast m_currentToast;
    public void showToast(String text) {
        if(m_currentToast == null) {
            m_currentToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        m_currentToast.setText(text);
        m_currentToast.setDuration(Toast.LENGTH_SHORT);
        m_currentToast.show();
    }

    Context context;
    List<String> pet_picture = new ArrayList<>();
    List<String> pet_name = new ArrayList<>();
    List<String> pet_desc = new ArrayList<>();

    private static LayoutInflater inflater = null;

    public CustomList(Context con, List<String> pet_picture, List<String> pet_name, List<String> pet_desc) {
        this.pet_picture = pet_picture;
        this.pet_name = pet_name;
        this.pet_desc = pet_desc;
        context = con;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return pet_picture.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Row {
        ImageView missingpet_img;
        TextView missingpet_name;
        TextView missingpet_desc;
        Button check_details;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Row row = new Row();
        View rowView = inflater.inflate(R.layout.custom_list, null);

        row.missingpet_img = (ImageView) rowView.findViewById(R.id.missingpet_img);
        row.missingpet_name = (TextView) rowView.findViewById(R.id.missingpet_name);
        row.missingpet_desc = (TextView) rowView.findViewById(R.id.missingpet_desc);

        Picasso.with(context).load(pet_picture.get(position)).into(row.missingpet_img);
        row.missingpet_name.setText(pet_name.get(position));
        row.missingpet_desc.setText(pet_desc.get(position));

        return rowView;
    }
}
