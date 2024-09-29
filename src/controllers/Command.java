package controllers;

import java.util.HashMap;

public class Command {

    private String name;
    private String helpMSG;
    public HashMap<String, String> options = new HashMap<String,String>();
    public Command(String name, String helpMSG) {
        this.name = name;
        this.helpMSG = helpMSG;
    }

    public String getName() {
        return name;
    }

    public String getHelpMSG() {
        return helpMSG;
    }

    public void addOption(String name, String description) {
        options.put(name, description);
    }
}