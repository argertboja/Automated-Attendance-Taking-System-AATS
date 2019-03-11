package bilkentcs492.aats;

import java.util.ArrayList;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  03/11/2019
 * Time        : 11 | 10: 59 of 03 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
public class Course {

    private String coursename;
    private String courseCode;
    private ArrayList<Student> listOfStudents;

    public Course(){}

    public Course(String coursename, String courseCode, ArrayList<Student> listOfStudents) {
        this.coursename = coursename;
        this.courseCode = courseCode;
        this.listOfStudents = listOfStudents;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public ArrayList<Student> getListOfStudents() {
        return listOfStudents;
    }

    public void setListOfStudents(ArrayList<Student> listOfStudents) {
        this.listOfStudents = listOfStudents;
    }
}
