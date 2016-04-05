package hmdq.js.codeproject.dekirunihongo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.StringTokenizer;

/**
 * Class này có nhiệm vụ kết nối đến database và trả về dữ liệu cung cấp cho app<br>
 * Database là MySQL, dữ liệu nhận về ở dạng <b>JSON</b><br>
 * Class sẽ phân giải JSON ra và dữ liệu cuối cũng trả về sẽ là 2 list<br>
 * <b>Class yêu cầu permisson INTERNET</b>
 */
public class DataGetter {
    public static final String HOST_ADDRESS = "http://10.22.172.16/MFS/";
    // String lưu địa chỉ của host, khi up server lên host cầm lưu ý chỉnh sửa
    // Nếu muốn test ở localhost, thay chuỗi trên bằng IP của máy
    // Cách lấy IP: http://cuuhotinhoc.com/2-cach-kiem-tra-dia-chi-ip-cua-may-tinh-nhanh-nhat/

    public static final String FILE_NAME = "cf.php";
    // Tên file sử lí request của server

    private HttpURLConnection con;
    // Connection đén server

    private URL URL;
    // URL đến nơi mà ta cần request

    private InputStream stream;

    private InputStreamReader reader;

    GettingData gd;
    // Instance của class GettingData

    /**
     * Khỏi tạo dữ liệu cho instance
     */
    public DataGetter() {
        super();
        gd = new GettingData();
    }

    /**
     * Hàm này sẽ tiến hành request data từ server dựa vào các thông tin được cung cấp
     * Khi đã lấy được dữ liệu từ server, hàm OnReceived trong interface OnDataReceived sẽ được chạy
     * @param book Số quyển sách được yêu cầu, đánh từ 1 đến 3
     * @param part Phần đươck yêu cầu: vocab, gra, listen
     * @param lesson Số bài;
     * @param requestCode Là một đoạn String sseer xác nhận xem lúc trả về có đúng kết quả được yêu cầu hay không
     * @param callback Thực thì khi đã lấy được data về
     */
    void requestData(String book, String part, String lesson, String requestCode, OnDataReceived callback) {
        String u = HOST_ADDRESS + FILE_NAME + "?book=" + book +
                                              "&part=" + part +
                                              "&lesson=" + lesson;
        gd.execute(u, requestCode, callback);
    }

    /**
     * Interface đóng vai trò là callback khi lấy dữ liệu xong
     */
    interface OnDataReceived {
        /**
         * Hàm được gọi khi dữ liệu đã được láy về theo request
         * @param key Giá trị ở chuổi thứ nhát (Từ vựng, cấu trúc,...)
         * @param value Giá trị tương ứng ở chuổi thứ hai (Nghĩa, giải thích,...)
         * @param requestCode Là Code để xác nhận với requestCode ở hàm requestData
         */
        public void onReceive(List<String> key, List<String> value, String requestCode);
    }

    /**
     * Class phụ trách việc láy dữ liệu<br>
     * Class chạy nền<br>
     * Android không cho phép tát cả các quá trình liên quan đến INTERNET như download,... được thực hiện trong Thread chính<br>
     * vì có thể gây ảnh hưởng đến chương trình
     */
    class GettingData extends AsyncTask<Object, Void, String> {
        String result = "";
        String rCode = "";
        OnDataReceived odg;

        @Override
        protected String doInBackground(Object... params) {
            String url = (String)params[0];
            rCode = (String) params[1];
            odg = (OnDataReceived) params[2];
            try {
                URL = new URL(url);
                con = (HttpURLConnection) URL.openConnection();
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
            List<String> key = new ArrayList<>();
            List<String> value = new ArrayList<>();
            try {
                JSONObject root = new JSONObject(s);
                JSONArray array = root.getJSONArray("res");
                for (int i = 0;i< array.length();i++) {
                    JSONObject o = array.getJSONObject(i);
                    key.add(o.getString("n"));
                    value.add(o.getString("m"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                odg.onReceive(key, value, rCode);
            }
        }
    }
}
