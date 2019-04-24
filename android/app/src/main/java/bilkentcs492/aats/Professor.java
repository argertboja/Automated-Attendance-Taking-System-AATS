package bilkentcs492.aats;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  04/14/2019
 * Time        : 14 | 05: 43 of 04 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
public class Professor  extends User implements Serializable {

    private String currentCourse;
//    private ArrayList listOfCurrentStudents;
    private int num_present_students;
    private int num_absent_students;
    private int num_total_students;
    private int attendance_percentage;
    private String current_hour;

    private ArrayList<ImageItem> listOfCurrentStudents;

    Professor(String user_ID,String user_password,String user_name,String user_surname,String user_email, ArrayList<ImageItem> listOfCurrentStudents  ){
        super(user_ID,user_password,user_name,user_surname,user_email);
        this.listOfCurrentStudents = listOfCurrentStudents;
    }

    Professor(String user_ID,String user_password,String user_name,String user_surname,String user_email  ){
        super(user_ID,user_password,user_name,user_surname,user_email);
    }

    public String getCurrent_hour() {
        return current_hour;
    }

    public void setCurrent_hour(String current_hour) {
        this.current_hour = current_hour;
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

    int getAttendance_percentage() {
        return attendance_percentage;
    }

    void setAttendance_percentage() {
        this.attendance_percentage =  (int) ((double)this.num_present_students / (double)this.num_total_students *100) ;
    }

    ArrayList<ImageItem> getListOfCurrentStudents() {
        return listOfCurrentStudents;
    }

    public void setListOfCurrentStudents(ArrayList<ImageItem> listOfCurrentStudents) {
        this.listOfCurrentStudents = listOfCurrentStudents;
    }

}
