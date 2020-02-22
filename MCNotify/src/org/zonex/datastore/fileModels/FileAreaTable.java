package org.zonex.datastore.fileModels;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.parser.ParseException;
import org.zonex.areas.Area;
import org.zonex.datastore.baseModels.BaseAreaModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Flat file storage for player areas
 */
public class FileAreaTable extends BaseAreaModel {

    private ConfigurationSection areaConfig;
    private YamlConfiguration yamlConfiguration;
    private File file;


    public FileAreaTable(YamlConfiguration yaml, File file){
        this.areaConfig = yaml.getConfigurationSection("areas");
        this.yamlConfiguration = yaml;
        this.file = file;
    }

    @Override
    public ArrayList<Area> selectAll() {
        ArrayList<Area> areaList = new ArrayList<>();
        if(areaConfig != null) {
            // Loop players
            for (String uuid : areaConfig.getKeys(false)) {
                // Loop areas
                for(String areaName: areaConfig.getConfigurationSection(uuid).getKeys(false)) {
                    try {
                        areaList.add(((Area.deserialize((String)areaConfig.get(uuid + "." + areaName)))));
                    } catch (ParseException e) {
                        // Unable to parse the area
                        System.out.println("[ZoneX] unable to parse Area JSON at: " + uuid + "." + areaName);
                        continue;
                    }
                }
            }
        }
        return areaList;
    }

    @Override
    public ArrayList<Area> selectWhereUuid(String uuid) {
        ArrayList<Area> areaList = new ArrayList<>();

        if(areaConfig != null) {
            // Check player
            if (areaConfig.getConfigurationSection(uuid) != null) {
                // Loop areas
                for (String key : areaConfig.getConfigurationSection(uuid).getKeys(false)) {
                    Area area = null;
                    try {
                        area = ((Area.deserialize((String)areaConfig.get(uuid + "." + key))));
                    } catch (ParseException e) {
                        // Unable to parse the area
                        System.out.println("[ZoneX] unable to parse Area JSON at: " + uuid + "." + key);
                        continue;
                    }
                    areaList.add(area);
                }
            }
        }
        return areaList;
    }

    @Override
    public Area selectWhereId(int id) {
        if(areaConfig != null) {
            // Loop players
            for (String uuid : areaConfig.getKeys(false)) {
                // Loop areas
                for (String areaName : areaConfig.getConfigurationSection(uuid).getKeys(false)) {
                    Area area = null;
                    try {
                        area = (Area.deserialize((String)areaConfig.get(uuid + "." + areaName)));
                    } catch (ParseException e) {
                        // Unable to parse the area
                        System.out.println("[ZoneX] unable to parse Area JSON at: " + uuid + "." + areaName);
                        return null;
                    }
                    if (area.getAreaId() == id) {
                        return area;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean insert(Area area) {
        areaConfig.set(area.getOwner().getUniqueId().toString() + "." + area.getAreaName(), area.serialize());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean update(Area area) {
        if(areaConfig != null) {
            areaConfig.set(area.getOwner().getUniqueId().toString() + "." + area.getAreaName(), area.serialize());
            try {
                yamlConfiguration.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Area area) {
        areaConfig.set(area.getOwner().toString() + "." + area.getAreaName(), null);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
