package org.zonex.config;

import org.zonex.ZoneX;

public enum Configuration {
    /** Configuration **/
    SECRET_KEY("server.secret_key", randomAlphaNumeric(64)),
    RECOVERY_EMAIL("server.recovery_email", ""),

    DATABASE_ENABLED("server.database.enabled", "false"),
    DATABASE_HOST("server.database.host", "localhost"),
    DATABASE_PORT("server.database.port", "3306"),
    DATABASE_DATABASE("server.database.database", "MCNotify"),
    DATABSE_USERNAME("server.database.username", "root"),
    DATABASE_PASSWORD("server.database.password", ""),

    /** Communication **/
    INGAME_NOTIFICATIONS_ENABLED("communication.enabled", "true"),

    TWILIO_ENABLED("communication.twilio.enabled", "false"),
    TWILIO_ACCOUNT_SID("communication.twilio.account.sid", ""),
    TWILIO_PHONE_NUMBER("communication.twilio.account.number", ""),
    TWILIO_AUTH_TOKEN("communication.twilio.account.authToken", ""),

    EMAIL_ENABLED("communication.email.enabled", "false"),
    EMAIL_SMTP_SERVER("communication.email.smtp.server", ""),
    EMAIL_USERNAME("communication.email.smtp.username", ""),
    EMAIL_PASSWORD("communication.email.smtp.password", ""),
    EMAIL_FROM_ADDRESS("communication.email.smtp.fromEmail", ""),

    DISCORD_SRV_ENABLED("communication.discord.enabled", "false"),

    /** Area Protection **/
    AREA_DEFAULT_PROTECT("areas.default.protect", "true"),
    AREA_DEFAULT_MOBPROTECT("areas.default.mobProtect", "true"),
    AREA_DEFAULT_STOPLIQUID("areas.default.stopLiquid", "true"),
    AREA_DEFAULT_CHESTLOCK("areas.default.chestLock", "true"),
    AREA_DEFAULT_NOREDSTONE("areas.default.noRedstone", "true"),
    AREA_DEFAULT_NOFIRE("areas.default.noFire", "true"),
    AREA_DEFAULT_DOORLOCK("areas.default.doorLock", "false"),
    AREA_DEFAULT_NOENTER("areas.default.noEnter", "false"),
    AREA_DEFAULT_NOINTERACT("areas.default.noInteract", "true"),
    AREA_DEFAULT_NOPVP("areas.default.noPvp", "true"),
    AREA_LIMIT("areas.limit", "3"),

    /** Subscriptions **/
    ON_PLAYER_JOIN_APPROVAL("events.onplayerjoin.approval_required", "true"),
    ON_PLAYER_JOIN_ALLOWED("events.onplayerjoin.allow", "false"),
    ON_PLAYER_ENTER_ALLOWED("events.onplayerenter.allow", "true"),
    ON_BLOCK_BREAK_ALLOWED("events.onblockbreak.allow", "true"),
    ON_CROP_GROW_ALLOWED("events.oncropgrow.allow", "true"),
    ON_ENTITY_EXPLODE_ALLOWED("events.onentityexplode.allow", "true"),
    ON_REDSTONE_ALLOWED("events.onredstone.allow", "true"),

    /** Subscription Communication Details **/
    ON_PLAYER_ENTER_DETAILED_MESSAGE("events.onplayerenter.lang.showplayer", "true"),
    ON_BLOCK_BREAK_DETAILED_MESSAGE("events.onplayerenter.lang.showplayer", "true");



    private final String yamlName;
    private final String defaultValue;

    Configuration(String name, String defaultValue){
        this.yamlName = name;
        this.defaultValue = defaultValue;
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
        return ZoneX.config.getConfigValue(this.yamlName);
    };

    public String getYamlName(){
        return this.yamlName;
    }

    public void setValue(String value){
        ZoneX.config.setConfigValue(this.yamlName, value);
    }

    public String getDefaultValue(){
        return this.defaultValue;
    }

}
