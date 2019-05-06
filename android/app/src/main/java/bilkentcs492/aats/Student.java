package bilkentcs492.aats;

import java.io.Serializable;

class Student extends User implements Serializable {

    private String present;
    private String currentCourse;
    private String studentImage;
    private String current_hour;

    public Student(String user_ID,String user_password,String user_name,String user_surname,String user_email ){
        super(user_ID,user_password,user_name,user_surname,user_email);
        this.present = present;
        this.currentCourse = currentCourse;
    }
    public String getCurrent_hour() {
        return current_hour;
    }

    public void setCurrent_hour(String current_hour) {
        this.current_hour = current_hour;
    }

    String isPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    String getCurrentCourse() {
        return currentCourse;
    }

    void setCurrentCourse(String currentCourse) {
        this.currentCourse = currentCourse;
    }

    public String getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
    }

}
