package org.zonex.commands.autocomplete;

import org.bukkit.entity.Player;
import org.zonex.ZoneX;
import org.zonex.areas.Area;

import java.util.ArrayList;
import java.util.List;

public class ZoneNameAutoCompleteNode extends AutoCompleteNode {
    @Override
    public boolean matches(String s) {
        return true;
    }

    @Override
    public List<String> getSuggestedCommands(Player player, String s) {
        List<String> areas = new ArrayList<>();
        for(Area area : ZoneX.areaManager.getAreas(player.getUniqueId())){
            if(area.getAreaName().startsWith(s)){
                areas.add(area.getAreaName());
            }
        }
        return areas;
    }
}
