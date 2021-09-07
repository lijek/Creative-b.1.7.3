package paulevs.creative.api.event;

import lombok.RequiredArgsConstructor;
import net.mine_diver.unsafeevents.Event;
import paulevs.creative.api.CreativeTab;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class TabRegisterEvent extends Event {

    public final Consumer<CreativeTab> register;

    public final void register(CreativeTab tab) {
        register.accept(tab);
    }

    @Override
    protected int getEventID() {
        return ID;
    }

    public static final int ID = NEXT_ID.incrementAndGet();
}
