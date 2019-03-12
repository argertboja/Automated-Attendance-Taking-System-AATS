package bilkentcs492.aats;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * This class will show the current course happening now and the students current isPresent status
 * A simple {@link Fragment} subclass.
 */
public class StudentCourseViewFragment extends Fragment {


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

}
