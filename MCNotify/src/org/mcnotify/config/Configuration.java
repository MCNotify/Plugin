package org.mcnotify.config;

import org.mcnotify.MCNotify;

public enum Configuration {
    SECRET_KEY("server.secret_key", randomAlphaNumeric(64), true),
    RECOVERY_EMAIL("server.recovery_email", "", true),

    DATABASE_HOST("server.database.host", "localhost", true),
    DATABASE_PORT("server.database.port", "3306", true),
    DATABASE_DATABASE("server.database.database", "MCNotify", true),
    DATABSE_USERNAME("server.database.username", "root", true),
    DATABASE_PASSWORD("server.database.password", "", true),

    AREA_DEFAULT_PROTECT("areas.default.protect", "true", true),
    AREA_DEFAULT_MOBPROTECT("areas.default.mobProtect", "true", true),
    AREA_DEFAULT_STOPLIQUID("areas.default.stopLiquid", "true", true),
    AREA_DEFAULT_CHESTLOCK("areas.default.chestLock", "true", true),
    AREA_DEFAULT_NOREDSTONE("areas.default.noRedstone", "true", true),
    AREA_DEFAULT_NOFIRE("areas.default.noFire", "true", true),
    AREA_DEFAULT_DOORLOCK("areas.default.doorLock", "false", true),
    AREA_DEFAULT_NOENTER("areas.default.noEnter", "false", true),
    AREA_DEFAULT_NOINTERACT("areas.default.noInteract", "true", true),
    AREA_DEFAULT_NOPVP("areas.default.noPvp", "true", true),
    AREA_LIMIT("areas.limit", "3", true),

    ON_PLAYER_JOIN_APPROVAL("events.onplayerjoin.approval_required", "true", true),
    ON_PLAYER_JOIN_ALLOWED("events.onplayerjoin.allow", "false", true),
    ON_PLAYER_ENTER_ALLOWED("events.onplayerenter.allow", "true", true),
    ON_BLOCK_BREAK_ALLOWED("events.onblockbreak.allow", "true", true),
    ON_CROP_GROW_ALLOWED("events.oncropgrow.allow", "true", true),
    ON_ENTITY_EXPLODE_ALLOWED("events.onentityexplode.allow", "true", true),
    ON_REDSTONE_ALLOWED("events.onredstone.allow", "true", true);



    private final String yamlName;
    private final String defaultValue;
    private final boolean isRequired;

    Configuration(String name, String defaultValue, boolean isRequired){
        this.yamlName = name;
        this.defaultValue = defaultValue;
        this.isRequired = isRequired;
    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    public String getValue(){
        return MCNotify.config.getConfigValue(this.yamlName);
    };

    public String getYamlName(){
        return this.yamlName;
    }

    public void setValue(String value){
        MCNotify.config.setConfigValue(this.yamlName, value);
    }

    public void setDefaultValue(){
        MCNotify.config.setConfigValue(this.yamlName, this.defaultValue);
    }

    public boolean isRequired(){
        return this.isRequired;
    }

}
