package dev.invasion.plugins.games.mlgrush.maps;

import java.util.ArrayList;

public class MapManager {
    private ArrayList<gameMap> Maps = new ArrayList<>();

    public ArrayList getMaps() {
        return Maps;
    }

    public void addMap(gameMap map) {
        Maps.add(map);
    }

    public gameMap getMap(int id) {
        /*if(id >= 0 && Maps.size() <= id) {
           return Maps.get(id);
        }else {
            return null;
        }*/
        return Maps.get(id);
    }
}