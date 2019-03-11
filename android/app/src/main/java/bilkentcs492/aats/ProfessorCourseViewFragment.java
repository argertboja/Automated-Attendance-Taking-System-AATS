package bilkentcs492.aats;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * This fragment shows the current course taking place and the attendance results as fetched from
 * the online recognition data
 * A simple {@link Fragment} subclass.
 */
public class ProfessorCourseViewFragment extends Fragment {


    public ProfessorCourseViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  (View) inflater.inflate(R.layout.fragment_professor_course_view, container, false);

        GridView gridView = (GridView) v.findViewById(R.id.grid_view);

        StudentsGridViewAdaptor adaptor = new StudentsGridViewAdaptor(getActivity(), R.layout.grid_item_layout,getStudentData(new ArrayList<Student> ()) );
        gridView.setAdapter(adaptor);
        gridView.setOnItemClickListener(new GridViewClickListener(getActivity()));
        return v;
    }

    /**
     *
     * @param studentList : a list of all the students enrolled in class
     * @return returns an arraylist of ImageItem type, containing an image bitmap related to the
     * student and small info on his name/id on a string
     */
    private ArrayList<ImageItem> getStudentData(ArrayList<Student> studentList){
        // currently add dummy data
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        String filename = "mona.jpg";

        InputStream ims = null;
        try {
            ims = getActivity().getAssets().open( filename);
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();

        }

//        Bitmap bitmap = getRoundedCornerBitmap(BitmapFactory.decodeStream(ims));
        Bitmap bitmap = BitmapFactory.decodeStream(ims);
        if(bitmap==null)
            bitmap =BitmapFactory.decodeStream(ims);
        imageItems.add(new ImageItem(bitmap, filename));
        try {
            ims.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 20; i++){
            imageItems.add(new ImageItem(bitmap, filename));
        }
        return imageItems;
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        if(bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = 35;
            final Rect topRightRect = new Rect(bitmap.getWidth() / 2, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
            final Rect bottomRect = new Rect(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            // Fill in upper right corner
            //  canvas.drawRect(topRightRect, paint);
            // Fill in bottom corners
            canvas.drawRect(bottomRect, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        }else return null;
    }

    /**
     *  This class implements the OnItem Click listener for the grid items
     *  when user taps on the image of a student they may choose to marke them present or not
     *  as per the students objection
     */
    class GridViewClickListener implements AdapterView.OnItemClickListener{
        Context context;

        GridViewClickListener(Context cnt){
            context = cnt;
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            Toast.makeText(context,"Hello there clicker", Toast.LENGTH_SHORT);
//            Intent intent = new Intent(context, dsfdssfvw3cx.class);
//            ImageView imageThumbnail =(ImageView) v.findViewById(R.id.imageThumbnail);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, imageThumbnail, "smooth_Details");
//            intent.putExtra("image",listOfFoods.get(position));
//            startActivity(intent,options.toBundle());
        }
    }

}
