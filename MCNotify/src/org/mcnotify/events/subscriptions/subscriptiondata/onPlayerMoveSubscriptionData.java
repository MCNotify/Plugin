package org.mcnotify.events.subscriptions.subscriptiondata;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcnotify.MCNotify;
import org.mcnotify.utility.Polygon;

public class onPlayerMoveSubscriptionData extends AreaSubscriptionData {


    public onPlayerMoveSubscriptionData(int areaId){
        super(areaId);
    }

    public onPlayerMoveSubscriptionData(){
        super();
    }

}
