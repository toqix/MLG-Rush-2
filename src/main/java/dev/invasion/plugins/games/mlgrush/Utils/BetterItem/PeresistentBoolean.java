package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

public class PeresistentBoolean implements PersistentDataType<Integer, Boolean> {

    @Override
    public Class<Integer> getPrimitiveType() {
        return Integer.class;
    }

    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public Integer toPrimitive(Boolean aBoolean, PersistentDataAdapterContext persistentDataAdapterContext) {
        return aBoolean ? 1 : 0;
    }

    @Override
    public Boolean fromPrimitive(Integer integer, PersistentDataAdapterContext persistentDataAdapterContext) {
        return integer == 1;
    }
}
