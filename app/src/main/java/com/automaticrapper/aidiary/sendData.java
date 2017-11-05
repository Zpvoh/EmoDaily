import java.io.*;
import java.net.*;
public class sendData {
    public static String sendHttpRequest(String url, String entity, String method) {
        BufferedReader bufferedReader = null;
        URL realUrl;
        HttpURLConnection conn = null;
        PrintWriter printWriter = null;
        String result = "";
        try {
            if ("get".equals(method)) {
                if (entity != null && !"".equals(entity)) {
                    realUrl = new URL(url + "?" + entity);
                } else {
                    realUrl = new URL(url);
                }
                // 根据url生成urlConnection对象
                conn = (HttpURLConnection) realUrl.openConnection();
           /* conn.connect();可以不明文设定连接，conn.getInputStream()会自动连接*/
            } else if ("post".equals(method)) {
                realUrl = new URL(url);
                conn = (HttpURLConnection) realUrl.openConnection();
                conn.setRequestMethod("POST");/*设定请求的方法为"POST"，默认是GET */
                if (entity != null && !"".equals(entity)) {
                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    conn.setDoInput(true);
                /*设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在 http正文内，因此需要设为true, 默认情况下是false*/
                    conn.setDoOutput(true);
                    // Post 请求不能使用缓存
                    conn.setUseCaches(false);
                    conn.connect();
                    printWriter = new PrintWriter(conn.getOutputStream());
                    printWriter.print(entity);
                    printWriter.flush();
                }
            }else{
                throw new IllegalArgumentException("请输入正确的请求方式");
            }
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            if ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

    }
}
