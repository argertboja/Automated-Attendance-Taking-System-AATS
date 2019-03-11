package bilkentcs492.aats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
        return inflater.inflate(R.layout.fragment_student_course_view, container, false);
    }

}
