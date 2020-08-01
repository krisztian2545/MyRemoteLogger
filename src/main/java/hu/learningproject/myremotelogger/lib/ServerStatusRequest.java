package hu.learningproject.myremotelogger.lib;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ServerStatusRequest extends Request {
  
  private final Response.Listener mListener;
  @Nullable private final String mRequestBody;
  
  private static final String PROTOCOL_CHARSET = "utf-8";
  
  /** Content type for request. */
  private static final String PROTOCOL_CONTENT_TYPE =
      String.format("application/json; charset=%s", PROTOCOL_CHARSET);
  
  public ServerStatusRequest(
      int method,
      String url,
      @Nullable JSONObject jsonRequest,
      Response.Listener listener,
      @Nullable Response.ErrorListener errorListener) {
    super(method, url, errorListener);
    mListener = listener;
    mRequestBody = (jsonRequest == null) ? null : jsonRequest.toString();
  }

  public ServerStatusRequest(
      String url,
      @Nullable JSONObject jsonRequest,
      Response.Listener listener,
      @Nullable Response.ErrorListener errorListener) {
    this(
        jsonRequest == null ? Method.GET : Method.POST,
        url,
        jsonRequest,
        listener,
        errorListener);
  }
  
  @Override
  protected Response parseNetworkResponse(NetworkResponse response) {
    return Response.success(response.statusCode, HttpHeaderParser.parseCacheHeaders(response));
  }
  
  @Override
  protected void deliverResponse(Object response) {
    try {
      mListener.onResponse((Integer)response);
    } catch (Exception e) {
      VolleyLog.wtf("Error while casting response to Integer");
    }
  }
  
  @Override
  public byte[] getBody() {
    try {
      return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
    } catch (UnsupportedEncodingException uee) {
      VolleyLog.wtf(
          "Unsupported Encoding while trying to get the bytes of %s using %s",
          mRequestBody, PROTOCOL_CHARSET);
      return null;
    }
  }
  
  @Override
  public String getBodyContentType() {
    return PROTOCOL_CONTENT_TYPE;
  }
  
  @Override
  public int compareTo(Object o) {
    return 0;
  }
}
