package bilkentcs492.aats;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Information extends AppCompatActivity {

    final String URL_DEV_1 = "https://github.com/ndricimrr";
    final String URL_DEV_2 = "https://github.com/argertboja";
    final String URL_PROJECT = "http://cs491-492-projects.bilkent.edu.tr/AATS";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);



        Button openDev1 = (Button) findViewById(R.id.dev1) ;
        Button openDev2 = (Button) findViewById(R.id.dev2) ;
        Button openProject = (Button) findViewById(R.id.project_link) ;

        openDev1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse(URL_DEV_1); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        openDev2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse(URL_DEV_2); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        openProject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri = Uri.parse(URL_PROJECT); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });




    }


}
