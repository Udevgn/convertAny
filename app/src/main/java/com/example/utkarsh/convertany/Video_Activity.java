package com.example.utkarsh.convertany;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
// import Apache HTTP Client v 4.3
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.entity.mime.*;
import org.apache.http.entity.mime.content.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;

// import JSON
import org.json.*;

// import from JDK
import java.io.*;
import java.util.concurrent.ExecutionException;

public class Video_Activity extends AppCompatActivity {
    String apiKey = "9664d2338f92bd0efe6b5df96854fa5bc447baad";
    String endpoint = "https://sandbox.zamzar.com/v1/jobs";
    String sourceFile = "";
    String targetFormat = "png";
    String parentFolder="";
    String targetFilename = "";
    private static final int FILE_SELECT_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_);
        showFileChooser();
        /*
        try {
            String result = new InitializeJob().execute(apiKey,endpoint).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
        new InitializeJob().execute(apiKey,endpoint);
        new StartJob().execute(apiKey,endpoint);

    }


    private void showFileChooser(){
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_SELECT_CODE);
        } catch (ActivityNotFoundException exp) {
            Toast.makeText(getBaseContext(), "No File (Manager / Explorer)etc Found In Your Device", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case FILE_SELECT_CODE:
                if(resultCode==RESULT_OK){

                    sourceFile = data.getData().getPath();
                    String FileName = data.getData().getLastPathSegment();
                    int lastPos = sourceFile.length() - FileName.length();
                    parentFolder = sourceFile.substring(0, lastPos);
                    targetFilename  = parentFolder+"convertAny"+"."+targetFormat;

                  //  filepath.setText(Folder+"FILEPATH"+FilePath+"FILENAME"+FileName);
                }
                break;

        }
    }

    private class InitializeJob extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            return initialize_Job(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            endpoint += "/"+s;
        }
    }
    private class StartJob extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            return start_Job(apiKey,endpoint);
        }

        @Override
        protected void onPostExecute(String s) {
            endpoint = "https://sandbox.zamzar.com/v1/files/" + s+ "/content";
        }
    }
    private String initialize_Job(String apiKey,String endpoint){
        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpEntity requestContent = MultipartEntityBuilder.create()
                .addPart("source_file", new FileBody(new File(sourceFile)))
                .addPart("target_format", new StringBody(targetFormat, ContentType.TEXT_PLAIN))
                .build();
        HttpPost request = new HttpPost(endpoint);
        request.setEntity(requestContent);

        // Make request
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract body from response
        HttpEntity responseContent = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(responseContent, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse result as JSON
        JSONObject json = null;
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Print result
        System.out.println(json);

        // Finalise response and client
        try {
            response.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String res;
        try {
            res = json.getString("id");
        } catch (JSONException e) {
            res = "";
        }
        return res;
    }
    private String start_Job(String apiKey,String endpoint){
        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpGet request = new HttpGet(endpoint);

        // Make request
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract body from response
        HttpEntity responseContent = response.getEntity();
        String result = null;
        try {
            result = EntityUtils.toString(responseContent, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parse result as JSON
        JSONObject json = null;
        try {
            json = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Print result
        System.out.println(json);

        // Finalise response and client
        try {
            response.close();
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileid ="";
        try {
            JSONArray json_arr = json.getJSONArray("target_files");
            fileid = (String)json_arr.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fileid;
    }
    private String DownloadFile(){

        // Create HTTP client and request object
        CloseableHttpClient httpClient = getHttpClient(apiKey);
        HttpGet request = new HttpGet(endpoint);

        // Make request
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extract body from response
        HttpEntity responseContent = response.getEntity();

        // Save response content to file on local disk
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(responseContent.getContent());
            bos = new BufferedOutputStream(new FileOutputStream(targetFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int inByte;
        try {
            while((inByte = bis.read()) != -1) {
                bos.write(inByte);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Finalise response, client and streams
        try {
            response.close();
            httpClient.close();
            bos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
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
