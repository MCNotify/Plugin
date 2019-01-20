package org.mcnotify.config;

import org.mcnotify.MCNotify;

public enum Configuration {
    SECRET_KEY("server.secret_key", randomAlphaNumeric(64), true),
    RECOVERY_EMAIL("server.recovery_email", " ", true),
    ON_PLAYER_JOIN_APPROVAL("events.onplayerjoin.approval_required", "false", false);


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
