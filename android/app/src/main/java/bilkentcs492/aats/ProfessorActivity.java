package bilkentcs492.aats;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ProfessorActivity extends AppCompatActivity {

    TextView presentNum;
    TextView absentNum;
    TextView totalNum;
    TextView percentage_num;
    TextView professor_name;
    TextView professor_surnname;
    TextView current_hour;
    ProgressBar progressBar;
    EditText searchBar;
    private GridView gridView;
    Professor professor;
    private StudentsGridViewAdaptor adapter;
    private String student_objection_ID;
    ArrayList<ImageItem> studentList;
    private String objection_picture_path;
    ArrayList<ImageItem> items;
    private String objection_picutre_base64;
    private int number_students_present;
    private int number_students_absent;
    private int number_students_total;
    private int percentage_ratio;
    private static final int REQUEST_IMAGE_CAPTURE  = 1;
    private static final int BITMAP_SMALL_WIDTH     = 1000;
    private static final int BITMAP_SMALL_HEIGHT    = 1200;
    private final String MARK_ABSENT_URL                 = "https://bilmenu.com/aats/php/mark_student_absent.php";
    private final String UPLOAD_URL                 = "https://bilmenu.com/aats/php/upload_image.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);

        professor  = (Professor) getIntent().getExtras().getSerializable("ProfessorData");;

        searchBar = (EditText) findViewById(R.id.search_bar) ;
        TextView courseID = findViewById(R.id.course_ID);
        presentNum = findViewById(R.id.present_num);
        absentNum = findViewById(R.id.absent_num);
        totalNum = findViewById(R.id.total_num);
        percentage_num = findViewById(R.id.percentage_num);
        TextView date = findViewById(R.id.date);
        progressBar = findViewById(R.id.progressBar);
        professor_name = findViewById(R.id.professor_name);
        professor_surnname = findViewById(R.id.professor_surname);
        current_hour = findViewById(R.id.current_hour);

        //set up grid view data
        gridView = (GridView) findViewById(R.id.grid_view);

        items = professor.getListOfCurrentStudents();
        studentList = items;
        adapter = new StudentsGridViewAdaptor(ProfessorActivity.this, R.layout.grid_item_layout ,items);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new GridViewClickListener(ProfessorActivity.this));

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.filter( charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //SET TEXT DATA
        courseID.setText(professor.getCurrentCourse());
        number_students_present = professor.getNum_present_students() ;
        number_students_absent = professor.getNum_absent_students();
        number_students_total = number_students_present + number_students_absent;
        percentage_ratio = professor.getAttendance_percentage() ;

        presentNum.setText(String.valueOf(number_students_present));
        absentNum.setText(String.valueOf(number_students_absent));
        totalNum.setText(String.valueOf(number_students_total));
        percentage_num.setText(String.valueOf(percentage_ratio) + "%");
        professor_name.setText(professor.getUser_name());
        professor_surnname.setText(professor.getUser_surname());
        current_hour.setText(professor.getCurrent_hour());
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
            AlertDialog.Builder builder =  new AlertDialog.Builder(context);

                    builder.setTitle("Mark Presence");
                    builder.setMessage("Mark Student with id : " + clicked_ID + " as ?");

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    if(studentList.get(position).isPresent()){
                         builder.setNegativeButton(R.string.absent, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                student_objection_ID = clicked_ID;
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                                }
                                markStudentAbsent();
                            }
                        });
                    }else{
                        builder.setPositiveButton(R.string.present, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Take a Picture of the student", Toast.LENGTH_SHORT).show();
                                // take a picture here
                                student_objection_ID = clicked_ID;
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                                }
                                dispatchTakePictureIntent();
                            }
                        });
                    }

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    builder.setIcon(android.R.mipmap.sym_def_app_icon);
                    builder.show();
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

    // UPLOAD AND MARK PRESENCE HAPPENS HERE
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(objection_picture_path, options);

            Toast.makeText(getApplicationContext(),"Picture Taken Successfully!", Toast.LENGTH_SHORT).show();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap = scaleDownToRatio(bitmap);

            //bitmap = convert2Grayscale(bitmap);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream); //compress to which format you want.

            byte [] byte_arr = stream.toByteArray();
            final String image_str= new String(android.util.Base64.encode(byte_arr, android.util.Base64.DEFAULT));

            objection_picutre_base64 = image_str;

            // new thread because doing a HTTP request
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    ServerRequest uploadRequest = new ServerRequest();
                    // set URL
                    uploadRequest.setURL(UPLOAD_URL);

                    // set Parameters .. ex: ?id=1
                    List<QueryParameter> params = new ArrayList<>();
                    params.add(new QueryParameter("ID",professor.getUser_ID() ));
                    params.add(new QueryParameter("password", professor.getUser_password() ));

                    params.add(new QueryParameter("image",image_str ));
                    params.add(new QueryParameter("filename",""+student_objection_ID  ));
                    uploadRequest.setParams(params);

                    JSONArray receiveResponse = uploadRequest.requestAndFetch(ProfessorActivity.this);

                    if(receiveResponse != null){
                        JSONObject object;
                        String responseText ="";
                        try {
                            object = receiveResponse.getJSONObject(0);
                            responseText = object.optString("response");
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), "Upload Response failed to parse: " + e.getMessage() , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if(responseText != null){ // print response
                            final String responseText_const = responseText;
                            Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), responseText_const , Toast.LENGTH_LONG).show();
                                    updatePresenceOnUI(true);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }else{
                            Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), "Upload Response failed to parse: " , Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }else{ // no internet probably
                        Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( getApplicationContext(), "INTERNET CONNECTION ERROR" , Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
            t.start();


        }

    }

    //MARK STUDENT AS ABSENT
    private void markStudentAbsent(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerRequest uploadRequest = new ServerRequest();
                // set URL
                uploadRequest.setURL(MARK_ABSENT_URL);

                // set Parameters .. ex: ?id=1
                List<QueryParameter> params = new ArrayList<>();
                params.add(new QueryParameter("ID",professor.getUser_ID() ));
                params.add(new QueryParameter("password", professor.getUser_password() ));
                params.add(new QueryParameter("studentID","" + student_objection_ID  ));
                uploadRequest.setParams(params);

                JSONArray receiveResponse = uploadRequest.requestAndFetch(ProfessorActivity.this);

                if(receiveResponse != null){
                    JSONObject object;
                    String responseText ="";
                    try {
                        object = receiveResponse.getJSONObject(0);
                        responseText = object.optString("response");
                    } catch (final JSONException e) {
                        e.printStackTrace();
                        Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( getApplicationContext(), "Mark absent response failed to parse: " + e.getMessage() , Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if(responseText != null){ // print response
                        final String responseText_const = responseText;
                        Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( getApplicationContext(), responseText_const , Toast.LENGTH_LONG).show();
                                updatePresenceOnUI(false);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText( getApplicationContext(), "Mark Absent response failed to parse: " , Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }else{ // no internet probably
                    Objects.requireNonNull(ProfessorActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getApplicationContext(), "INTERNET CONNECTION ERROR" , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        t.start();
    }

    /**
     * Update text view-s and progress bars accordingly
     * @param present : if true : mark objection-making student as present, else mark him as absent
     */
    private void updatePresenceOnUI(boolean present){
        int index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getStudentID().equals(student_objection_ID)) {
                    index = i;
                }
            }

        if(index > -1) {
            if (present) {
                items.get(index).setPresent(true);
                if(!(number_students_present == number_students_total) ) {
                    number_students_present++;
                    number_students_absent--;

                    professor.setNum_present_students(number_students_present);
                    professor.setNum_absent_students(number_students_absent);
                    professor.setAttendance_percentage();
                    percentage_ratio = professor.getAttendance_percentage();
                    items.get(index).setImage(objection_picutre_base64);
                }

            } else {
                items.get(index).setPresent(false);
                if(!(number_students_absent == number_students_total) ) {
                    number_students_present--;
                    number_students_absent++;
                    professor.setNum_present_students(number_students_present);
                    professor.setNum_absent_students(number_students_absent);
                    professor.setAttendance_percentage();
                    percentage_ratio = professor.getAttendance_percentage();
                }

            }
            presentNum.setText(String.valueOf(number_students_present));
            absentNum.setText(String.valueOf(number_students_absent));
            totalNum.setText(String.valueOf(number_students_total));
            String ratio = percentage_ratio + "%";
            percentage_num.setText(ratio);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                progressBar.setProgress(percentage_ratio,true);
            }else{
                progressBar.setProgress(percentage_ratio);
            }


        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent logout = new Intent(ProfessorActivity.this, LoginActivity.class);
            Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_LONG).show();
            startActivity(logout);
            finish();
        }else if (id == R.id.infoButton){
            Intent logout = new Intent(ProfessorActivity.this, Information.class);
            Toast.makeText(getApplicationContext(), "App Info", Toast.LENGTH_LONG).show();
            startActivity(logout);
        }
        return super.onOptionsItemSelected(item);
    }
}
