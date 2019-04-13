package bilkentcs492.aats;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Author      : Mr.Ndricim Rrapi
 * Project     : android
 * Date        :  04/13/2019
 * Time        : 13 | 05: 52 of 04 2019
 * Package name: bilkentcs492.aats
 * ALWAYS AIMING HIGH :D
 */
public class ServerRequest {
    private static final int CONNECTION_TIMEOUT=10000;
    private static final int READ_TIMEOUT=15000;
    private  String URL;
    List<QueryParameter> queryParamList;
    Uri.Builder paramURI; // Uri with all appended parameters from queryParamList

    public ServerRequest(  ){

    }

    public void setURL(String URL){
        this.URL = URL;
    }

    public void setParams(List<QueryParameter> queryParamList){
        paramURI = appendParam(queryParamList);
    }

    public JSONArray requestAndFetch(){

        HttpURLConnection conn;
        URL url = null;

        // ACCESS URL-API
        try {
            // Enter URL address where your php file resides
            url = new URL(URL);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            Log.e("MalformedURLException: ", "Error forming URL");
            e.printStackTrace();
            return null;
        }

        // SEND POST REQUEST WITH GIVEN PARAMETERS TO SERVER
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Append parameters to URL

            String query = paramURI.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            Log.e("IOException: ", "Error sending POST request");
            e1.printStackTrace();
            return null;
        }


        // RECEIVE RESPONSE FROM SERVER , NULL / JSON
        try {
            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                String outputData = null;
                outputData = result.toString();

                //SERVER RETURNED NULL
                if (outputData.equals("null")){
                    Log.e("Server returned: ",outputData); // null
                    conn.disconnect();
                    return null;
                }

                // SERVER RETURNED VALID DATA IN JSON FORMAT
                else {
                    try {
                        conn.disconnect();
                        return new JSONArray(outputData);
                    } catch (JSONException e) {
                        Log.e("log_tag", "Error parsing jsonarray " + e.toString());
                        conn.disconnect();
                        return null;
                    }
                }

            }
            return null;
        } catch (IOException e) {
            Log.e("IOException: ", "Error receiving RESPONSE from server");
            e.printStackTrace();
            return null;
        }
    }

    // easy way to add multiple paramaters from a list
    private Uri.Builder appendParam(List<QueryParameter> inputParams){
        Uri.Builder builder = new Uri.Builder();
        for (int i = 0; i < inputParams.size(); i++){
            builder.appendQueryParameter(inputParams.get(i).getKey(), inputParams.get(i).getValue());
        }
        return builder;
    }
}


// code for printing json data for testing
//                        for (int i = 0; i < jArray.length(); i++) {
//                            JSONObject json_data = jArray.getJSONObject(i);
//                            Log.i("log_tag", "id: " + json_data.getInt("ID") +
//                                    ", name: " + json_data.getString("name") +
//                                    ", surname: " + json_data.getString("surname") +
//                                    ", email: " + json_data.getString("email") +
//                                    ", present: " + json_data.getString("present")
//                            );
//                        }