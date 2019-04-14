package bilkentcs492.aats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfessorActivity extends AppCompatActivity {

    private GridView gridView;
    private StudentsGridViewAdaptor adapter;
    private String student_objection_ID;
    ArrayList<ImageItem> studentList;
    private String objection_picture_path;
    private static final int REQUEST_IMAGE_CAPTURE  = 1;
    private static final int BITMAP_SMALL_WIDTH     = 1200;
    private static final int BITMAP_SMALL_HEIGHT    = 1600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        Professor professor  = (Professor) getIntent().getExtras().getSerializable("ProfessorData");;

        if(professor == null){
            Log.e("NULL","nullllll");
        }
        EditText searchBar = (EditText) findViewById(R.id.search_bar) ;
        TextView courseID = findViewById(R.id.course_ID);
        TextView presentNum = findViewById(R.id.present_num);
        TextView absentNum = findViewById(R.id.absent_num);
        TextView totalNum = findViewById(R.id.total_num);
        TextView percentage_num = findViewById(R.id.percentage_num);
        TextView date = findViewById(R.id.date);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        //set up grid view data
        gridView = (GridView) findViewById(R.id.grid_view);
        if(professor.getCurrentCourse() == null){
            Log.e("NULL","");
        }
        Log.e("balueeee",""+professor.getCurrentCourse());
        ArrayList<ImageItem> items = professor.getListOfCurrentStudents();
        Log.e("size--=",""+items.size());
        adapter = new StudentsGridViewAdaptor(ProfessorActivity.this, R.layout.grid_item_layout ,items);
        gridView.setOnItemClickListener(new GridViewClickListener(ProfessorActivity.this));
        gridView.setAdapter(adapter);
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

        courseID.setText(professor.getCurrentCourse());
        presentNum.setText(professor.getNum_present_students()+"");
        absentNum.setText(professor.getNum_absent_students()+"");
        totalNum.setText(professor.getNum_total_students()+"");
        String percentage = professor.getAttendance_percentage()+"";
        percentage_num.setText(percentage);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c);
        date.setText(formattedDate);

        progressBar.setProgress((int)professor.getAttendance_percentage());
    }

    /**
     *  This class implements the OnItem Click listener for the grid items
     *  when user taps on the image of a student they may choose to marke them present or not
     *  as per the students objection
     */
    class GridViewClickListener implements AdapterView.OnItemClickListener {
        Context context;
        static final int REQUEST_TAKE_PHOTO = 1;
        GridViewClickListener(Context cnt) {
            context = cnt;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
            final String clicked_ID = studentList.get(position).getStudentID();
            new AlertDialog.Builder(context)
                    .setTitle("Mark Presence")
                    .setMessage("Mark Student with id : " + clicked_ID + " as ?")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(R.string.present, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Take a Picture", Toast.LENGTH_SHORT).show();
                            // take a picture here
                            student_objection_ID = clicked_ID;
                            dispatchTakePictureIntent();
                        }
                    })

                    .setNegativeButton(R.string.absent, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "MARKED ABSENT", Toast.LENGTH_SHORT).show();
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }


        void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile(student_objection_ID + "");
                } catch (IOException ex) {
                    // Error occurred while creating the File

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "bilkentcs492.aats",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    /**
     *
     * @return an Empty Image File Object with location and filename upon which we'll
     * unleash our camera taken jpeg image
     * taken picture
     * @throws IOException
     */
    private File createImageFile(String imageFileName) throws IOException {
        File storageDir = getApplicationContext().getExternalFilesDir("student_objections");
        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        objection_picture_path = image.getAbsolutePath();
        return image;
    }

    /**
     *
     * @param bitmap : Original colored bitmap
     * @return grayscale version of the original
     */
    private Bitmap convert2Grayscale(Bitmap bitmap){
        Bitmap grayscale_btmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(grayscale_btmp);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return grayscale_btmp;
    }

    /**
     *
     * @param bitmap : Original scale bitmap
     * @return : New Bitmap scaled down to fixed smaller ration
     */
    private Bitmap scaleDownToRatio(Bitmap bitmap){
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, BITMAP_SMALL_WIDTH, BITMAP_SMALL_HEIGHT), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

}
