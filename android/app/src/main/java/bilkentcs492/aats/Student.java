package bilkentcs492.aats;

import java.io.Serializable;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  03/11/2019
 * Time        : 11 | 11: 01 of 03 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
class Student extends User implements Serializable {

    private boolean present;
    private String currentCourse;


    public Student(String user_ID,String user_password,String user_name,String user_surname,String user_email ){
        super(user_ID,user_password,user_name,user_surname,user_email);
    }


    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getCurrentCourse() {
        return currentCourse;
    }

    public void setCurrentCourse(String currentCourse) {
        this.currentCourse = currentCourse;
    }
}
