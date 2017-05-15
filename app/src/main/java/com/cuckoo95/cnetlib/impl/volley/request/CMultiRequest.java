package com.cuckoo95.cnetlib.impl.volley.request;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cuckoo95.cnetlib.CNet;
import com.cuckoo95.cnetlib.util.HttpHeaderUtil;
import com.cuckoo95.cutillib.CListUtil;
import com.cuckoo95.cutillib.log.CLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Cuckoo
 * @date 2016-12-28
 * @description
 *      同时处理文字和文件上传
 */

public class CMultiRequest extends StringRequest {
    private HashMap<String, Object> postParams = null;
    private HashMap<String, String> headerMap = null;

    public CMultiRequest(int method, String url, HashMap<String, Object> postParams,
                         HashMap<String, String> headerMap,
                         Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.postParams = postParams;
        this.headerMap = headerMap;
    }

    @Override
    public Map<String, String> getHeaders() {
        return HttpHeaderUtil.appendAllHeader(headerMap);
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + Part.getBoundary();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Part.sendParts(baos, getAllParams());
        } catch (IOException e) {
            CLog.e(CNet.LOG_TAG,"error when sending parts to output!",e);
        }
        return baos.toByteArray();
    }

    private Part[] getAllParams() {
        ArrayList<Part> partList = new ArrayList<>();
        if (!CListUtil.isEmpty(postParams)) {
            Iterator<Map.Entry<String, Object>> it = postParams.entrySet().iterator();
            Map.Entry<String, Object> entry = null;
            Part part = null;
            while (it.hasNext()) {
                entry = it.next();
                if (entry.getValue() instanceof String) {
                    part = new StringPart(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof File) {
                    try {
                        part = new FilePart(entry.getKey(), (File) entry.getValue());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (part != null) {
                    partList.add(part);
                }
            }
        }
        return partList.toArray(new Part[partList.size()]);
    }
}
