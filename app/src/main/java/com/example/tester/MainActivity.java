package com.example.tester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    String resultcode;
    ArrayList<String> listofcash;
    String fullCode;
    int totalcash;
    String[] watfirst;
    int appender;


    public class CodeDownloader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                resultcode = "";
                int data = inputStream.read();
                while (data != -1){
                    char resy = (char) data;
                    resultcode +=resy;
                    data = inputStream.read();
                }
                return resultcode;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;

        }
    }
    public void clickFunction(View view){

        Toast.makeText(MainActivity.this, "The total crowns required for all dlcs is "+ Integer.toString(totalcash), Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listofcash = new ArrayList<String>();
        CodeDownloader codeDownloader = new CodeDownloader();
        try {
            fullCode = codeDownloader.execute("https://www.elderscrollsonline.com/en-us/crownstore/category/1").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Pattern p = Pattern.compile("<p><span class=\"bright crown-details\">(.*?)</span>");
        Matcher m= p.matcher(fullCode);
        while(m.find()){
            listofcash.add(m.group(1));
        }
        for(int i =0; i<=(listofcash.size()-1); i++) {
            watfirst = listofcash.get(i).split(",");
            appender = Integer.parseInt(watfirst[0]) *1000 +Integer.parseInt(watfirst[1]);
            totalcash += appender;
            watfirst[0]= null;
            watfirst[1] = null;
            appender = 0;
        }


        Toast.makeText(MainActivity.this, "The total crowns required for all dlcs is "+ Integer.toString(totalcash), Toast.LENGTH_LONG).show();


    }
}
