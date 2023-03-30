package UC.KirchePlus.Utils;

import java.io.IOException;

public class RegistryHandler {

    public static void initRegistries() {
        try {
            SoundsHandler.registerSounds();
        } catch (IOException e) {
            System.out.println("Fehler beim kopieren der Sounds");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
