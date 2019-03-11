package bilkentcs492.aats;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static bilkentcs492.aats.ProfessorCourseViewFragment.GridViewClickListener.REQUEST_IMAGE_CAPTURE;


/**
 * This fragment shows the current course taking place and the attendance results as fetched from
 * the online recognition data
 * A simple {@link Fragment} subclass.
 */
public class ProfessorCourseViewFragment extends Fragment {

    public View rootView;
    Bundle args;
    ArrayList<ImageItem> studentList;
    private GridView gridView;
    private EditText searchBar;
    private StudentsGridViewAdaptor adapter;
    private String student_objection_ID; // CURRENT STUDENT BEING MARKED AS PRESENT MANUALLY

    public ProfessorCourseViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  (View) inflater.inflate(R.layout.fragment_professor_course_view, container, false);
        searchBar = (EditText) rootView.findViewById(R.id.search_bar) ;
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        studentList = getStudentData(new ArrayList<Student> ());
        adapter = new StudentsGridViewAdaptor(getActivity(), R.layout.grid_item_layout,studentList );
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new GridViewClickListener(getActivity()));

        // Add on text change listener for the search bar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e("edit text=",charSequence+"_");
                adapter.filter( charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return rootView;
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
        String filename = "21500342";
        Log.e("Ckemi","oooooooooooo123");

        InputStream ims = null;
        try {
            ims = getActivity().getAssets().open( "mona.jpg");
        } catch (NullPointerException | IOException e) {
            Log.e("ERROR 1",imageItems.get(19).getStudentID());

            e.printStackTrace();

        }

//        Bitmap bitmap = getRoundedCornerBitmap(BitmapFactory.decodeStream(ims));
        Bitmap bitmap = getRoundedCornerBitmap(BitmapFactory.decodeStream(ims));
        if(bitmap==null)
            bitmap =BitmapFactory.decodeStream(ims);
        try {
            ims.close();
        } catch (IOException e) {
            Log.e("ERROR 2",imageItems.get(19).getStudentID());

            e.printStackTrace();
        }

        for (int i = 0; i < 50; i++){
            imageItems.add(new ImageItem(bitmap, filename,true));
        }
        imageItems.add(new ImageItem(bitmap, "21500010",false));
        imageItems.add(new ImageItem(bitmap, "21500009",false));

        Log.e("Ckemi",imageItems.get(19).getStudentID());

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
        public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
            final String clicked_ID = studentList.get(position).getStudentID();
             new AlertDialog.Builder(context)
                    .setTitle("Mark Presence")
                    .setMessage("Mark Student with id : "  + clicked_ID +" as ?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.present, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Take a Picture", Toast.LENGTH_SHORT).show();
                            // take a picture here
                            student_objection_ID = clicked_ID;
                            dispatchTakePictureIntent();
                        }
                    })

                    .setNegativeButton(R.string.absent, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"MARKED ABSENT", Toast.LENGTH_SHORT).show();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

//            Intent intent = new Intent(context, dsfdssfvw3cx.class);
//            ImageView imageThumbnail =(ImageView) v.findViewById(R.id.imageThumbnail);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, imageThumbnail, "smooth_Details");
//            intent.putExtra("image",listOfFoods.get(position));
//            startActivity(intent,options.toBundle());
        }

        static final int REQUEST_IMAGE_CAPTURE = 1;

        public void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Toast.makeText(getActivity(),"Picture taken and sent to server", Toast.LENGTH_SHORT).show();

            // SEND BITMAP TO SERVER AND MARK STUDENT AS PRESENT ON SERVER SIDE THROUGH HERE

//                imageView.setImageBitmap(imageBitmap);
        }
    }

}
