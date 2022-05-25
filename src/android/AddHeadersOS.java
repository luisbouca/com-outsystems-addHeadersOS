package com.outsystems.addheadersos;

/*
 * OutSystems Cordova Loader
 *
 * Copyright (C) 2016 OutSystems.
 *
 */

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddHeadersOS extends CordovaPlugin {

    private final String ADDHEADER = "AddHeader";
    private final String LISTHEADERS = "ListHeaders";
    private final String CLEARHEADERS = "ClearHeaders";
    private final String SETHEADERS = "SetHeaders";

    @Override
    protected void pluginInitialize() {
        String headersString = SharedPrefUtils.getStringData(cordova.getContext(),"headers");
        if(headersString == null || headersString.equals("[]")){
            SharedPrefUtils.saveData(cordova.getContext(),"headers","[]");
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        switch (action){
            case LISTHEADERS:
                callbackContext.success(SharedPrefUtils.getStringData(cordova.getContext(),"headers"));
                return true;
            case CLEARHEADERS:
                SharedPrefUtils.saveData(cordova.getContext(),"headers","[]");
                callbackContext.success();
                return true;
            case SETHEADERS:
                String headersString = args.getString(0);
                if (headersString.endsWith("]") && headersString.startsWith("[")){
                    SharedPrefUtils.saveData(cordova.getContext(),"headers",headersString);
                    callbackContext.success();
                }else {
                    callbackContext.error("Headers String is not a json array!");
                }
                return true;
            case ADDHEADER:
                String headerString = args.getString(0);
                if (headerString.startsWith("{") && headerString.endsWith("}")){
                    String headersString2 = SharedPrefUtils.getStringData(cordova.getContext(),"headers");
                    JSONArray headers = new JSONArray(headersString2);
                    JSONObject header = new JSONObject(headerString);
                    headers.put(header);
                    SharedPrefUtils.saveData(cordova.getContext(),"headers",headers.toString());
                    callbackContext.success();
                }else {
                    callbackContext.error("Header String is not a json object!");
                }
                return true;
            default:
                //error action not mapped
                callbackContext.error("Action is not mapped in the plugin!");
                return false;
        }
    }
}
