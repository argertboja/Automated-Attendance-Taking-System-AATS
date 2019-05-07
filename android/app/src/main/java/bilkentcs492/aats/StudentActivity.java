package bilkentcs492.aats;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static bilkentcs492.aats.MainActivity.getRoundedCornerBitmap;

public class StudentActivity extends AppCompatActivity {
    Student student;
    final String GET_STUDENT_IMAGE_URL = "https://bilmenu.com/aats/php/get_student_image.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        student  = (Student) getIntent().getExtras().getSerializable("StudentData");

        TextView accountOwner = findViewById(R.id.accountOwner);
        TextView studentCourse = findViewById(R.id.student_current_course);
        TextView student_current_hour = findViewById(R.id.current_hour_student);
        TextView student_id_view = findViewById(R.id.student_ID);
        ImageView present = findViewById(R.id.present);
        ImageView absent = findViewById(R.id.absent);

        ImageView studentImage = findViewById(R.id.studentImage);
        Bitmap btmp = getStudentImage(student.getStudentImage());
        if(btmp != null){
            studentImage.setImageBitmap(btmp);
        }

        accountOwner.setText( student.getUser_name() +" "+ student.getUser_surname() );
        studentCourse.setText( student.getCurrentCourse());
        student_current_hour.setText( student.getCurrent_hour());
        student_id_view.setText(student.getUser_ID());
        if (student.isPresent().equals("1")) {
            present.setVisibility(View.VISIBLE);
            absent.setVisibility(View.GONE);
        } else {
            present.setVisibility(View.GONE);
            absent.setVisibility(View.VISIBLE);
        }
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
            Intent logout = new Intent(StudentActivity.this, LoginActivity.class);
            Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_LONG).show();
            startActivity(logout);

        }
        return super.onOptionsItemSelected(item);
    }

    public Bitmap getStudentImage(String base64){
        if(base64 != null) {
            final byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            decodedBitmap = Bitmap.createScaledBitmap(decodedBitmap, 500, 500, false);
            decodedBitmap = getRoundedCornerBitmap(decodedBitmap, 12);
            return decodedBitmap;
        }
        return null;
    }
}
