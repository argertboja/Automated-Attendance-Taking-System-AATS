package bilkentcs492.aats;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * This class will show the current course happening now and the students current isPresent status
 * A simple {@link Fragment} subclass.
 */
public class StudentCourseViewFragment extends Fragment {
    private String id;
    private String Password;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    private String user_name ;
    private String user_surname;
    private String user_presence;
    private String user_password;
    private String user_course;
    private String URL = "http://accentjanitorial.com/accentjanitorial.com/aats_admin/public_html/retreive_student_info.php";

    public StudentCourseViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_student_course_view, container, false);

        ImageView firstStatus = (ImageView) rootView.findViewById(R.id.firstStatus);
        ImageView secondStatus = (ImageView) rootView.findViewById(R.id.secondStatus);

        firstStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(rootView.getContext())
                    .setTitle("Info")
                    .setMessage("You were marked as present for the first hour")

                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_info
                    )
                    .show();
            }
        });

        secondStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(rootView.getContext())
                        .setTitle("Info")
                        .setMessage("This hour has not started yet, check status bar below.")

                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(android.R.drawable.ic_dialog_info
                        )
                        .show();
            }
        });
        return  rootView;
    }

    private ArrayList<String> getStudentInfo() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerRequest receiveStudentInfo = new ServerRequest();
                receiveStudentInfo.setURL(URL);

                List<QueryParameter> params = new ArrayList<>();
                params.add(new QueryParameter("ID", id));
            }
        });
        return new ArrayList<String>();
    }

}
