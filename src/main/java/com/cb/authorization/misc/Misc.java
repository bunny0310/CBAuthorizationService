package com.cb.authorization.misc;

import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;

public class Misc {
    public Misc() {

    }
    public static class Config {
        public static Boolean usersServiceProduction = true;
        public static String usersServiceUrl = usersServiceProduction ? "http://35.227.72.50" : "http://localhost";

        public static Boolean authServiceProduction = true;
        public static String authServiceUrl = authServiceProduction ? "http://35.227.72.50" : "http://localhost";
    }
    public static class HttpRequest {
        public static JSONObject createRequest(String endpoint, String method, String body) throws IOException {
            HttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = null;
            HttpEntity httpEntity = null;
            String apiOutput = "";
            if(method.equals("post")) {
                HttpPost httpPost = new HttpPost(endpoint);
                httpPost.addHeader("accept", "application/json");
                if(!body.equals("")){
                    httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
                }
                response = httpclient.execute(httpPost);
            }
            else if(method.equals("get")) {
                HttpGet httpGet = new HttpGet(endpoint);
                httpGet.addHeader("accept", "application/json");
                response = httpclient.execute(httpGet);
            }
            httpEntity = response.getEntity();
            apiOutput = EntityUtils.toString(httpEntity);
            JSONParser parser = new JSONParser();
            JSONObject json = null;
            try {
                json = (JSONObject) parser.parse(apiOutput);
            }catch (Exception e) {
                System.out.println(e + "");
            }
            return json;
        }
    }
    public static class JSON {
        public static Object createJSON(KeyValPair pair) {
            JSONObject obj = new JSONObject();
            obj.put(pair.key, pair.val);
            return obj;
        }
        public static Object createJSON(ArrayList<KeyValPair> pairs) {
            JSONObject obj = new JSONObject();
            for(KeyValPair pair : pairs) {
                obj.put(pair.key, pair.val);
            }
            return obj;
        }
    }

    public class KeyValPair{
        String key;
        String val;
        public KeyValPair() {

        }
        public KeyValPair(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }
}
