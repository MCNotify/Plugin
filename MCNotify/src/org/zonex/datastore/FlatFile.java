package org.zonex.datastore;

import org.bukkit.configuration.file.YamlConfiguration;
import org.zonex.datastore.fileModels.FileAreaTable;
import org.zonex.datastore.fileModels.FileSubscriptionTable;

import java.io.File;

public class FlatFile {

    private YamlConfiguration yamlConfiguration;
    private File file;

    public FlatFile(YamlConfiguration yamlFile, File file) {
        this.yamlConfiguration = yamlFile;
        this.file = file;
    }

    public FileAreaTable areaTable(){
        return new FileAreaTable(yamlConfiguration, file);
    }

    public FileSubscriptionTable subscriptionTable(){
        return new FileSubscriptionTable(yamlConfiguration, file);
    }

}
