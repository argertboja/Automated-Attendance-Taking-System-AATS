package bilkentcs492.aats;

import java.io.Serializable;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  04/14/2019
 * Time        : 14 | 05: 28 of 04 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
public class User implements Serializable {

    private String user_ID;
    private String user_password;
    private String user_name;
    private String user_surname;
    private String user_email;

    User(){

    }

    User(String user_ID,String user_password,String user_name,String user_surname,String user_email){
        this.user_ID        = user_ID;
        this.user_password  = user_password;
        this.user_name      = user_name;
        this.user_surname   = user_surname;
        this.user_email     = user_email;
    }

//    protected User(Parcel in) {
//        user_ID = in.readString();
//        user_password = in.readString();
//        user_name = in.readString();
//        user_surname = in.readString();
//        user_email = in.readString();
//    }


    public String getUser_ID() {
        return user_ID;
    }

    public String getUser_password() {
        return user_password;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_surname() {
        return user_surname;
    }

    public String getUser_email() {
        return user_email;
    }

//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(user_ID);
//        dest.writeString(user_password);
//        dest.writeString(user_name);
//        dest.writeString(user_surname);
//        dest.writeString(user_email);
//    }

}
