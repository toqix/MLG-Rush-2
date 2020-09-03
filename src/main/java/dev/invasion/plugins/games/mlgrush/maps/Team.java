package dev.invasion.plugins.games.mlgrush.maps;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
    private TeamColor color;
    private Respawn bed;
    private SerializableLocation spawn;

    public Team(TeamColor color) {
        this.color = color;
    }

    public void setBed(Respawn bed) {
        this.bed = bed;
    }

    public SerializableLocation getSpawn() {
        return spawn;
    }

    public void setSpawn(SerializableLocation spawn) {
        this.spawn = spawn;
    }

    public TeamColor getColor() {
        return color;
    }
    public String getName() {
        String toReturn = "noName";
        switch (color) {
            case RED:toReturn = "&cRed";break;
            case BLUE:toReturn = "&1Blue";break;
            case GREEN:toReturn ="&2Green";break;
            case YELLOW:toReturn ="&eYellow";break;
        }
        return toReturn;
    }

    public Respawn getBed() {
        return bed;
    }
}
