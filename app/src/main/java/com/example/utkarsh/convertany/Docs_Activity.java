package com.example.utkarsh.convertany;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

public class Docs_Activity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    String apiKey = "9664d2338f92bd0efe6b5df96854fa5bc447baad";
    String endpoint = "https://sandbox.zamzar.com/v1/formats";
    static String filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs_);
        new DownloadTask().execute(apiKey,endpoint);
        showFileChooser();
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

                    String FilePath = data.getData().getPath();
                    String FileName = data.getData().getLastPathSegment();
                    int lastPos = FilePath.length() - FileName.length();
                    String Folder = FilePath.substring(0, lastPos);

                    TextView filepath = (TextView)findViewById(R.id.filepath);
                    filepath.setText(Folder+"FILEPATH"+FilePath+"FILENAME"+FileName);
                }
                break;

        }
    }






    private class DownloadTask extends AsyncTask<String,Void,String> {
        TextView textView = (TextView)findViewById(R.id.filepath);
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
            textView.setText(s);
        }
    }
    public String showList(String apiKey,String endpoint) throws JSONException {
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
