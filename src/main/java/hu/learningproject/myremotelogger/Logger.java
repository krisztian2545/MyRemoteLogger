package hu.learningproject.myremotelogger;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import hu.learningproject.myremotelogger.lib.ServerStatusRequest;
import hu.learningproject.myremotelogger.model.LogMessage;

public class Logger extends Application {
  
  private static Logger instance;
  private static RequestQueue requestQueue;
  private static String url = "http://localhost:5000";
  
  // this will be removed
  private static boolean showToastResponses = false;
  
  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    
  }
  
  public static void setUrl(String newUrl) {
    url = newUrl;
  }
  
  public static synchronized Logger getInstance() {
    return instance;
  }
  
  private RequestQueue getRequestQueue() {
    if (requestQueue == null) {
      // getApplicationContext() is key, it keeps you from leaking the
      // Activity or BroadcastReceiver if someone passes one in.
      requestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return requestQueue;
  }
  
  private <T> void addToRequestQueue(Request<T> req) {
    getRequestQueue().add(req);
  }
  
  public ServerStatusRequest createJsonPostRequest(String message) {
    LogMessage log = new LogMessage();
    
    log.setAppName(getApplicationInfo().loadLabel(getPackageManager()).toString()); // idk how, but gets the app name
    log.setMessage(message);
    log.setTime((new Date().toString()));
  
    JSONObject jsonObject = null;
    try {
      jsonObject = new JSONObject((new Gson()).toJson(log));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  
    ServerStatusRequest jsonObjectRequest = new ServerStatusRequest(url, jsonObject,
        new Response.Listener<Integer>() {
          @Override
          public void onResponse(Integer response) {
            ezToast("onResponse: " + response.toString());
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            ezToast("onErrorResponse: " + error.getMessage());
          }
        });
    
    return jsonObjectRequest;
  }
  
  public static void debug(String message) {
    getInstance().addToRequestQueue( getInstance().createJsonPostRequest("DEBUG: " + message) );
  }
  
  private static void ezToast(String message) {
    if (showToastResponses) {
      Toast.makeText(instance.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
  }
  
  public static void setShowToastResponses(boolean b) {
    showToastResponses = b;
  }
  
}
