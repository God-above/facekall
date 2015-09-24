package com.falcon.Utils.Node;

/**
 * Created by Administrator on 2015/7/29 0029.
 */
public class AVUserNode {
    //{"Identifier":"10086","UserSig":"{   \"TLS.account_type\" : \"\",   \"TLS.appid_at_3rd\" : \"\",   \"TLS.expiry_after\" : \"604800\",   \"TLS.identifier\" : \"10086\",   \"TLS.sdk_appid\" : \"\",   \"TLS.sig\" : \"MEUCIQCT4w6HFsq+gziOMKs6rWjRXXj8qSp9KQu4DkrQdkFgRAIgG4XLOqgmv1yq6iW8R7KC52aEMnYnXvWx8m9ak2QvIgw=\",   \"TLS.signed\" : \"appid_at_3rd,account_type,identifier,sdk_appid,time,expire_after\",   \"TLS.time\" : \"2015-07-29T16:14:28-08:00\"}"}

    public  String expiry_after;
    public  String identifier;
    public  String sig;
    public  String signed;
    public  String time;

}
