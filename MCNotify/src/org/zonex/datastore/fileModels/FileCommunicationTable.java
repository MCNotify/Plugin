package org.zonex.datastore.fileModels;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.parser.ParseException;
import org.zonex.communication.notifications.CommunicationMethod;
import org.zonex.datastore.baseModels.BaseCommunicationModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Flat file storage for player communication methods
 */
public class FileCommunicationTable extends BaseCommunicationModel {

    private ConfigurationSection configurationSection;
    private YamlConfiguration yamlConfiguration;
    private File file;

    public FileCommunicationTable(YamlConfiguration yaml, File file){
        this.configurationSection = yaml.getConfigurationSection("communications");
        this.yamlConfiguration = yaml;
        this.file = file;
    }

    @Override
    public ArrayList<CommunicationMethod> selectAll() {
        ArrayList<CommunicationMethod> subscriptionList = new ArrayList<>();
        if(configurationSection != null) {
            for (String uuid : configurationSection.getKeys(false)) {
                for(String key : configurationSection.getConfigurationSection(uuid).getKeys(false)) {
                    try {
                        subscriptionList.add((CommunicationMethod.deserialize((String) configurationSection.get(uuid + "." + key))));
                    } catch (ParseException e) {
                        // Unable to parse the subscription
                        System.out.println("[ZoneX] unable to parse CommunicationMethod JSON at:" + uuid + "." + key);
                        return subscriptionList;
                    }
                }
            }
        }
        return subscriptionList;
    }

    @Override
    public ArrayList<CommunicationMethod> selectWhereUuid(String uuid) {
        ArrayList<CommunicationMethod> subscriptionList = new ArrayList<>();
        if(configurationSection != null) {
            if (configurationSection.getConfigurationSection(uuid) != null) {
                for (String key : configurationSection.getConfigurationSection(uuid).getKeys(false)) {
                    CommunicationMethod subscription = null;
                    try {
                        subscription = (CommunicationMethod.deserialize((String)configurationSection.get(uuid + "." + key)));
                    } catch (org.json.simple.parser.ParseException e) {
                        // Unable to parse the subscription
                        System.out.println("[ZoneX] unable to parse CommunicationMethod JSON at:" + uuid + "." + key);
                        return subscriptionList;
                    }
                    subscriptionList.add(subscription);
                }
            }
        }
        return subscriptionList;
    }

    @Override
    public boolean insert(CommunicationMethod method) {
        configurationSection.set(method.getPlayer().getUniqueId().toString() + "." + method.getProtocol().toString(), method.serialize());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean update(CommunicationMethod method) {
        configurationSection.set(method.getPlayer().getUniqueId().toString() + "." + method.getProtocol().toString(), method.serialize());
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean delete(CommunicationMethod method) {
        configurationSection.set(method.getPlayer().getUniqueId().toString() + "." + method.getProtocol().toString(), null);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
