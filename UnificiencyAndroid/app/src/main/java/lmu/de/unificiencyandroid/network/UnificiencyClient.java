package lmu.de.unificiencyandroid.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

public abstract class UnificiencyClient {
  protected String BASE_URL;

  protected AsyncHttpClient client;

  public UnificiencyClient(){
    client = new AsyncHttpClient();
  }

  public void addHeader(String key, String value) {
    client.addHeader(key, value);
  }

  public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    Logger.i("GET " + getAbsoluteUrl(url) + ((params != null) ? ("?" +  params.toString()) : ' '));
    client.get(getAbsoluteUrl(url), params, responseHandler);
  }

  public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    Logger.i("POST " + getAbsoluteUrl(url) + ((params != null) ? ("?" +  params.toString()) : ' '));
    client.post(getAbsoluteUrl(url), params, responseHandler);
  }

  public void put(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    Logger.i("PUT " + getAbsoluteUrl(url)  + ((params != null) ? ("?" +  params.toString()) : ' '));
    client.put(getAbsoluteUrl(url), params, responseHandler);
  }

  public void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    Logger.i("DELETE " + getAbsoluteUrl(url)  + ((params != null) ? ("?" +  params.toString()) : ' '));
    client.put(getAbsoluteUrl(url), params, responseHandler);
  }

  protected String getAbsoluteUrl(String relativeUrl) {
    return BASE_URL + relativeUrl;
  }
}

