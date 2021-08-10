package paulevs.creative;

import net.modificationstation.stationapi.api.common.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.common.registry.ModID;
import net.modificationstation.stationapi.api.common.util.Null;

public class Creative {
    @Entrypoint.Instance
    public static final Creative INSTANCE = Null.get();

    @Entrypoint.ModID
    public static final ModID MODID = Null.get();
}
