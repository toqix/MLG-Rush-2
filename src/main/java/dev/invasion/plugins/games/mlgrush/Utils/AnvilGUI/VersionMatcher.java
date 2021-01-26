package dev.invasion.plugins.games.mlgrush.Utils.AnvilGUI;

import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.List;

/**
 * Matches the server's NMS version to its {@link VersionWrapper}
 *
 * @author Wesley Smith
 * @since 1.2.1
 */
class VersionMatcher {

    /**
     * The server's version
     */
    private final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
    /**
     * All available {@link VersionWrapper}s
     */
    private final List<Class<? extends VersionWrapper>> versions = Collections.singletonList(
            Wrapper1_16_R2.class
    );

    /**
     * Matches the server version to it's {@link VersionWrapper}
     *
     * @return The {@link VersionWrapper} for this server
     * @throws RuntimeException If AnvilGUI doesn't support this server version
     */
    VersionWrapper match() {
        try {
            return versions.stream()
                    .filter(version -> version.getSimpleName().substring(7).equals(serverVersion))
                    .findFirst().orElseThrow(() -> new RuntimeException("Your server version isn't supported in AnvilGUI!"))
                    .newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }

}
