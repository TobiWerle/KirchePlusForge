package UC.KirchePlus.Utils;


import UC.KirchePlus.main.main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class SoundsHandler {

    public static SoundEvent ENTITY_DRINK_MASTER;
    public static SoundEvent ENTITY_DRYOUT_MASTER;

    public static void registerSounds() {

        ENTITY_DRINK_MASTER = registerSound("entity.drink.master");
        ENTITY_DRYOUT_MASTER = registerSound("entity.dryout.master");
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation location = new ResourceLocation(main.MODID, name);
        SoundEvent event = new SoundEvent(location);
        event.setRegistryName(name);
        ForgeRegistries.SOUND_EVENTS.register(event);
        return event;
    }
}
