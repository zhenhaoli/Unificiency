package lmu.de.unificiencyandroid.network;

public class NodeAPIClient extends UnificiencyClient {
  private final String BASE_URL = "http://li.mz-host.de:5048/";
  //private final String BASE_URL = "http://141.84.214.127:5048/";
  //private final String BASE_URL = "http://192.168.178.24:5048/";
  public NodeAPIClient(){
    super();
  }

  @Override
  protected String getAbsoluteUrl(String relativeUrl) {
    return BASE_URL + relativeUrl;
  }
}
