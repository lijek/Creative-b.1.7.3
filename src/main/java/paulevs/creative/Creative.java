package paulevs.creative;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

public class Creative {

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();

    public static final Identifier infoPacket = Identifier.of(MODID, "playerInfo");
    public static final Identifier toggleFlyPacket = Identifier.of(MODID, "toggleFly");
    public static final Identifier inventoryClickPacket = Identifier.of(MODID, "creativeInventoryClick");
    public static final Identifier duplicateItemStackPacket = Identifier.of(MODID, "duplicateItemStack");
}
