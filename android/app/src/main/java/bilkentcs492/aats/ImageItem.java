package bilkentcs492.aats;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  03/11/2019
 * Time        : 11 | 21: 05 of 03 2019
 * Package name: bilkentcs492.aats
 * Description : This class holds the current class data, for the professor's view,
 *               An ImageItem contains a Bitmap of the students face and their ID number
 *               Saving any other data for temporary reasons would be redundant
 * ALWAYS AIMING HIGH :D
 */

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String studentID;



    private boolean isPresent;


    ImageItem(Bitmap image, String title, boolean isPresent) {
        super();
        this.image = image;
        this.studentID = title;
        this.isPresent = isPresent;
    }

    Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
