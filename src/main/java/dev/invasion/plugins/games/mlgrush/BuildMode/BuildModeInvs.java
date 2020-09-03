package dev.invasion.plugins.games.mlgrush.BuildMode;

import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.Stats.StatsManager;
import dev.invasion.plugins.games.mlgrush.Utils.InvOpener;
import dev.invasion.plugins.games.mlgrush.Utils.InventoryHandler;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import dev.invasion.plugins.games.mlgrush.maps.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BuildModeInvs {
    public static Inventory BuildMode() {
        Inventory inv = InventoryHandler.createInventory("&6Build&7Mode");

        inv.setItem(20, InventoryHandler.createStack(Material.SANDSTONE_WALL, "&aCreate&7 a new Map", Arrays.asList("&7Start Building a brand new Map"),"b(create)", true));
        inv.setItem(22, InventoryHandler.createStack(Material.SANDSTONE, "&aEdit&7 an existing Map", Arrays.asList("&7Start editing a Map which already exists"), "b(edit)", false));
        return inv;
    }

    public static Inventory BoxNotEmtpy() {
        Inventory inv = InventoryHandler.createInventory("&cThe Map is not Empty!", 27);

        inv.setItem(9, InventoryHandler.createStack(Material.RED_DYE, "&cDelete", Arrays.asList("&cDelete &7the Blocks inside", "&7inside the new Map", "&cmay delete something important"), "b(notemptydelete)", false));
        inv.setItem(17, InventoryHandler.createStack(Material.BARRIER, "&7Abort", Arrays.asList("&7Abort the creation of the Map", "&awon't destroy anything"), "b(notemptyabort)"));
        return inv;
    }

    public static Inventory EditMapMode(Player player) {
        Inventory inv = InventoryHandler.createInventory("&6Choose &7Map to edit");
        StatsManager stats = MLGRush.getStatsManager();
        inv.setItem(0, InventoryHandler.createStack(Material.BOOK, "&6Stats", Arrays.asList("&7&fTotal Maps Created: &6" + stats.getGeneralStats().getTotalMaps(), "&7&fFritz ist gaga"), true));

        for(int i = 9; i < 36; i++) {
            inv.setItem(i, new ItemStack(Material.AIR));
        }
        for(int i = 0; i <27; i++) {
            int slot = 9 + i;
            int mapId = PlayerDataManager.getPlayerData(player).getPage() * 27 + i;

            gameMap map = MLGRush.getMapManager().getMap(mapId);
            if(map != null) {
                if (map.getMapState() == MapState.GAME) {
                    inv.setItem(slot, InventoryHandler.createStack(Material.BARRIER, map.getName(), Arrays.asList("&cThere's an &aactive &c Game", "&con this Map")));
                } else if (map.getMapState() == MapState.BUILD) {
                    ArrayList<String> builderNames = new ArrayList<>();
                    builderNames.add("&7You can &ajoin");
                    for (Player builder : PlayerDataManager.getPlayers(map)) {
                        builderNames.add("&6" + builder.getName());
                    }
                    builderNames.add("&7editing this Map");
                    inv.setItem(slot, InventoryHandler.createStack(Material.SANDSTONE, map.getName(), builderNames, "e(" + mapId + ")", true));
                }else if(map.getMapState() == MapState.WAITING) {
                    inv.setItem(slot, InventoryHandler.createStack(Material.SANDSTONE, map.getName(), Arrays.asList("&aStart&7 editing", "&7this nice Map"), "e(" + mapId + ")"));
                }
            }

        }
        inv.setItem(36, InventoryHandler.createStack(Material.END_CRYSTAL, "&7Previous Page", Collections.singletonList("&7Current Page: " + PlayerDataManager.getPlayerData(player).getPage()), "p(-)", false));
        inv.setItem(44, InventoryHandler.createStack(Material.END_CRYSTAL, "&7Next Page", Collections.singletonList("&7Current Page: " + PlayerDataManager.getPlayerData(player).getPage()), "p(+)", false));

        return inv;
    }

    public static Inventory BuildInv(gameMap map) {
        Inventory inv = InventoryHandler.createInventory("&6Build&7Mode");
        ArrayList<String> lore = new ArrayList<>();
        if(BuildModeManager.checkFinished(map)) {
            lore.add("&aThe map is fully configured");
            lore.add("&aand can be finished now");
        }else {
            lore.add("&6Teams: ");
            for (Team team : map.getTeamManager().getTeams()) {
                StringBuilder string = new StringBuilder();
                string.append("Team: ").append(team.getName()).append("&7 : ");
                if (team.getBed() == null) string.append("&cBed missing");
                if (team.getSpawn() == null) string.append(" &cSpawn missing");
                if (team.getBed() != null && team.getSpawn() != null) string.append("&aeverything set");
                lore.add(string.toString());
            }
        }
        inv.setItem(8, InventoryHandler.createStack(Material.GREEN_DYE, "&aFinish", Arrays.asList("&6Quits Build Mode", "&7Saves Map", "&7and makes it playable"), "b(finish)"));
        inv.setItem(13, InventoryHandler.createStack(Material.BOOK, "&6Info", lore, true));
        inv.setItem(9, InventoryHandler.createStack(Material.BOOK, "&aRe&7name", Collections.singletonList("&7Rename the Map"), "b(rename)", false));
        inv.setItem(17, InventoryHandler.createStack(Material.RED_DYE, "&cDelete Map", Arrays.asList("&7Deletes the whole map", "&7this is &cpermanent&7 there's", "&cno backup"), "b(deleterequest)"));
        inv.setItem(0, InventoryHandler.createStack(Material.BARRIER, "&7Leave", Arrays.asList("&7Leaves the Build,", "&7but doesn't finish the Map"), "b(leave)"));
        inv.setItem(22, InventoryHandler.createStack(Material.RED_BED, "&6Teams", Arrays.asList("&7Set the Bed for each Team", "&7Create or delete Teams", "&7Set Team Spawns"), "b(teams)"));

        return inv;
    }
    public static Inventory TeamsInv(gameMap map) {
        Inventory inv = InventoryHandler.createInventory("&6Teams");

        inv.setItem(22, InventoryHandler.createStack(Material.SANDSTONE, "&6Main", Arrays.asList("&7Go back to the", "&7Main Build Inventory"), "b(main)"));
        TeamManager teams = map.getTeamManager();
        for(int i = 0; i < teams.getTeams().size(); i++) {
            Team team = teams.getTeam(i);
            boolean isSet = false;
            String info = "&cmissing";
            Material symbol = Material.RED_BED;
            if(team.getBed() !=null) {
                isSet = true;
                info = "&aalready set";
                symbol = Material.BARRIER;
            }
            if(team !=null) {
                inv.setItem(i + 27, InventoryHandler.createStack(symbol, "&7Team " + team.getName(), Arrays.asList("&7Set the Bed from", team.getName(), info), "z(" + team.getColor().name() + ")", isSet));
            }
        }
        for(int i = 0; i < teams.getTeams().size(); i++) {
            Team team = teams.getTeam(i);
            if(team !=null) {
                boolean isSet = false;
                String info = "&cmissing";
                Material symbol = Material.NETHER_STAR;
                if(team.getSpawn() !=null) {
                    symbol = Material.BARRIER;
                    isSet = true;
                    info = "&aalready set";
                }
                inv.setItem(i + 9, InventoryHandler.createStack(symbol, "&7Team " + team.getName() + " &7Spawn", Arrays.asList("&7Set the Spawn from", team.getName(), info), "s(" + team.getColor().name() + ")", isSet));
            }
        }
        inv.setItem(25, InventoryHandler.createStack(Material.BOOK, "&6Team info", Arrays.asList("&7Team size: " + teams.getTeamSize(), "&7Teams: " + teams.getTeams().size())));
        inv.setItem(15, InventoryHandler.createStack(Material.RED_DYE, "&cRemove &7a Team", "b(rteam)"));
        inv.setItem(17, InventoryHandler.createStack(Material.GREEN_DYE, "&aAdd &7a Team", "b(ateam)"));
        inv.setItem(33, InventoryHandler.createStack(Material.RED_DYE, "Teamsize &c-1", "b(-size)"));
        inv.setItem(35, InventoryHandler.createStack(Material.GREEN_DYE, "&7Teamsize &a+1", "b(+size)"));
        return inv;
    }

    public static Inventory Delete() {
        Inventory inv = InventoryHandler.createInventory("&lAre you sure", 9);
        inv.setItem(0, InventoryHandler.createStack(Material.GREEN_DYE, "&aCancel", "b(cancel)"));
        inv.setItem(8, InventoryHandler.createStack(Material.RED_DYE, "&cDelete", "b(delete)"));
        return inv;
    }
}
