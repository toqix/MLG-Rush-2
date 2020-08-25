package dev.invasion.plugins.games.mlgrush.maps;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Team {
    private TeamColor color;
    private Respawn bed;
    public Team(TeamColor color) {
        this.color = color;
    }
}
