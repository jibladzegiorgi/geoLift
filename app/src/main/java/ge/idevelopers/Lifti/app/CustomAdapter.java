package ge.idevelopers.Lifti.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 22.01.2017.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    private final Integer[] imgid;

    public CustomAdapter(Context context, String[] menu_Item, Integer[] imgid) {
        super(context, R.layout.list_row ,menu_Item);
        this.imgid = imgid;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater lado = LayoutInflater.from(getContext());
        View customView = lado.inflate(R.layout.list_row,parent,false);
        String singleMenuItem = getItem(position);
        TextView menuText = (TextView) customView.findViewById(R.id.tv_menuText);
        ImageView menuImg  = (ImageView) customView.findViewById(R.id.itemImage);
        menuText.setText(singleMenuItem);
        menuImg.setImageResource(imgid[position]);
        return customView;
    }
}
