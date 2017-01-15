package lmu.de.unificiencyandroid.components.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lmu.de.unificiencyandroid.MainActivity;
import lmu.de.unificiencyandroid.R;

public class LoginActivity extends AuthActivity {

    @BindView(R.id.usernameWrapper)
    TextInputLayout usernameWrapper;

    @BindView(R.id.passwordWrapper)
    TextInputLayout passwordWrapper;

    @OnClick(R.id.register)
    void register() {
        Intent register_Intent= new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(register_Intent, 1);
    }

    @OnClick(R.id.login)
    void login() {
        hideKeyboard();

        String username = usernameWrapper.getEditText().getText().toString();
        String password = passwordWrapper.getEditText().getText().toString();

        usernameWrapper.setError(null);
        usernameWrapper.setErrorEnabled(false);
        passwordWrapper.setError(null);
        passwordWrapper.setErrorEnabled(false);

        if(!validateEmail(username)){
            usernameWrapper.setError(getString(R.string.validation_email));
        }
        if(!validatePassword(password)){
            passwordWrapper.setError(getString(R.string.validation_password));
        }

        boolean validInput = (validateEmail(username)&&validatePassword(password));

        if(validInput) {

            HashMap<String, String> postDataParams = new HashMap<>();

            postDataParams.put("email", username);
            postDataParams.put("password", password);

            new HttpRequestTask().execute(postDataParams);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //TODO: remove this in prd
        usernameWrapper.getEditText().setText("unificiency@lmu.de");
        passwordWrapper.getEditText().setText("wearebest");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                Bundle extras = data.getExtras();
                String registeredMsg;

                if (extras != null) {
                    registeredMsg = extras.getString("registerSuccess");
                    SuperActivityToast.create(this, new Style(), Style.TYPE_STANDARD)
                        .setText(registeredMsg)
                        .setDuration(Style.DURATION_LONG)
                        .setFrame(Style.FRAME_KITKAT)
                        .setColor(ResourcesCompat.getColor(getResources(), R.color.lmugreen, null))
                        .setAnimations(Style.ANIMATIONS_SCALE)
                        .show();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private class HttpRequestTask extends AsyncTask<HashMap<String, String>, Void,String> {
        protected String doInBackground(HashMap<String, String>...postDataParams) {
            try {

                String token = performPostCall("http://li.mz-host.de:5048/users/login", postDataParams[0]);

                return token;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;

        }


        protected void onPostExecute(String res) {
            String token = null;
            try {
                JSONObject jObject = new JSONObject(res);
                token = jObject.getString("id_token");
            } catch (Exception e) {
                Log.e("JSON EER", e.toString());
            }


            if(token==null){
                //TODO: handle when user didnt get token e.g when pass wrong
            }

            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            loginIntent.putExtra("authToken", token);
            startActivity(loginIntent);
            finish();
        }

    }

    public String performPostCall(String requestURL,
                                  HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
