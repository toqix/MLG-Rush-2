package dev.invasion.plugins.games.mlgrush.maps;

import java.util.ArrayList;

public class MapManager {
    private ArrayList<gameMap> Maps;

    public MapManager() {
        Maps = new ArrayList<gameMap>();
    }

    public ArrayList<gameMap> getMaps() {
        return Maps;
    }

    public ArrayList<gameMap> getMaps(MapState state) {
        ArrayList<gameMap> toReturn = new ArrayList<>();
        for(gameMap map: getMaps()) {
            if(map.getMapState() == state) {
                toReturn.add(map);
            }
        }
        return toReturn;
    }

    public void addMap(gameMap map) {
        Maps.add(map);
    }

    public gameMap getMap(int id) {
        if(id >= 0 && id < Maps.size()) {
           return Maps.get(id);
        }else {
            return null;
        }

    }
}