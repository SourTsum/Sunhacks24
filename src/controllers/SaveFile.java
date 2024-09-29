package controllers;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class SaveFile {

    private static final int fileNUMBER = 1;
    private String fileName;

    public SaveFile() {

    }

    public SaveFile(String fileName) {
        this.fileName = fileName + "-" + Integer.toString(fileNUMBER);
    }

    public Path getDefaultPath(String fileName) {
        String home = "SaveData"; //name of SaveData Folder, which JSON save data will go into
        return Paths.get(home).resolve(fileName);
    }

    public String getFileName(){
        return fileName;
    }

    public JSONObject toJSONObject() {
        return null;
    }

    public abstract SaveFile fromJSONObject(JSONObject jsonObject);

    public abstract void importData(SaveFile settingsFile);

    public void save(){
        save(getDefaultPath(fileName));
    }

    public void save(Path p){
        String jsonText = this.toJSONObject().toJSONString();
        try {
            Files.write(p, jsonText.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(){
        load(getDefaultPath(fileName));
    }

    public void load(Path p){
        String jsonText = null;
        try {
            jsonText = new String(Files.readAllBytes(p));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONParser parser = new JSONParser();

        try {

            JSONObject jsonObject = (JSONObject) parser.parse(jsonText);
            SaveFile saveFile = fromJSONObject(jsonObject);
            this.importData(saveFile);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}