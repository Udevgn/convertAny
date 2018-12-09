package com.example.utkarsh.convertany;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;
// import Apache HTTP Client v 4.3
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;


import java.io.IOException;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {
    public Button docs;
    public Button audio;
    public Button video;
    String apiKey = "9664d2338f92bd0efe6b5df96854fa5bc447baad";
    String endpoint = "https://sandbox.zamzar.com/v1/formats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  new DownloadTask().execute(apiKey,endpoint);
        docs = (Button)findViewById(R.id.docs);
        audio = (Button)findViewById(R.id.audio);
        video = (Button)findViewById(R.id.video);

        docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, Docs_Activity.class);
                    MainActivity.this.startActivity(i);

            }
        });
    }




























    /* private class DownloadTask extends AsyncTask<String,Void,String> {
        TextView textView = (TextView)findViewById(R.id.list);
        @Override
        protected String doInBackground(String... strings) {
            try {
                return showList(strings[0],strings[1]);
            } catch (JSONException e) {
                e.printStackTrace();
                return "empty";
            }
        }
        @Override
        protected void onPostExecute(String s) {
            //textView.setText(s);
        }
    }
    public String showList(String apiKey,String endpoint) throws JSONException{
        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpGet request = new HttpGet(endpoint);
        // Make request
        JSONObject json = null;
        try {
            CloseableHttpResponse response = httpClient.execute(request);
            // Extract body from response
            HttpEntity responseContent = response.getEntity();
            String result = EntityUtils.toString(responseContent, "UTF-8");
            // Parse result as JSON
            json = new JSONObject(result);
            // Finalise response and client
            response.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this,"JSON Null", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this,"JSON Null", Toast.LENGTH_LONG).show();

        }

        String string = json.toString(2);
        return string;
    }
    */
    private static CloseableHttpClient getHttpClient(String apiKey) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(apiKey, ""));

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

        return httpClient;
    }
}
