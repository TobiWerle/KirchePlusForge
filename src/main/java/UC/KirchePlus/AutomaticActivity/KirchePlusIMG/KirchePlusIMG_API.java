package UC.KirchePlus.AutomaticActivity.KirchePlusIMG;

import UC.KirchePlus.Config.KircheConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

public class KirchePlusIMG_API {

    public static String uploadIMG(File file) throws IOException {

        HttpPost request = new HttpPost("http://upload.kircheplus-mod.de/api/upload");
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("api", KircheConfig.token);
        try {
            request.addHeader(new BasicScheme().authenticate(creds, request, null));
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        String base64 = toBase64(file);
        StringEntity entity2 = new StringEntity(base64, ContentType.DEFAULT_BINARY);
        request.setEntity(entity2);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);


                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                return json.get("image_url").toString().replace("\"","");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Nicht erstellt!";
    }

    public static boolean isTokenValid() {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL("http://upload.kircheplus-mod.de/api/check");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            String basicAuth = Base64.getEncoder().encodeToString(("api:"+KircheConfig.token).getBytes(StandardCharsets.UTF_8));
            conn.setRequestProperty("Authorization", "Basic "+basicAuth);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(result.toString());

            return json.get("login").getAsBoolean();
        }catch (Exception ignored){}
        return false;
    }


    private static String toBase64(File file) throws IOException {
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
