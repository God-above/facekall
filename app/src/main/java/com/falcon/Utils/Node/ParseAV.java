package com.falcon.Utils.Node;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class ParseAV {
    public static AVUserNode parseAVUserNode(JSONObject data) {
        Log.d("ParseAV",data.toString());
        AVUserNode node=new AVUserNode();
        try {
            node.expiry_after=data.getString("TLS.expiry_after");
            node.identifier=data.getString("TLS.identifier");
            node.sig=data.getString("TLS.sig");
            node.signed=data.getString("TLS.signed");
            node.time=data.getString("TLS.time");

        }catch (JSONException e){
            Log.e("ParseAV", e.toString());
        }

        return node;
    }
}
