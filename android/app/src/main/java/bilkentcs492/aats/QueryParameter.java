package bilkentcs492.aats;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  04/13/2019
 * Time        : 13 | 05: 57 of 04 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
public class QueryParameter {
    private String key;
    private String value;

    public QueryParameter(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
