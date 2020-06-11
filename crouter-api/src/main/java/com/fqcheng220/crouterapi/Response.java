package com.fqcheng220.crouterapi;

import android.content.Intent;

public class Response {
    public int mResultCode;
    public Intent mResultIntent;

    public Response(int mResultCode,Intent mResultIntent){
        this.mResultCode = mResultCode;
        this.mResultIntent = mResultIntent;
    }
}
