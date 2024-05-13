package com.bextdev.jasper;

import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.util.AsynchUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Jasper extends AndroidNonvisibleComponent {

  public Jasper(ComponentContainer container) {
    super(container.$form());
  }

  @SimpleFunction
  public void ChatJasper(String prompt, String apiKey){
    AsynchUtil.runAsynchronously(new Runnable() {
      @Override
      public void run() {
        try {
          URL url = new URL("https://api.jasper.ai/v1/templates");
          HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
          httpConnection.setRequestMethod("POST");
          httpConnection.setRequestProperty("Content-Type", "application/json");
          httpConnection.setRequestProperty("X-API-Key", apiKey);
          httpConnection.setDoOutput(true);
          OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream());
          writer.write(prompt.replace("\"", "'")); 
          writer.flush();
          writer.close();
       
          int resCode = httpConnection.getResponseCode();

          if(resCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
              response.append(inputLine);
            }
            in.close();
            form.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                try {
                  String json = response.toString();
                  JSONObject obj = new JSONObject(json);
                  String output = obj.getJSONArray("choices").getJSONObject(0).getString("text");
                  JasperResponse(output);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            });
          }
        } catch(Exception e) { 
          e.printStackTrace();
        }
      }
    });
  }
   
  @SimpleEvent
  public void JasperResponse(String response) {
    EventDispatcher.dispatchEvent(this, "JasperResponse", response);
  }
}