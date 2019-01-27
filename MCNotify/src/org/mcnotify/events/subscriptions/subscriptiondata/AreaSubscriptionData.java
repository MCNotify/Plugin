package org.mcnotify.events.subscriptions.subscriptiondata;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.utility.Polygon;

public abstract class AreaSubscriptionData extends SubscriptionData {

    int areaId;

    AreaSubscriptionData(int areaId){
        this.areaId = areaId;
    }

    AreaSubscriptionData(){

    }

    @Override
    public SubscriptionData fromJSON(String json) {
        if(json == null || json == ""){
            return this;
        }

        try {
            Object obj = new JSONParser().parse(json);
            JSONObject jsonObject = (JSONObject) obj;
            int areaId =  Math.toIntExact((Long)jsonObject.get("areaId"));
            this.areaId = areaId;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public String toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("areaId", this.areaId);
        return obj.toJSONString();
    }


    public Polygon getArea(){
        return MCNotify.areaManager.getArea(areaId).getPolygon();
    }
}
