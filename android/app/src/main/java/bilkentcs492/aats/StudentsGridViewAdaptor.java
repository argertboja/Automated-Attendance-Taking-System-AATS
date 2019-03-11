package bilkentcs492.aats;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  03/11/2019
 * Time        : 11 | 16: 14 of 03 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentsGridViewAdaptor extends ArrayAdapter<ImageItem> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    private int lastPosition = -1;


    StudentsGridViewAdaptor(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            row.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.student_image);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = data.get(position);
        if(item.getTitle().length()>8)
            holder.imageTitle.setText(String.format("%s...", item.getTitle().substring(0, 8)));
        else
            holder.imageTitle.setText(item.getTitle());

        holder.image.setImageBitmap(item.getImage());

        lastPosition = position;
        return row;
    }

    private static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}
