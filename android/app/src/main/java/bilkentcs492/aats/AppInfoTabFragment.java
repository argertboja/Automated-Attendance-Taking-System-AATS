package bilkentcs492.aats;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AppInfoTabFragment extends Fragment {

    final String URL_DEV_1 = "https://github.com/ndricimrr";
    final String URL_DEV_2 = "https://github.com/argertboja";
    final String URL_PROJECT = "http://cs491-492-projects.bilkent.edu.tr/AATS";

    public AppInfoTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_app_info_tab, container, false);
        Button openDev1 = (Button) v.findViewById(R.id.dev1) ;
        Button openDev2 = (Button) v.findViewById(R.id.dev2) ;
        Button openProject = (Button) v.findViewById(R.id.project_link) ;

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





        return v;
    }

}
