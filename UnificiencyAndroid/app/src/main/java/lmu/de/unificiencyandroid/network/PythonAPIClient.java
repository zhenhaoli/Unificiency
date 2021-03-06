package lmu.de.unificiencyandroid.network;

public class PythonAPIClient extends UnificiencyClient {
  private final String BASE_URL = "http://romue404.pythonanywhere.com/api/";
  public PythonAPIClient(){
    super();
  }

  @Override
  protected String getAbsoluteUrl(String relativeUrl) {
    return BASE_URL + relativeUrl;
  }
}
