package paulevs.creative;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.registry.ModID;
import net.modificationstation.stationapi.api.util.Null;

public class Creative {
    @Entrypoint.Instance
    public static final Creative INSTANCE = Null.get();

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();
}
