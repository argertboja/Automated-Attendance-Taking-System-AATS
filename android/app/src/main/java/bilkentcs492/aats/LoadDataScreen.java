package bilkentcs492.aats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LoadDataScreen extends AppCompatActivity {
    private String userType;
    private final String TYPE_PROFESSOR = "-1";
    private final String TYPE_STUDENT_ABSENT = "0";
    private final String TYPE_STUDENT_PRESENT = "1";

    //profi
    private static final int REQUEST_IMAGE_CAPTURE  = 1;
    private static final int BITMAP_SMALL_WIDTH     = 1200;
    private static final int BITMAP_SMALL_HEIGHT    = 1600;
    private final String UPLOAD_URL                 = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/upload_image.php";
    private final String GET_STUDENT_LIST_URL       = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/retrieve_professors_course.php";
    private String objection_picture_path;
    Professor professor;
    User student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data_screen);

        userType        = (getIntent().getExtras().getString("user_presence"));
        if(userType.equals(TYPE_PROFESSOR)) {
            ProfessorTask task =  new ProfessorTask();
            task.execute("");
        }else if(userType.equals(TYPE_STUDENT_ABSENT) || userType.equals(TYPE_STUDENT_PRESENT) ){
            //
        }

    }



    private  class ProfessorTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            //some heavy processing resulting in a Data String
            String user_id         = (getIntent().getExtras().getString("user_id"));
            String user_password   = (getIntent().getExtras().getString("user_password"));
            String user_email      = (getIntent().getExtras().getString("user_email"));
            String user_name       = (getIntent().getExtras().getString("user_name"));
            String user_surname    = (getIntent().getExtras().getString("user_surname"));
            String  current_course = (getIntent().getExtras().getString("professor_current_course"));
            if(user_id != null && user_password != null && user_email !=null && user_name != null && user_surname != null && current_course != null) {
                 professor = new Professor(user_id, user_password, user_name, user_surname, user_email);
                ((Professor) professor).setCurrentCourse(current_course);

                ArrayList<ImageItem> listOfStudents = getStudentData(professor.getUser_ID(), professor.getUser_password());
                Log.e("empty__",(listOfStudents == null) + "" );
                if(listOfStudents != null) {
                    ((Professor) professor).setListOfCurrentStudents(listOfStudents);
                    ((Professor) professor).setNum_present_students(getNumberOfPresentStudents(listOfStudents));
                    ((Professor) professor).setNum_total_students(listOfStudents.size());
                    ((Professor) professor).setNum_absent_students(((Professor) professor).getNum_total_students() - ((Professor) professor).getNum_present_students());
                    ((Professor) professor).setAttendance_percentage((double)((Professor) professor).getNum_present_students()/(double)((Professor) professor).getNum_total_students());
                    return true;
                }
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean course_received) {
            // Pass object to next class
            if(course_received) {
                Intent i = new Intent(LoadDataScreen.this, ProfessorActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable();
                i.putExtra("ProfessorData", professor);
                startActivity(i);
                finish();
            }else{
                Objects.requireNonNull(LoadDataScreen.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( getApplicationContext(), "NO COURSES FOR YOU NOW!" , Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    /**
     *
     * @return returns an arraylist of ImageItem type, containing an image bitmap related to the
     * student and small info on his name/id on a string
     */
    private ArrayList<ImageItem> getStudentData(final String user_id, final String user_password){
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

                JSONArray receiveData = retrieve_professors_course_data.requestAndFetch(LoadDataScreen.this);
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
                            Objects.requireNonNull(LoadDataScreen.this).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error Parsing Student Data", Toast.LENGTH_LONG).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }else{
                    Objects.requireNonNull(LoadDataScreen.this).runOnUiThread(new Runnable() {
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
        if(imageItems.size() != 0) {
            return imageItems;
        }else{
            return null;
        }

    }

    // get number of prsent students in professors current class
    private int getNumberOfPresentStudents( ArrayList<ImageItem> listOfStudents){
        int num_present = 0;
        for(int i = 0; i < listOfStudents.size(); i++){
            if(listOfStudents.get(0).isPresent()){
                num_present++;
            }
        }
        return num_present;
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


}
