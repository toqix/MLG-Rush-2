package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;


import dev.invasion.plugins.games.mlgrush.Utils.MessageCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BetterItemDescription {

    private List<ClickType> react_to;
    private String on_left;
    private String on_right;
    private List<String> description;

    public BetterItemDescription(String reaction_left, String reaction_right, List<String> desc) {
        on_left = reaction_left;
        on_right = reaction_right;
        description = desc;
        react_to = Arrays.asList(ClickType.LEFT, ClickType.RIGHT);
    }
    public BetterItemDescription(String reaction, List<String> desc) {
        on_left = reaction;
        description = desc;
        react_to = Arrays.asList(ClickType.LEFT);
    }
    public BetterItemDescription(String reaction, ClickType reaction_type, List<String> desc) {
        if(reaction_type == ClickType.LEFT) {
            on_left = reaction;
        } else {
            on_right = reaction;
        }
        description = desc;
        react_to = Arrays.asList(reaction_type);
    }
    public BetterItemDescription(List<String> desc) {
        react_to = Arrays.asList();
        description = desc;
        react_to = Arrays.asList();
    }

    public List<String> toLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        if(description.size() != 0) {
            if(!description.get(0).equals("")) {
                lore.add("");
                lore.add("&f   &b&n&lDescription:");
                for (String description_line : description) {
                    lore.add("&f   &7" + description_line);
                }
                lore.add("&f ");
                lore.add("");
            }
        }
        if(react_to.size() != 0) {
            if (react_to.contains(ClickType.LEFT)) {
                lore.add("&a&l+ &7" + on_left);
            }
            if (react_to.contains(ClickType.RIGHT)){
                lore.add("&c&l- &7" + on_right);
            }
        }
        lore.add("");
        ArrayList<String> colorful_lore = new ArrayList<>();
        for (String item : lore) {
            colorful_lore.add(MessageCreator.t("&7" + item));
        }
        return colorful_lore;
    }

    public enum ClickType {
        LEFT,
        RIGHT
    }
}
