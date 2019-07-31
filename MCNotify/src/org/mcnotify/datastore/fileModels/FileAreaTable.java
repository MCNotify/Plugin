package org.mcnotify.datastore.fileModels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONObject;
import org.mcnotify.areas.Area;
import org.mcnotify.areas.Polygon;
import org.mcnotify.areas.protection.Protection;
import org.mcnotify.datastore.baseModels.BaseAreaModel;

import java.util.ArrayList;
import java.util.UUID;

public class FileAreaTable extends BaseAreaModel {

    public ConfigurationSection areaConfig;

    public FileAreaTable(ConfigurationSection configurationSection){
        this.areaConfig = configurationSection;
    }

    @Override
    public ArrayList<Area> selectAll() {
        ArrayList<Area> areaList = new ArrayList<>();
        if(areaConfig.getConfigurationSection("areas") != null) {
            for (String key : areaConfig.getConfigurationSection("areas").getKeys(false)) {
                areaList.add(this.getArea(areaConfig.getConfigurationSection("areas." + key)));
            }
        }
        return areaList;
    }

    @Override
    public Area selectWhereUuid(String uuid) {
        if(areaConfig.getConfigurationSection("areas") != null) {
            return this.getArea(areaConfig.getConfigurationSection("areas." + uuid));
        } else {
            return null;
        }
    }

    @Override
    public Area selectWhereId(int id) {
        if(areaConfig.getConfigurationSection("areas") != null) {
            for (String key : areaConfig.getConfigurationSection("areas").getKeys(false)) {
                Area area = this.getArea(areaConfig.getConfigurationSection("areas." + key));
                if(area.getAreaId() == id){
                    return area;
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Area area) {
        return false;
    }

    @Override
    public boolean update(Area area) {
        return false;
    }

    @Override
    public boolean delete(Area area) {
        return false;
    }

    private Area getArea(ConfigurationSection section){
        if(section != null) {
            int areaId = section.getInt("id");
            String ownerUuid = section.getString("uuid");
            String areaName = section.getString("area_name");
            String jsonPoly = section.getString("polygon");
            String world = section.getString("world");
            String protectionString = section.getString("protections");
            String whitelist = section.getString("whitelist");

            String[] playerStrings = whitelist.split(",");
            ArrayList<OfflinePlayer> playerList = new ArrayList<>();
            for (String s : playerStrings) {
                if (s != null && s != "") {
                    UUID uuid = UUID.fromString(s);
                    if (uuid != null) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(s));
                        playerList.add(op);
                    }
                }
            }

            OfflinePlayer owner = null;

            if (ownerUuid != null && ownerUuid != "") {
                UUID uuid = UUID.fromString(ownerUuid);
                if (uuid != null) {
                    owner = Bukkit.getOfflinePlayer(UUID.fromString(ownerUuid));
                }
            }

            return new Area(areaId, owner, new Polygon(jsonPoly), areaName, world, Protection.fromString(protectionString), playerList);
        } else {
            return null;
        }
    }

    private YamlConfiguration toYaml(Area area){
        if(area != null){
            // TODO: convert to Json string.
            return null;
        } else {
            return null;
        }
    }
}
