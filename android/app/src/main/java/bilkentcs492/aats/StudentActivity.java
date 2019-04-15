package bilkentcs492.aats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StudentActivity extends AppCompatActivity {
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        student  = (Student) getIntent().getExtras().getSerializable("StudentData");
        if (student == null) {
            Log.e("NULL","nullllll");
        }
        TextView accountOwner = findViewById(R.id.accountOwner);
        TextView studentCourse = findViewById(R.id.student_current_course);
        ImageView present = findViewById(R.id.present);
        ImageView absent = findViewById(R.id.absent);

        accountOwner.setText("Welcome: " + student.getUser_name() +" "+ student.getUser_surname() + "!");
        studentCourse.setText("Course: " + student.getCurrentCourse());
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
}
