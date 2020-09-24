package com.cb.authorization.api;

import com.cb.authorization.cache.LRUCache;
import com.cb.authorization.database.models.Token;
import com.cb.authorization.database.models.User;
import com.cb.authorization.database.models.UserObject;
import com.cb.authorization.misc.Misc;
import com.cb.authorization.misc.Misc.KeyValPair;
import com.cb.authorization.misc.Sha256Generator;
import com.cb.authorization.services.Auth;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.mysql.cj.xdevapi.JsonArray;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import static java.lang.System.exit;

@Path("/api/v1/auth")
@Consumes(MediaType.APPLICATION_JSON + "; charset=utf-8")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    private LRUCache<String, String> cache= new LRUCache<String, String>(10);
    private LRUCache<String, String> tokenCache= new LRUCache<String, String>(10);
    private Sha256Generator hashGen = new Sha256Generator();
    private Auth auth;

    public AuthResource(Auth auth) {
        this.auth = auth;
    }

    @POST()
    @Path("/generateToken")
    public Response auth(User user) {
        String hash = "-1";
        String cachedHash = cache.get(user.getEmail());
        if(cachedHash != null) {
            return Response.status(200).entity(Misc.JSON.createJSON(new Misc().new KeyValPair("hash", cachedHash))).build();
        }
        try {
            hash = hashGen.toHexString(hashGen.getSHA(user.getEmail()));

        }catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }

        if(hash.equals("-1")) {
            return Response.status(500).entity(Misc.JSON.createJSON(new Misc().new KeyValPair("message", "token generation error"))).build();
        }

        List<Token> tokens = auth.getToken(user.getEmail());
        hash = tokens.size() != 1 ? auth.saveAuth(user.getEmail(), hash) : tokens.get(0).getToken();

        if(!hash.equals("error")) {
            cache.put(user.getEmail(), hash);
        }
        return hash.equals("error") ? Response.status(500).entity(Misc.JSON.createJSON(new Misc().new KeyValPair("message", "token saving error"))).build()
        : Response.status(200).entity(Misc.JSON.createJSON(new Misc().new KeyValPair("hash", hash))).build();
    }

    @GET()
    @Path("/isAuthorized/{token}")
    public Response isAuthorized(@PathParam("token") final String token) {
        JsonObject object = new JsonObject();
        if(tokenCache.contains(token)) {
            object.addProperty("success", 1);
            return Response.status(200).entity(object.toString()).build();
        }
        List<Token> result = auth.verifyToken(token);
        if(result.size() != 1) {
            object.addProperty("success", 0);
            return Response.status(401).entity(object.toString()).build();
        }
        tokenCache.put(token, result.get(0).getEmail());
        object.addProperty("success", 1);
        return Response.status(200).entity(object.toString()).build();
    }

    @POST()
    @Path("/login")
    public Response login(User user) throws ClientProtocolException, IOException {
        JsonObject object = new JsonObject();
        object.addProperty("email", user.getEmail());
        object.addProperty("password", user.getPassword());

        //send a request to users microservice
        JSONObject authObject = Misc.HttpRequest.createRequest(
                Misc.Config.usersServiceUrl + ":8080/api/v1/users/login",
                "post",
                object.toString());

        if(authObject.get("authenticated") == (Boolean)false) {
            return Response.status(401).entity(JSONValue.parse("{\"message\" : \"unauthorized\"}")).build();
        }

        //if authorization verified then generate the token
        JSONObject tokenObject = Misc.HttpRequest.createRequest(
                Misc.Config.authServiceUrl + ":8080/api/v1/auth/generateToken",
                "post",
                object.toString());
        return Response.status(200).entity(tokenObject).build();
    }
}
