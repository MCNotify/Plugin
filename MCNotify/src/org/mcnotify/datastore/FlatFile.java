package org.mcnotify.datastore;

import org.bukkit.configuration.file.YamlConfiguration;
import org.mcnotify.datastore.dbModels.DbAreaTable;
import org.mcnotify.datastore.dbModels.DbSubscriptionTable;
import org.mcnotify.datastore.fileModels.FileAreaTable;
import org.mcnotify.datastore.fileModels.FileSubscriptionTable;

public class FlatFile {

    private YamlConfiguration yamlConfiguration;

    public FlatFile(YamlConfiguration yamlFile) {
        this.yamlConfiguration = yamlFile;
    }

    public FileAreaTable areaTable(){
        return new FileAreaTable(yamlConfiguration.getConfigurationSection("areas"));
    }

    public FileSubscriptionTable subscriptionTable(){
        return new FileSubscriptionTable(yamlConfiguration.getConfigurationSection("subscriptions"));
    }

}
