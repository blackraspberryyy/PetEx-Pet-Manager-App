package com.codebusters.petexmanager.petexpetmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomListDiscovery extends BaseAdapter{
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
    List<String> owner_picture = new ArrayList<>();
    List<String> owner_name = new ArrayList<>();
    List<String> discovery_location = new ArrayList<>();

    private static LayoutInflater inflater = null;

    public CustomListDiscovery(Context con, List<String> owner_picture, List<String> owner_name, List<String> discovery_location) {
        this.owner_picture = owner_picture;
        this.owner_name = owner_name;
        this.discovery_location = discovery_location;
        context = con;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return owner_picture.size();
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
        CircleImageView discovery_user_img;
        TextView discovery_owner_info;
        TextView discovery_location;
        Button check_details;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Row row = new Row();
        View rowView = inflater.inflate(R.layout.custom_list, null);

        row.discovery_user_img = (CircleImageView) rowView.findViewById(R.id.missingpet_img);
        row.discovery_owner_info = (TextView) rowView.findViewById(R.id.missingpet_name);
        row.discovery_location = (TextView) rowView.findViewById(R.id.missingpet_desc);

        Picasso.with(context).load(owner_picture.get(position)).into(row.discovery_user_img);
        row.discovery_owner_info.setText(owner_name.get(position));
        row.discovery_location.setText(discovery_location.get(position));

        return rowView;
    }
}
