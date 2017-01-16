package lmu.de.unificiencyandroid.network;

public class NodeAPIClient extends UnificiencyClient {
  private final String BASE_URL = "http://li.mz-host.de:5048/";
  public NodeAPIClient(){
    super();
  }

  @Override
  protected String getAbsoluteUrl(String relativeUrl) {
    return BASE_URL + relativeUrl;
  }
}