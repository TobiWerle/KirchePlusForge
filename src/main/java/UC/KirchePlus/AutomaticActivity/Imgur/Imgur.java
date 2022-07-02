package UC.KirchePlus.AutomaticActivity.Imgur;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Imgur {
    private static final String UPLOAD_API_URL = "https://api.imgur.com/3/image";

    public static String uploadToLink(File file) {
        String json = upload(file);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        return jsonElement.getAsJsonObject().get("data").getAsJsonObject().get("link").getAsString();
    }

    public static String upload(File file) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection("https://api.imgur.com/3/image");
            writeToConnection(conn, "image=" + toBase64(file));
            return getResponse(conn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    private static String toBase64(File file) throws IOException {
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
        return URLEncoder.encode(new String(encoded, StandardCharsets.US_ASCII), StandardCharsets.UTF_8.toString());
    }

    private static HttpURLConnection getHttpConnection(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)(new URL(url)).openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID 2877715148da238");
        conn.setReadTimeout(100000);
        conn.connect();
        return conn;
    }

    private static void writeToConnection(HttpURLConnection conn, String message) throws IOException {
        try(OutputStream outputStream = conn.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
            writer.write(message);
            writer.flush();
        }
    }

    private static String getResponse(HttpURLConnection conn) throws IOException {
        InputStream inputStream = conn.getInputStream();
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } finally {
            inputStream.close();
        }
        return sb.toString();
    }

    /* Thanks to LogischerHD */
}