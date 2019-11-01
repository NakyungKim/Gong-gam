package com.example.otpapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2Activity extends AppCompatActivity {

    private static final String TAG1 = "MainActivity";




    private static final String TAG = "HttpExampleActivity";

    TextView TxtResult;
    Button PostBtn, GetBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG1, "onCreate start in MainActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.e(TAG1, "onCreate end in MainActivity");

        Log.e(TAG, "onCreate started in HttpExampleActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_example);

        GetBtn = (Button) findViewById(R.id.get_btn);
        PostBtn = (Button) findViewById(R.id.post_btn);

        GetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // donggam
                new MakeNetworkCall().execute("http://52.79.242.88:80/http.php?get=1", "Get");
            }
        });

        PostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // donggam
                new MakeNetworkCall().execute("http://52.79.242.88/http.php?post=1", "Post");
            }
        });

        Log.e(TAG, "onCreate ended in HttpExampleActivity");
    }

    InputStream ByGetMethod(String ServerURL) {

        Log.e(TAG, "ByGetMethod started in HttpExampleActivity");

        InputStream DataInputStream = null;
        try {
            URL url = new URL(ServerURL);
            HttpURLConnection cc = (HttpURLConnection)url.openConnection();
            cc.setReadTimeout(5000);
            cc.setConnectTimeout(5000);
            cc.setRequestMethod("GET");
            cc.setDoInput(true);
            cc.connect();

            int response = cc.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
                //Log.e(TAG, "ByGetMethod, DataInputStream = " + DataInputStream);
            }
            else {
                Log.e(TAG, "ByGetMethod, response = " + response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in ByGetMethod", e);
        }

        Log.e(TAG, "ByGetMethod ended in HttpExampleActivity");

        return DataInputStream;
    }

    InputStream ByPostMethod(String ServerURL) {

        Log.e(TAG, "ByPostMethod started in HttpExampleActivity");

        InputStream DataInputStream = null;
        try {
            String PostParam = "first_name=dong&last_name=gam";

            URL url = new URL(ServerURL);

            HttpURLConnection cc = (HttpURLConnection)url.openConnection();
            if(cc == null) {
                Log.e(TAG, "ByPostMethod, cc is null");
            }
            cc.setReadTimeout(5000);
            cc.setConnectTimeout(5000);
            cc.setRequestMethod("POST");
            cc.setDoInput(true);
            cc.connect();

            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            dos.flush();
            dos.close();

            int response = cc.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }
            else {
                Log.e(TAG, "ByPostMethod, response = " + response);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error in PostData, error = " + e);
        }

        Log.e(TAG, "ByPostMethod ended in HttpExampleActivity");

        return DataInputStream;
    }

    String ConvertStreamToString(InputStream stream) {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error in ConvertStreamToString, error = " + e);
        } catch (Exception e) {
            Log.e(TAG, "Error in ConvertStreamToString", e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, "Error in ConvertStreamToString", e);
            } catch (Exception e) {
                Log.e(TAG, "Error in ConvertStreamToString", e);
            }
        }

        return response.toString();
    }

    public void DisplayMessage(String a) {

        TxtResult = (TextView) findViewById(R.id.response);
        TxtResult.setText(a);
    }

    private class MakeNetworkCall extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DisplayMessage("Please Wait ...");
        }

        @Override
        protected String doInBackground(String... arg) {
            InputStream is = null;
            String URL = arg[0];
            Log.e(TAG, "doInBackground started in HttpExampleActivity, URL: " + URL);
            String res = "";

            Log.e(TAG, "doInBackground, arg[1] = " + arg[1]);
            if (arg[1].equals("Post")) {
                is = ByPostMethod(URL);
            } else if (arg[1].equals("Get")) {
                is = ByGetMethod(URL);
            } else {
                Log.e(TAG, "doInBackground, do nothing, arg[1] = " + arg[1]);
            }

            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DisplayMessage(result);
            Log.e(TAG, "onPostExecute, result: " + result);
        }
    }
}
