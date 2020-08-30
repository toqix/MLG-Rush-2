package dev.invasion.plugins.games.mlgrush.maps;

import com.google.common.collect.Iterables;
import dev.invasion.plugins.games.mlgrush.MLGRush;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerData;
import dev.invasion.plugins.games.mlgrush.PlayerData.PlayerDataManager;
import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class BoundingBoxActions {


    public static void replace(List<Material> toReplace, BoundingBox box) {
        long start = System.nanoTime();
        double x1 = box.getX1();
        double x2 = box.getX2();
        double y1 = box.getY1();
        double y2 = box.getY2();
        double z1 = box.getZ1();
        double z2 = box.getZ2();
        double vol = (Math.abs((x1 - x2)) + 1) * (Math.abs(y1 - y2) + 1) * (Math.abs(z1 - z2) + 1);


        //Bukkit.broadcastMessage("x diff is " + Math.abs((x1 - x2)) + " y diff is " + Math.abs(y1 - y2) + " z diff is " + Math.abs(z1 - z2));
        for (Player player : PlayerDataManager.getDebugPlayers()) {
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] Executing task &aCLEANUP&7, checking " + vol + " blocks."));
        }
        int repl = 0;
        double times = 0;
        double diff = (y2 - y1);
        for (double i = y1; i <= y2; i++) {
            double done = diff - i;
            double percent = 100 - (done / diff) * 100;
            percent = Math.round(percent);
            if (percent > 10) {
                DecimalFormat df = new DecimalFormat("###.#");
                for (Player player : PlayerDataManager.getDebugPlayers()) {
                    String p = df.format(percent);
                    if (percent > 100) {
                        p = "100";
                    }
                    MessageCreator.sendTitle(player, "&a" + p + "% done", "&7Cleaning map...", 20000, false);
                }
            }
            for (double f = x1; f <= x2; f++) {
                for (double g = z1; g <= z2; g++) {
                    Location blockLoc = new Location(MLGRush.getWorld(), f, i, g);
                    Block block = MLGRush.getWorld().getBlockAt(blockLoc);
                    times++;
                    if (toReplace.contains(block.getType())) {
                        block.setType(Material.AIR);
                        repl++;
                    }
                }

            }
        }
        long finish = System.nanoTime();
        long duration = (finish - start) / 1000000;
        for (Player player : PlayerDataManager.getDebugPlayers()) {
            if (times != vol) {
                player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &cERROR: &7Checked blocks &7(" + times + ") &cdo not align with calculated &7(" + vol + ") &cblocks!"));
            }
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &7Checked " + times + " blocks; Replaced " + repl + " blocks; Time: " + duration + "ms."));
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &aFinished task CLEANUP."));

            MessageCreator.sendTitle(player, "&aDone", "&7Cleaned map in " + duration + "ms", 40);
        }
    }

    public static void replace(Material toReplace, BoundingBox box) {
        long start = System.nanoTime();
        double x1 = box.getX1();
        double x2 = box.getX2();
        double y1 = box.getY1();
        double y2 = box.getY2();
        double z1 = box.getZ1();
        double z2 = box.getZ2();
        double vol = (Math.abs((x1 - x2)) + 1) * (Math.abs(y1 - y2) + 1) * (Math.abs(z1 - z2) + 1);


        //Bukkit.broadcastMessage("x diff is " + Math.abs((x1 - x2)) + " y diff is " + Math.abs(y1 - y2) + " z diff is " + Math.abs(z1 - z2));
        for (Player player : PlayerDataManager.getDebugPlayers()) {
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] Executing task &aCLEANUP&7, checking " + vol + " blocks."));
        }
        int repl = 0;
        double times = 0;
        double diff = (y2 - y1);
        for (double i = y1; i <= y2; i++) {
            double done = diff - i;
            double percent = 100 - (done / diff) * 100;
            percent = Math.round(percent);
            if (percent > 10) {
                DecimalFormat df = new DecimalFormat("###.#");
                for (Player player : PlayerDataManager.getDebugPlayers()) {
                    String p = df.format(percent);
                    if (percent > 100) {
                        p = "100";
                    }
                    MessageCreator.sendTitle(player, "&a" + p + "% done", "&7Cleaning map...", 20000, false);
                }
            }
            for (double f = x1; f <= x2; f++) {
                for (double g = z1; g <= z2; g++) {
                    Location blockLoc = new Location(MLGRush.getWorld(), f, i, g);
                    Block block = MLGRush.getWorld().getBlockAt(blockLoc);
                    times++;
                    if (toReplace == block.getType()) {
                        block.setType(Material.AIR);
                        repl++;
                    }
                }

            }
        }
        long finish = System.nanoTime();
        long duration = (finish - start) / 1000000;
        for (Player player : PlayerDataManager.getDebugPlayers()) {
            if (times != vol) {
                player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &cERROR: &7Checked blocks &7(" + times + ") &cdo not align with calculated &7(" + vol + ") &cblocks!"));
            }
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &7Checked " + times + " blocks; Replaced " + repl + " blocks; Time: " + duration + "ms."));
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &aFinished task CLEANUP."));

            MessageCreator.sendTitle(player, "&aDone", "&7Cleaned map in " + duration + "ms", 40);
        }
    }

    public static void deleteBox(BoundingBox box) {
        long start = System.nanoTime();
        double x1 = box.getX1();
        double x2 = box.getX2();
        double y1 = box.getY1();
        double y2 = box.getY2();
        double z1 = box.getZ1();
        double z2 = box.getZ2();
        double vol = (Math.abs((x1 - x2)) + 1) * (Math.abs(y1 - y2) + 1) * (Math.abs(z1 - z2) + 1);
        //Bukkit.broadcastMessage("x diff is " + Math.abs((x1 - x2)) + " y diff is " + Math.abs(y1 - y2) + " z diff is " + Math.abs(z1 - z2));
        for (Player player : PlayerDataManager.getDebugPlayers()) {
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] Executing task &aDELETE&7, deleting " + vol + " blocks."));
        }
        int repl = 0;
        double times = 0;
        double diff = (y2 - y1);
        for (double i = y1; i <= y2; i++) {
            double done = diff - i;
            double percent = 100 - (done / diff) * 100;
            percent = Math.round(percent);
            if (percent > 10) {
                DecimalFormat df = new DecimalFormat("###.#");
                for (Player players : PlayerDataManager.getDebugPlayers()) {
                    MessageCreator.sendTitle(players, "&a" + df.format(percent) + "% done", "&cDeleting map...", 20000, false);
                }
            }
            for (double f = x1; f <= x2; f++) {
                for (double g = z1; g <= z2; g++) {
                    Location blockLoc = new Location(MLGRush.getWorld(), f, i, g);
                    MLGRush.getWorld().getBlockAt(blockLoc).setType(Material.AIR);
                    times++;

                }

            }
        }
        long finish = System.nanoTime();
        long duration = (finish - start) / 1000000;
        for (Player player : PlayerDataManager.getDebugPlayers()) {
            if (times != vol) {
                player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &cERROR: &7Checked blocks &7(" + times + ") &cdo not align with calculated &7(" + vol + ") &cblocks!"));
            }
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &cDeleted &7" + times + " blocks; Time: " + duration + "ms."));
            player.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &aFinished task DELETE."));
            MessageCreator.sendTitle(player, "&aDone", "&7Deleted map in " + duration + "ms", 40);
        }
    }

    public static int checkEmpty(BoundingBox box) {
        long start = System.nanoTime();
        double x1 = box.getX1();
        double x2 = box.getX2();
        double y1 = box.getY1();
        double y2 = box.getY2();
        double z1 = box.getZ1();
        double z2 = box.getZ2();
        double vol = (Math.abs((x1 - x2)) + 1) * (Math.abs(y1 - y2) + 1) * (Math.abs(z1 - z2) + 1);
        //Bukkit.broadcastMessage("x diff is " + Math.abs((x1 - x2)) + " y diff is " + Math.abs(y1 - y2) + " z diff is " + Math.abs(z1 - z2));
        Player player;
        Location previous;
        if (Bukkit.getOnlinePlayers().size() == 0) {
            MLGRush.getInstance().reload();
            return 0;
        } else {
            player = Iterables.get(Bukkit.getOnlinePlayers(), 0);
            previous = player.getLocation();
            box.getMiddle().teleport(player);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.setGameMode(GameMode.CREATIVE);
        }
        for (Player player1 : PlayerDataManager.getDebugPlayers()) {
            player1.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] Executing task &aCHECK_EMPTY&7, checking " + vol + " blocks."));
        }
        double times = 0;
        int notAir = 0;
        double diff = (y2 - y1);
        for (double i = y1; i <= y2; i++) {
            double done = diff - i;
            double percent = 100 - (done / diff) * 100;
            if (percent > 10) {
                DecimalFormat df = new DecimalFormat("###.#");
                for (Player players : PlayerDataManager.getDebugPlayers()) {
                    MessageCreator.sendTitle(players, "&a" + df.format(percent), "&7Checking block...", 20000);
                }
            }
            for (double f = x1; f <= x2; f++) {
                for (double g = z1; g <= z2; g++) {
                    Location blockLoc = new Location(MLGRush.getWorld(), f, i, g);
                    Block block = MLGRush.getWorld().getBlockAt(blockLoc);
                    //Bukkit.broadcastMessage("x: " + i + " y: " + f + " z: " + g);
                    times++;
                    if (block.getType() != Material.AIR) {
                        notAir++;
                    }
                }

            }
        }
        long finish = System.nanoTime();
        long duration = (finish - start) / 1000000;
        for (Player player1 : PlayerDataManager.getDebugPlayers()) {
            if (times != vol) {
                player1.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &cERROR: &7Checked blocks &7(" + times + ") &cdo not align with calculated &7(" + vol + ") &cblocks!"));
            }
            player1.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &7Checked " + times + " blocks; Found " + notAir + " not air blocks; Time: " + duration + "ms."));
            player1.sendMessage(MessageCreator.t("&7[" + MLGRush.getGameName() + "&7] &aFinished task CHECK_EMPTY."));
        }
        player.teleport(previous);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setFlying(false);
        return notAir;
    }
}
