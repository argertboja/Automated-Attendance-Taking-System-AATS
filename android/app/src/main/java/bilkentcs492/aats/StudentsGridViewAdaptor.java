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
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
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
    private ArrayList<ImageItem> data; // contains user image and id number
    private ArrayList<ImageItem> filterData; // duplicate array for filtering

    private int lastPosition = -1;


    StudentsGridViewAdaptor(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        filterData = new ArrayList<ImageItem>();
        this.filterData.addAll(data); // filterData will be holding all elements to aid in the search impl

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
            holder.imageTitle = (TextView) row.findViewById(R.id.student_id_text);
            holder.image = (ImageView) row.findViewById(R.id.student_image);
            holder.presentState = (TextView) row.findViewById(R.id.student_presence);
//            holder.group = (LinearLayout) row.findViewById(R.id.group_image);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        ImageItem item = data.get(position);
        if(item.getStudentID().length()>8)
            holder.imageTitle.setText(String.format("%s...", item.getStudentID().substring(0, 8)));
        else
            holder.imageTitle.setText(item.getStudentID());
        String presenceText = (item.isPresent()) ? "Present" : "Absent";
        String colorCode = (item.isPresent()) ? "#1CCF09" : "#D6032D";
        holder.presentState.setText(presenceText);
        holder.presentState.setTextColor(Color.parseColor(colorCode));
//        holder.group.setBackgroundColor(Color.parseColor(colorCode));
        holder.image.setImageBitmap(item.getImage());

        lastPosition = position;
        return row;
    }

    private static class ViewHolder {
        TextView imageTitle;
        TextView presentState;
        ImageView image;
//        LinearLayout group;
    }

    /**
     * This method filters the items on the gridview and updates the gridview accordignly
     * @param searchKey : the input on the search bar
     */
    public void filter(String searchKey) {

         // empty arraylist initially upon starting search
         data.clear();
        Log.e("clear text=",searchKey);

        //if empty query show all elements
        if (searchKey.length() == 0) {
            data.addAll(filterData);
            Log.e("clear text length0=",searchKey);

        } else {

            //loop through present data and filter all the data, then refresh the data array
            for (int i = 0; i < filterData.size();i++) {
                Log.e("____inside loop",searchKey);

                if (filterData.get(i).getStudentID().toLowerCase().contains(searchKey)) {
                    data.add(filterData.get(i));
                }

            }
            Log.e("+++++outtt loop",filterData.size()+")");

        }

        notifyDataSetChanged(); // update gridview with the newly changed "data" variable
    }


}
