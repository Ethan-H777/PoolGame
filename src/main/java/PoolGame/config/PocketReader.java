package PoolGame.config;

import PoolGame.GameManager;
import PoolGame.builder.PoolPocketBuilder;
import PoolGame.objects.Pocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PocketReader implements Reader {
    /**
     * parse the json file and create pockets
     * @param path of json file
     * @param gameManager
     */
    @Override
    public void parse(String path, GameManager gameManager) {
        JSONParser parser = new JSONParser();
        ArrayList<Pocket> pockets = new ArrayList<Pocket>();

        try{
            Object object = parser.parse(new FileReader(path));
            JSONObject jo = (JSONObject) object;
            JSONObject jTable = (JSONObject) jo.get("Table");
            JSONArray jPockets = (JSONArray) jTable.get("pockets");

            for (Object obj : jPockets){
                JSONObject jPocket = (JSONObject) obj;
                Double posX = (Double) ((JSONObject) jPocket.get("position")).get("x");
                Double posY = (Double) ((JSONObject) jPocket.get("position")).get("y");

                Double radius = (Double) jPocket.get("radius");

                PoolPocketBuilder builder = new PoolPocketBuilder();
                builder.setPosX(posX);
                builder.setPosY(posY);
                builder.setRadius(radius);
                pockets.add(builder.build());
            }
            gameManager.setPocketsToTable(pockets);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
    }
}
