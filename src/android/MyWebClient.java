package com.outsystems.addheadersos;


import android.content.Context;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import com.outsystems.plugins.loader.clients.WebClient;
import com.outsystems.plugins.oscache.cache.interfaces.CacheEngine;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyWebClient extends WebClient {
    public MyWebClient(Context context, CordovaWebView webView, CordovaInterface cordova, CacheEngine cacheEngine, CordovaPreferences preferences) {
        super(context, webView, cordova, cacheEngine, preferences);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse response = super.shouldInterceptRequest(view,request);

        if (response == null){

            String url = request.getUrl().toString();
            try {
                OkHttpClient httpClient = new OkHttpClient();
                Request.Builder newRequestBuilder = new Request.Builder()
                        .url(url.trim());

                String headersString = SharedPrefUtils.getStringData(view.getContext(), "headers");
                if (headersString != null) {

                    JSONArray headers = new JSONArray(headersString);

                    for (int i = 0; i < headers.length(); i++) {
                        JSONObject header = headers.getJSONObject(i);
                        String query = header.getString("query");
                        if (query.startsWith("/") && query.endsWith("/")) {
                            //regex
                            query = query.substring(1, query.length() - 2);
                            if (Pattern.matches(query, url)) {
                                String key = header.getString("key");
                                String value = header.getString("value");
                                newRequestBuilder.addHeader(key, value);
                            }
                        } else {
                            //normal contains
                            if (url.contains(query)) {
                                String key = header.getString("key");
                                String value = header.getString("value");
                                newRequestBuilder.addHeader(key, value);
                            }
                        }
                    }

                    Request newRequest = newRequestBuilder.build();

                    Response newResponse = httpClient.newCall(newRequest).execute();
                    if (newResponse.body() != null) {
                        response = new WebResourceResponse(
                                getMimeType(url), // set content-type
                                newResponse.header("content-encoding", "utf-8"),
                                newResponse.body().byteStream()
                        );
                    }
                }
            }  catch (IOException | JSONException e) {
            }
        }

        return response;
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            if (extension.equals("js")) {
                return "text/javascript";
            }
            else if (extension.equals("woff")) {
                return "application/font-woff";
            }
            else if (extension.equals("woff2")) {
                return "application/font-woff2";
            }
            else if (extension.equals("ttf")) {
                return "application/x-font-ttf";
            }
            else if (extension.equals("eot")) {
                return "application/vnd.ms-fontobject";
            }
            else if (extension.equals("svg")) {
                return "image/svg+xml";
            }
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
