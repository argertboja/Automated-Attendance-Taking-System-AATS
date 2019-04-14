package bilkentcs492.aats;

import java.io.Serializable;

class Student extends User implements Serializable {

    private String present;
    private String currentCourse;


    public Student(String user_ID,String user_password,String user_name,String user_surname,String user_email ){
        super(user_ID,user_password,user_name,user_surname,user_email);
        this.present = present;
        this.currentCourse = currentCourse;
    }


    public String isPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(String currentCourse) {
        this.currentCourse = currentCourse;
    }
}
