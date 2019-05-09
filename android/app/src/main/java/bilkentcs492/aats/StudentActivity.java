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

        }else if (id == R.id.infoButton){
            Intent logout = new Intent(StudentActivity.this, Information.class);
            Toast.makeText(getApplicationContext(), "App Info", Toast.LENGTH_LONG).show();
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
