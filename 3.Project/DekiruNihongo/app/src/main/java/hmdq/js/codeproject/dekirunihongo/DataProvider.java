package hmdq.js.codeproject.dekirunihongo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataProvider {
    public static final String HOST_NAME = "http://localhost/MFS/cf.php";

    HttpURLConnection con;

    InputStream stream;

    InputStreamReader reader;

    SQLiteDatabase db;

    Cursor query;

    gettingData gd;

    public DataProvider() {
        super();
        gd = new gettingData();
        db = SQLiteDatabase.openOrCreateDatabase("jsdk.db",  null);
        db.execSQL("CREATE TABLE IF NOT EXISTS info (num integer, rev integer, dat text");
        int num_row = db.rawQuery("SELECT count(*) from info", null).getInt(0);
        if (num_row == 0) db.execSQL("insert into info (num, rev) values(1, 0)");
    }

    int getLocalRev() {
        return db.rawQuery("SELECT rev FROM info", null).getInt(0);
    }

    String getLocalData() {
        return db.rawQuery("SELECT dat FROM info",null).getString(0);
    }

    boolean updateData(String rev, String data) {
        try {
           db.execSQL("UPDATE info SET rev=" + rev + ", dat=" + data + " where num=1");
        } catch (Exception e) {
           return false;
        }
        return true;
    }

    HashMap<String, String> getData(String book, String part, String lesson) {
        String tableName = part + book;
        String lessonName = "l" + lesson;
        HashMap<String, String> result = new HashMap<>();
        try {
            JSONObject root = new JSONObject(getLocalData());
            root = root.getJSONObject("data");
            JSONArray unit = root.getJSONObject(tableName).getJSONArray(lessonName);
            for (int i = 0; i< unit.length(); i++) {
                JSONObject o = unit.getJSONObject(i);
                result.put(o.getString("n"), o.getString("m"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    List<String> getListLesson(String book, String part) {
        String columnName = "b" + book;
        String tableName = (part.equals("vocab")) ? "list1" : "list2";
        List<String> result = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(getLocalData());
            root = root.getJSONObject("data");
            JSONArray table = root.getJSONObject(tableName).getJSONArray(columnName);
            for (int i = 0; i< table.length(); i++) result.add(table.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    void requestData (String rq, OnDataReceived callback) {
        String url = HOST_NAME + "?r=" + rq;
        gd.execute(url, callback);
    }

    void closeDB() {
        db.close();
    }

    interface OnDataReceived {
        void onReceive (String result);
    }

    private class gettingData extends AsyncTask<Object, Void, String> {
        OnDataReceived callback;

        @Override
        protected String doInBackground(Object... params) {
            String u = (String) params[0];
            callback = (OnDataReceived) params[1];
            String result = "";
            try {
                URL url = new URL(u);
                con = (HttpURLConnection) url.openConnection();
                stream = con.getInputStream();
                reader = new InputStreamReader(stream);
                int temp = 0;
                while ((temp = reader.read()) != -1) result += (char) temp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) con.disconnect();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            callback.onReceive(s);
        }
    }
}
