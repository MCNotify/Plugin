package org.mcnotify.utility;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;

public class Response {

    private int responseCode;
    private String responseBody;

    Response(int responseCode, String responseBody){
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }


    public JSONObject getResponseBody() throws ParseException {

        if(!this.responseBody.equals("")) {
            Object jsonobj = new JSONParser().parse(this.responseBody);
            JSONObject jsonObject = (JSONObject) jsonobj;
            return jsonObject;
        } else {
            return null;
        }
    }

    public int getResponseCode() {
        return responseCode;
    }
}
