package com.qupp.client.utils.secretUtils;

import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qupp.client.MyApplication;


public class JwtUtils {


    private static String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImV4cGlyZXNJbiI6IjI1OTIwMDAifQ.eyJhdXRoZW50aWNhdGVkIjp0cnVlLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiIn1dLCJkZXRhaWxzIjp7ImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJhdXRob3JpdGllcyI6W3siJHJlZiI6IiQuYXV0aG9yaXRpZXNbMF0ifV0sImNyZWRlbnRpYWxzTm9uRXhwaXJlZCI6dHJ1ZSwiZW5hYmxlZCI6dHJ1ZSwidXNlcklkIjo2MjM1MDY0MzE3MzExMzQ0Njd9LCJwYXJhbXMiOnt9LCJwcmluY2lwYWwiOiIxNTY2OTk2NzExNyIsInVzZXJJZCI6IjYyMzUwNjQzMTczMTEzNDQ2NyJ9.d5LRB8c4uggV_YmNn2fnk6nxHXag9i_kygw3yUWzY0SoIYisNGDMa51BISRTFBRkreThfgjDqnGqycVGNJ5Tk8ZkQw4ekHS-hpmeMGr9Swbohf1zIV4EXrTD35mMB8ilUQ9dI3k-Vz-VtKrCBuFj6cce3mcT9K9-trmPt2LMA3w";


    public static void setUserId(String jwt) {
        try {
            JWT mjwt = new JWT(jwt.replace("bearer",""));
            String json = new Gson().toJson(mjwt.getClaims());
            JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
            String userId = jsonObject.get("userId").getAsJsonObject().get("value").getAsString();
            Log.d("mjwt", userId);
            MyApplication.setUserId(userId);
        }catch (Exception e){

        }
    }


}
