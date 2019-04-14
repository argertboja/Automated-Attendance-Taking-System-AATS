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

import java.io.Serializable;

public class ImageItem implements Serializable {
//    private Bitmap image;
    private String image_base64;
    private String studentID;
    private boolean isPresent;


    ImageItem(String image_base64, String title, boolean isPresent) {
        super();
        this.image_base64 = image_base64;

        this.studentID = title;
        this.isPresent = isPresent;
    }

//    private ImageItem(Parcel in) {
//        image_base64 = in.readString();
//        studentID = in.readString();
//        isPresent = in.readByte() != 0;
//    }



    String getImage() {
        return image_base64;
    }

    public void setImage(String image_base64) {
        this.image_base64 = image_base64;
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

//    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
//        @Override
//        public ImageItem createFromParcel(Parcel in) {
//            return new ImageItem(in);
//        }
//
//        @Override
//        public ImageItem[] newArray(int size) {
//            return new ImageItem[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(image_base64);
//        dest.writeString(studentID);
//        dest.writeByte((byte) (isPresent ? 1 : 0));
//    }
}
