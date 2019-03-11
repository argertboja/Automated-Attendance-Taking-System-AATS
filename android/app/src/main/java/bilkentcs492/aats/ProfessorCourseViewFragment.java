package bilkentcs492.aats;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * This fragment shows the current course taking place and the attendance results as fetched from
 * the online recognition data
 * A simple {@link Fragment} subclass.
 */
public class ProfessorCourseViewFragment extends Fragment {


    public ProfessorCourseViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_professor_course_view, container, false);



        return v;
    }

}
