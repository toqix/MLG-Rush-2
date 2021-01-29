package dev.invasion.plugins.games.mlgrush.Utils.BetterItem;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDTagType implements PersistentDataType<byte[], UUID> {

    //{@literal @Override}
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    //{@literal @Override}
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    //{@literal @Override}
    public byte[] toPrimitive(UUID complex, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(complex.getMostSignificantBits());
        bb.putLong(complex.getLeastSignificantBits());
        return bb.array();
    }

    //{@literal @Override}
    public UUID fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

}
