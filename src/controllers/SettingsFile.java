package controllers;

import org.json.simple.JSONObject;

public class SettingsFile extends SaveFile{
    //ADD INSTANCE VARIABLES OF SETTINGS
    private boolean closeCaption;

    public SettingsFile(){
        super();
    }

    public SettingsFile(String fileName) {
        super(fileName);
        setDefaultSettings();
    }


    public void setDefaultSettings(){
        closeCaption = true;
    }

    public void setSettings(boolean closeCaption){
        this.closeCaption = closeCaption;
    }

    public boolean getCloseCaption(){
        return closeCaption;
    }

    public void importData(SaveFile settingsFile){
        SettingsFile sf = (SettingsFile) settingsFile;
        this.closeCaption = sf.closeCaption;
    }

    public JSONObject toJSONObject(){
        JSONObject jo = new JSONObject();
        jo.put("Close Captions", closeCaption); // IF ADDED NEW INSTANCE VARIABLES SETTINGS MAKE SURE TO put into JSONObject
        return jo;
    }

    public SaveFile fromJSONObject(JSONObject jsonObject) {
        boolean cc = ((boolean) jsonObject.get("Close Captions")); // IF ADDED NEW INSTANCE VARIABLES
        SettingsFile sv = new SettingsFile();
        sv.setSettings(cc);

        return sv;
    }
}

