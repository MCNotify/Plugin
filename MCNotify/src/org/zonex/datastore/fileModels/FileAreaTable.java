package org.zonex.datastore.fileModels;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.zonex.areas.Area;
import org.zonex.datastore.baseModels.BaseAreaModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
                    areaList.add(((Area) (areaConfig.get(uuid + "." + areaName))));
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
                    Area area = ((Area) (areaConfig.get(uuid + "." + key)));
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
                    Area area = (Area) areaConfig.get(uuid + "." + areaName);
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
        areaConfig.set(area.getOwner().getUniqueId().toString() + "." + area.getAreaName(), area);
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
            areaConfig.set(area.getOwner().getUniqueId().toString() + "." + area.getAreaName(), area);
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
