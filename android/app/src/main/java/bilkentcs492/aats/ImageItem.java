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
import android.os.Parcel;
import android.os.Parcelable;

public class ImageItem implements Parcelable {
    private Bitmap image;
    private String studentID;
    private boolean isPresent;


    ImageItem(Bitmap image, String title, boolean isPresent) {
        super();
        this.image = image;
        this.studentID = title;
        this.isPresent = isPresent;
    }

    protected ImageItem(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        studentID = in.readString();
        isPresent = in.readByte() != 0;
    }

    public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        @Override
        public ImageItem createFromParcel(Parcel in) {
            return new ImageItem(in);
        }

        @Override
        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(image, flags);
        dest.writeString(studentID);
        dest.writeByte((byte) (isPresent ? 1 : 0));
    }
}
