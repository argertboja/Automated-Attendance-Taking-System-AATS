package bilkentcs492.aats;

import android.app.Activity;
import android.net.Uri;
import android.widget.Toast;

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
    private Uri.Builder paramURI; // Uri with all appended parameters from queryParamList
    private String uploadResponse;

    ServerRequest(){

    }

    void setURL(String URL){
        this.URL = URL;
    }

    void setParams(List<QueryParameter> queryParamList){
        paramURI = appendParam(queryParamList);
    }

    // makes a request and fetches the json outpu
    JSONArray requestAndFetch(final Activity activity){

        HttpURLConnection conn;
        URL url = null;

        // ACCESS URL-API
        try {
            // Enter URL address where your php file resides
            url = new URL(URL);

        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                String response;
                @Override
                public void run() {
                    Toast.makeText(activity, "MalformedURLException:"+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
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

        } catch (final IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                String response;
                @Override
                public void run() {
                    Toast.makeText(activity, "IOException: Error sending POST request"+ e1.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
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
                conn.disconnect();
                return new JSONArray(outputData);

            }
            return null;
        } catch (final IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                String response;
                @Override
                public void run() {
                    Toast.makeText(activity, "IOException: Error receiving RESPONSE from server"+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return null;
        } catch (final JSONException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                String response;
                @Override
                public void run() {
                    Toast.makeText(activity, "IOException: Error parsing response to JSON"+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return null;
        }
    }

    // makes an upload request
    void uploadRequest(final Activity activity){

        HttpURLConnection conn = null;
        URL url = null;

        // ACCESS URL-API
        try {
            // Enter URL address where your php file resides
            url = new URL(URL);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            e1.printStackTrace();
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

                // Pass data to onPostExecute method
                uploadResponse= "";
                uploadResponse = result.toString();

                if (uploadResponse.equals("null")){
                    activity.runOnUiThread(new Runnable() {
                        String response;
                        @Override
                        public void run() {
                            Toast.makeText(activity, "ERROR UPLOADING:"+ uploadResponse, Toast.LENGTH_LONG).show();
                        }
                    });
                    conn.disconnect();
                }else{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, uploadResponse, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
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
