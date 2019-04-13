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
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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

import static android.app.Activity.RESULT_OK;


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
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String serverResponse;
    ImageView test;
    String objection_picture_path;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int BITMAP_SMALL_WIDTH = 1200;
    private static final int BITMAP_SMALL_HEIGHT = 1600;
    private final String UPLOAD_URL = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/upload_image.php";
    private final String GET_STUDENT_LIST_URL = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/retrieve_professors_course.php";
    private String professorId ;
    private Context currentContext;

    public ProfessorCourseViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  (View) inflater.inflate(R.layout.fragment_professor_course_view, container, false);
        currentContext = rootView.getContext();
        searchBar = (EditText) rootView.findViewById(R.id.search_bar) ;
        TextView courseID = rootView.findViewById(R.id.course_ID);
        if (getArguments() != null) {
            professorId = getArguments().getString("user_id");
            courseID.setText(professorId);
        }
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
//        grid_width = gridView.getWidth();
//        int height = gridView.getHeight();

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
     * EDIT THIS, GET FROM DATABASE
     * @param studentList : a list of all the students enrolled in class
     * @return returns an arraylist of ImageItem type, containing an image bitmap related to the
     * student and small info on his name/id on a string
     */
    private ArrayList<ImageItem> getStudentData(ArrayList<Student> studentList){
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
                params.add(new QueryParameter("professorID", professorId));
                retrieve_professors_course_data.setParams(params);

                JSONArray receiveData = retrieve_professors_course_data.requestAndFetch();

                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = (size.x - 60)/4;
                Log.e("__=__", width+"");

//                int height = size.y;

                for (int i = 0; i < receiveData.length(); i++) {
                    JSONObject json_data = null;
                    try {
                        json_data = receiveData.getJSONObject(i);

                        String studentID = json_data.getString("studentID");
                        String base64 = json_data.getString("base64");
                        base64 = base64.substring(base64.indexOf(",") + 1);
                        final byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//                        decodedBitmap = getRoundedCornerBitmap(decodedBitmap);
                        Log.e("studentID", studentID);




                        Log.e("_______width", width+"");
                        decodedBitmap = Bitmap.createScaledBitmap(decodedBitmap, 125, 120, false);
                        decodedBitmap = getRoundedCornerBitmap(decodedBitmap, 12);
                        imageItems.add(new ImageItem(decodedBitmap, studentID, false));
                    } catch (JSONException e) {
                        Log.e("JSONException", "Error Parsing Student Data");
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(currentContext, "Error Parsing Student Data", Toast.LENGTH_LONG).show();
                            }
                        });
                        e.printStackTrace();
                    }
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



//        String filename = "21500342";
//        Log.e("Ckemi","oooooooooooo123");
//
//        InputStream ims = null;
//        try {
//            ims = getActivity().getAssets().open( "mona.jpg");
//        } catch (NullPointerException | IOException e) {
//            Log.e("ERROR 1",imageItems.get(19).getStudentID());
//
//            e.printStackTrace();
//
//        }
//
////        Bitmap bitmap = getRoundedCornerBitmap(BitmapFactory.decodeStream(ims));
//            Bitmap bitmap = getRoundedCornerBitmap(BitmapFactory.decodeStream(ims));
//        if(bitmap==null)
//            bitmap =BitmapFactory.decodeStream(ims);
//        try {
//            ims.close();
//        } catch (IOException e) {
//            Log.e("ERROR 2",imageItems.get(19).getStudentID());
//
//            e.printStackTrace();
//        }
//
//        for (int i = 0; i < 50; i++){
//            imageItems.add(new ImageItem(bitmap, filename,true));
//        }
//        imageItems.add(new ImageItem(bitmap, "21500010",false));
//        imageItems.add(new ImageItem(bitmap, "21500009",false));
//
//        Log.e("Ckemi",imageItems.get(19).getStudentID());




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

//            Intent intent = new Intent(context, dsfdssfvw3cx.class);
//            ImageView imageThumbnail =(ImageView) v.findViewById(R.id.imageThumbnail);
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context, imageThumbnail, "smooth_Details");
//            intent.putExtra("image",listOfFoods.get(position));
//            startActivity(intent,options.toBundle());
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
                    Uri photoURI = FileProvider.getUriForFile(rootView.getContext(),
                            "bilkentcs492.aats",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("_________ ERROR: "+resultCode,"Error " + requestCode);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(objection_picture_path, options);

            Toast.makeText(getActivity(),"Picture Taken Successfully!", Toast.LENGTH_SHORT).show();
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
                    params.add(new QueryParameter("image",image_str ));
                    params.add(new QueryParameter("filename",student_objection_ID + ".jpg" ));
                    uploadRequest.setParams(params);

                    uploadRequest.uploadRequest(getActivity());

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
        File storageDir = getContext().getExternalFilesDir("student_objections");
        File image = new File(storageDir, imageFileName + ".jpg");

        // createtempfile adds timestamp to file name - problem
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );

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
