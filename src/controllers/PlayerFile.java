package controllers;

import org.json.simple.JSONObject;

public class PlayerFile extends SaveFile{
    //ADD INSTANCE VARIABLES OF PLAYER
    private String NAME = "Chase";
    private int points;
    private int night;


    public PlayerFile(){
        super("Chase");
        initializePlayerData();
    }

    private void initializePlayerData(){
        points = 0;
        night = 0;
    }

    private void setPlayerData(int points, int night){
        this.points = points;
        this.night = night;
    }

    public void setPoints(int points){
        this.points = points;
        this.save();
    }

    public int getPoints(){
        return points;
    }

    public int getNight(){
        return night;
    }

    public JSONObject toJSONObject(){
        JSONObject jo = new JSONObject();
        jo.put("points", points);
        jo.put("night", night);

        return jo;
    }

    public void importData(SaveFile playerFile) {
        PlayerFile pf = (PlayerFile) playerFile;
        this.points = pf.points;
        this.night = pf.night;
    }

    public SaveFile fromJSONObject(JSONObject jsonObject) {
        int p = ((Long) jsonObject.get("points")).intValue();
        int n = ((Long) jsonObject.get("night")).intValue();
        PlayerFile pf = new PlayerFile();
        pf.setPlayerData(p, n);

        return pf;
    }
}

