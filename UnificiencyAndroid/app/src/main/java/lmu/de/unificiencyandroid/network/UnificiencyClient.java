package lmu.de.unificiencyandroid.network;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public abstract class UnificiencyClient {
  protected String BASE_URL;
  //private final String BASE_URL = "http://192.168.178.24:5048/";

  protected AsyncHttpClient client;

  public UnificiencyClient(){
    client = new AsyncHttpClient();
  }

  public void addHeader(String key, String value) {
    client.addHeader(key, value);
  }

  public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    Log.d("url", getAbsoluteUrl(url));
    client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    Log.d("url", getAbsoluteUrl(url));
    client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  protected String getAbsoluteUrl(String relativeUrl) {
    return BASE_URL + relativeUrl;
  }
}

