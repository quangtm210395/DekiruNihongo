package hmdq.js.codeproject.dekirunihongo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class này có nhiệm vụ kiểm tra kết nối Internet
 * Cách hoạt động chủ yếu là PING đế Google và xem kết quả
 */
public class netChecker extends AsyncTask<netChecker.OnCheckingDone, Void, String> {
    Context context;

    OnCheckingDone callback;

    interface OnCheckingDone {
        void onDone(String result);
    }

    public netChecker(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected String doInBackground(OnCheckingDone... params) {
        this.callback = params[0];
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if ((info != null)&&(info.isConnected())) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent", "test");
                con.setRequestProperty("Connection", "close");
                con.setConnectTimeout(1000);
                con.connect();
                if (con.getResponseCode() == 200) return "1";
                else return "0";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "0";
            } catch (IOException e) {
                e.printStackTrace();
                return "0";
            }
        }
        return "0";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callback.onDone(s);
    }
}

