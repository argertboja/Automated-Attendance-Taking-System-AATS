package bilkentcs492.aats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    private final String NO_COURSES_NOW = "NO_DATA";

    //profi
    private static final int REQUEST_IMAGE_CAPTURE  = 1;
    private static final int BITMAP_SMALL_WIDTH     = 1200;
    private static final int BITMAP_SMALL_HEIGHT    = 1600;
//    private final String UPLOAD_URL                 = "https://bilmenu.com/aats/php/upload_image.php";
    private final String GET_STUDENT_LIST_URL       = "https://bilmenu.com/aats/php/retrieve_professors_course.php";
//    private String objection_picture_path;
    Professor professor;
    AlertDialog.Builder dialog_builder;
    // Student
    Student student;
//    private final String GET_STUDENT_INFO           = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/retrieve_student_info.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data_screen);

        dialog_builder = new AlertDialog.Builder(LoadDataScreen.this);

        dialog_builder.setCancelable(false);

        userType        = (getIntent().getExtras().getString("user_presence"));
        if(userType.equals(TYPE_PROFESSOR)) {
            ProfessorTask task =  new ProfessorTask();
            dialog_builder.setTitle("Dear "+ (getIntent().getExtras().getString("user_name"))  +" Hocam");
            dialog_builder.setMessage("NO COURSES FOR YOU AT THIS MOMENT");

            dialog_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    Intent intent = new Intent(LoadDataScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            task.execute("");
        }else if(userType.equals(TYPE_STUDENT_ABSENT) || userType.equals(TYPE_STUDENT_PRESENT) ){
            StudentTask studentTask = new StudentTask();
            dialog_builder.setTitle("Dear "+ (getIntent().getExtras().getString("user_name"))  +" ");
            dialog_builder.setMessage("NO COURSES FOR YOU AT THIS MOMENT");

            dialog_builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    Intent intent = new Intent(LoadDataScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            studentTask.execute("");
        }

    }

    private class StudentTask extends  AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground (String... params) {
            String user_id         = (getIntent().getExtras().getString("user_id"));
            String user_password   = (getIntent().getExtras().getString("user_password"));
            String user_email      = (getIntent().getExtras().getString("user_email"));
            String user_name       = (getIntent().getExtras().getString("user_name"));
            String user_surname    = (getIntent().getExtras().getString("user_surname"));
            String user_presence   = (getIntent().getExtras().getString("user_presence"));
            String  current_course = (getIntent().getExtras().getString("professor_current_course"));
            String current_hour    = (getIntent().getExtras().getString("current_hour"));
            String user_image      = (getIntent().getExtras().getString("studentImage"));

            if( user_id != null && user_password != null && user_email != null && user_name != null && user_surname != null && user_presence != null && current_course != null && !current_course.equals(NO_COURSES_NOW)) {
                student = new Student(user_id, user_password, user_name, user_surname, user_email);
                student.setCurrentCourse(current_course);
                student.setPresent(user_presence);
                student.setCurrent_hour(current_hour);
                student.setStudentImage(user_image);
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean course_received) {
            // Pass object to next class
            if(course_received) {
                Intent intent = new Intent(LoadDataScreen.this, StudentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("StudentData", student);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }else{

                dialog_builder.show();
                Objects.requireNonNull(LoadDataScreen.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText( getApplicationContext(), "NO COURSES FOR YOU NOW!" , Toast.LENGTH_LONG).show();
                    }
                });
            }
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
            String current_hour    = (getIntent().getExtras().getString("current_hour"));
            String  current_course = (getIntent().getExtras().getString("professor_current_course"));
            if(user_id != null && user_password != null && user_email !=null && user_name != null && user_surname != null && current_course != null && !(current_course.equals(NO_COURSES_NOW)) ) {
                ArrayList<ImageItem> listOfStudents = getStudentData(user_id, user_password);
                professor = new Professor(user_id, user_password, user_name, user_surname, user_email,listOfStudents);
                (professor).setCurrentCourse(current_course);
                if(listOfStudents.size() != 0) {
//                    ((Professor) professor).setListOfCurrentStudents(listOfStudents);
                    ( professor).setNum_present_students(getNumberOfPresentStudents(listOfStudents));
                    ( professor).setNum_total_students(listOfStudents.size());
                    ( professor).setNum_absent_students(((Professor) professor).getNum_total_students() - ((Professor) professor).getNum_present_students());
                    ( professor).setAttendance_percentage(); // set within the class
                    ( professor).setCurrent_hour(current_hour); // set within the class

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
                Intent intent = new Intent(LoadDataScreen.this, ProfessorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ProfessorData", professor);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }else{

                dialog_builder.show();
                Log.e("ERROR:","POST EXECUTE");
//                Objects.requireNonNull(LoadDataScreen.this).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
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

                            String presenc = json_data.optString("present");
                            if(presenc == null || presenc.equals("")  || base64.equals("") || studentID.equals("") ){
                                break;
                            }
                            boolean present = (Integer.parseInt(presenc) == 1) ;
                            imageItems.add(new ImageItem(base64, studentID, present));
                        } catch (JSONException e) {
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

        while(t.isAlive()){
            // do nothing
        }
        if(imageItems.size() != 0) {
            return imageItems;
        }else{
            return new ArrayList<>();
        }

    }

    // get number of prsent students in professors current class
    private int getNumberOfPresentStudents( ArrayList<ImageItem> listOfStudents){
        int num_present = 0;
        for(int i = 0; i < listOfStudents.size(); i++){
            if(listOfStudents.get(i).isPresent()){
                num_present++;
            }
        }
        return num_present;
    }


}
