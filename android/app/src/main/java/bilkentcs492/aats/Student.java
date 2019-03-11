package bilkentcs492.aats;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  03/11/2019
 * Time        : 11 | 11: 01 of 03 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
class Student {

    private int studentID; //21500342
    private String studentFaceID;
    private boolean isPresent;

    public Student(){
    }

    public Student(int studentID, String studentFaceID, boolean isPresent){
        this.isPresent = isPresent;
        this.studentID = studentID;
        this.studentFaceID = studentFaceID;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
