package bilkentcs492.aats;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  04/14/2019
 * Time        : 14 | 05: 43 of 04 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
public class Professor  extends User implements Parcelable {

    private String currentCourse;
    private ArrayList listOfCurrentStudents;
    private int num_present_students;
    private int num_absent_students;
    private int num_total_students;
    private double attendance_percentage;

    Professor(String user_ID,String user_password,String user_name,String user_surname,String user_email  ){
        super(user_ID,user_password,user_name,user_surname,user_email);
    }

    Professor(Parcel in){
        this.currentCourse = in.readString();
        this.num_present_students = in.readInt();
        this.num_absent_students = in.readInt();
        this.num_total_students = in.readInt();
        this.attendance_percentage = in.readDouble();
        this.listOfCurrentStudents = in.readArrayList(ImageItem.class.getClassLoader());
    }

    ArrayList<ImageItem> getListOfCurrentStudents() {
        return listOfCurrentStudents;
    }

    void setListOfCurrentStudents(ArrayList<ImageItem> listOfCurrentStudents) {
        this.listOfCurrentStudents = listOfCurrentStudents;
    }

    String getCurrentCourse() {
        return currentCourse;
    }

    void setCurrentCourse(String currentCourse) {
        this.currentCourse = currentCourse;
    }

    int getNum_present_students() {
        return num_present_students;
    }

    void setNum_present_students(int num_present_students) {
        this.num_present_students = num_present_students;
    }

    int getNum_total_students() {
        return num_total_students;
    }

    void setNum_total_students(int num_total_students) {
        this.num_total_students = num_total_students;
    }

    Integer getNum_absent_students() {
        return num_absent_students;
    }

    void setNum_absent_students(int num_absent_students) {
        this.num_absent_students = num_absent_students;
    }

    double getAttendance_percentage() {
        return attendance_percentage;
    }

    void setAttendance_percentage(double attendance_percentage) {
        this.attendance_percentage = attendance_percentage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Professor createFromParcel(Parcel in) {
            return new Professor(in);
        }

        public Professor[] newArray(int size) {
            return new Professor[size];
        }
    };
}
