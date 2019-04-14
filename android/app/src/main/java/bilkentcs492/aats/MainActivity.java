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
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private String user_id ;
    private String user_password;
    private String user_name;
    private String user_surname;
    private String user_email;
    private String current_course;
    private String presence;

    private EditText searchBar;
    private TextView courseID;

    // Professor
    private GridView gridView;
    ArrayList<ImageItem> studentList;
    private static final int REQUEST_IMAGE_CAPTURE  = 1;
    private static final int BITMAP_SMALL_WIDTH     = 1200;
    private static final int BITMAP_SMALL_HEIGHT    = 1600;
    private final String UPLOAD_URL                 = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/upload_image.php";
    private final String GET_STUDENT_LIST_URL       = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/retrieve_professors_course.php";
    private String objection_picture_path;
    private String student_objection_ID; // CURRENT STUDENT BEING MARKED AS PRESENT MANUALLY
    private StudentsGridViewAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        TextView hello = (TextView) findViewById(R.id.hellworld);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        // part of code to change nav header email
//        View headerView = navigationView.getHeaderView(0);
//        TextView navUserEmail = (TextView) headerView.findViewById(R.id.user_email);
//        TextView navUserName = (TextView) headerView.findViewById(R.id.user_name);
//        String temp_user_email, temp_user_name, temp_user_surname;

        user_id         = (getIntent().getExtras().getString("user_id"));
        user_password   = (getIntent().getExtras().getString("user_password"));
        user_email      = (getIntent().getExtras().getString("user_email"));
        user_name       = (getIntent().getExtras().getString("user_name"));
        user_surname    = (getIntent().getExtras().getString("user_surname"));
        presence        = (getIntent().getExtras().getString("user_presence"));
        if(presence.equals("-1")) {
            current_course = (getIntent().getExtras().getString("professor_current_course"));
            searchBar = (EditText) findViewById(R.id.search_bar) ;
            courseID = findViewById(R.id.course_ID);
            courseID.setText(current_course);
            gridView = (GridView) findViewById(R.id.grid_view);
            studentList = getStudentData();
            adapter = new StudentsGridViewAdaptor(MainActivity.this, R.layout.grid_item_layout,studentList );
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new GridViewClickListener(MainActivity.this));

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

        }else{
            current_course = (getIntent().getExtras().getString("student_current_course"));
        }

//        temp_user_email = (getIntent().getExtras().getString("user_email") != null) ? getIntent().getExtras().getString("user_email") : "userEmail";
//        temp_user_name = (getIntent().getExtras().getString("user_name") != null) ? getIntent().getExtras().getString("user_name") : "userName";
//        temp_user_surname = (getIntent().getExtras().getString("user_surname") != null) ? getIntent().getExtras().getString("user_surname") : "userSurname";
//
//        navUserEmail.setText(temp_user_email);
//        navUserName.setText(temp_user_name);
       // hello.setText("Hello "+temp_user_name +" " + temp_user_surname + "! Check app drawer");

    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_professor) {
//            setTitle("Course Info");
//            Bundle bundle = new Bundle();
//            bundle.putString("user_id", (getIntent().getExtras().getString("user_id")));
//            bundle.putString("user_password",  (getIntent().getExtras().getString("user_password")));
//
//            bundle.putString("professor_current_course",  (getIntent().getExtras().getString("professor_current_course")));
//
//            ProfessorCourseViewFragment course_info_tab = new ProfessorCourseViewFragment();
//            course_info_tab.setArguments(bundle);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.main_fragment, course_info_tab).commit();
//        } else if (id == R.id.nav_info) {
//            setTitle("App Info");
//            AppInfoTabFragment app_info_tab = new AppInfoTabFragment();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.main_fragment, app_info_tab).commit();
//        } else if (id == R.id.nav_settings) {
//            setTitle("Student Info");
//            Bundle bundle = new Bundle();
//            bundle.putString("user_id", (getIntent().getExtras().getString("user_id")));
//            bundle.putString("user_password",  (getIntent().getExtras().getString("user_password")));
//            StudentCourseViewFragment student_info_tab = new StudentCourseViewFragment();
//            student_info_tab.setArguments(bundle);
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.main_fragment, student_info_tab).commit();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    /**
     *
     * @return returns an arraylist of ImageItem type, containing an image bitmap related to the
     * student and small info on his name/id on a string
     */
    private ArrayList<ImageItem> getStudentData(){
        // currently add dummy data
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                ServerRequest retrieve_professors_course_data = new ServerRequest();
                // set URL
                retrieve_professors_course_data.setURL(GET_STUDENT_LIST_URL);

                // set Parameters .. ex: ?id=1
                List<QueryParameter> params = new ArrayList<>();
                params.add(new QueryParameter("ID", user_id));
                params.add(new QueryParameter("password", user_password));
                retrieve_professors_course_data.setParams(params);

                Log.e("()",user_id + "_" + user_password    );
                JSONArray receiveData = retrieve_professors_course_data.requestAndFetch(MainActivity.this);
                if( receiveData != null) {
                    for (int i = 0; i < receiveData.length(); i++) {
                        try {
                            JSONObject json_data = receiveData.getJSONObject(i);

                            String studentID = json_data.optString("studentID");
                            String base64 = json_data.optString("base64");
                            base64 = base64.substring(base64.indexOf(",") + 1);
                            final byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                            //                        decodedBitmap = getRoundedCornerBitmap(decodedBitmap);


                            decodedBitmap = Bitmap.createScaledBitmap(decodedBitmap, 125, 120, false);
                            decodedBitmap = getRoundedCornerBitmap(decodedBitmap, 12);
                            imageItems.add(new ImageItem(decodedBitmap, studentID, false));
                        } catch (JSONException e) {
                            Log.e("JSONException", "Error Parsing Student Data");
                            Objects.requireNonNull(MainActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error Parsing Student Data", Toast.LENGTH_LONG).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }else{
                    Objects.requireNonNull(MainActivity.this).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( getApplicationContext(), "SERVER CONNECTION ERROR" , Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

        });
        t.start();
        Log.e("size ===", imageItems.size()+"");
        while(t.isAlive()){
            // do nothing
        }
        return imageItems;

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float smoothness) {
        if(bitmap != null) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
//            final float roundPx = smoothness;
            final Rect topRightRect = new Rect(bitmap.getWidth() / 2, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
            final Rect bottomRect = new Rect(0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, smoothness, smoothness, paint);
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

    // UPLOAD HAPPENS HERE
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("_________ ERROR: "+resultCode,"Error " + requestCode);

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

            // new thread because doing a HTTP request
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {

                    ServerRequest uploadRequest = new ServerRequest();
                    // set URL
                    uploadRequest.setURL(UPLOAD_URL);

                    // set Parameters .. ex: ?id=1
                    List<QueryParameter> params = new ArrayList<>();
                    params.add(new QueryParameter("ID",user_id ));
                    params.add(new QueryParameter("password",user_password ));
                    Log.e("pasw: " , user_id +"_" +user_password);
                    params.add(new QueryParameter("image",image_str ));
                    params.add(new QueryParameter("filename",""+student_objection_ID + ".jpg" ));
                    uploadRequest.setParams(params);

                    JSONArray receiveResponse = uploadRequest.requestAndFetch(MainActivity.this);

                    if(receiveResponse != null){
                        JSONObject object;
                        String responseText ="";
                        try {
                            object = receiveResponse.getJSONObject(0);
                            responseText = object.optString("response");
                        } catch (final JSONException e) {
                            e.printStackTrace();
                            Objects.requireNonNull(MainActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), "Upload Response failed to parse: " + e.getMessage() , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        if(responseText != null){ // print response
                            final String responseText_const = responseText;
                            Objects.requireNonNull(MainActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), responseText_const , Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Objects.requireNonNull(MainActivity.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText( getApplicationContext(), "Upload Response failed to parse: " , Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }else{ // no internet probably
                        Objects.requireNonNull(MainActivity.this).runOnUiThread(new Runnable() {
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
