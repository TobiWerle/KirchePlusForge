package UC.KirchePlus.Utils;


import UC.KirchePlus.main.main;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundsHandler {

    public static SoundEvent ENTITY_DRINK_MASTER;
    public static SoundEvent ENTITY_DRYOUT_MASTER;
    public static ArrayList<SoundEvent> dryoutSounds = new ArrayList<>();
    public static ArrayList<SoundEvent> drinkSounds = new ArrayList<>();

    public static void registerSounds() throws Exception {
        File fileDrink = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/Sounds/Drink/");
        File fileDryout = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/Sounds/Dryout/");

        if (!fileDrink.exists()) {
            fileDrink.mkdirs();
            InputStream drinkInputStream = SoundsHandler.class.getResourceAsStream("assets/kirche+/sounds/drink.ogg");
            Files.copy(drinkInputStream, fileDrink.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Drink copy...");
        }
        if (!fileDryout.exists()) {
            fileDryout.mkdirs();
            InputStream dryoutInputStream = SoundsHandler.class.getResourceAsStream("assets/kirche+/sounds/dryout.ogg");
            Files.copy(dryoutInputStream, fileDryout.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Dryout copy...");
        }

        copyDirectoryToJar(fileDrink.getPath(), SoundsHandler.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath(), "assets/kirche+/sounds");
        copyDirectoryToJar(fileDryout.getPath(), SoundsHandler.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI()
                .getPath(), "assets/kirche+/sounds");



        //write sounds.json an everystarup and add Sounds
        //Later for write sounds in sounds.json
        File[] drinkFiles = fileDrink.listFiles();
        if(drinkFiles != null){
            for(int i = 0; i<drinkFiles.length; i++){
                if (!drinkFiles[i].isDirectory()) {
                }
            }
        }


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

    protected static void copyDirectoryToJar(String directory, String jarFile, String jarFolder)
            throws Exception {

        FileSystem destinationJarFileSystem = null;
        Exception exception = null;
        try {

            final Path sourcePath = Paths.get(directory);
            final URI uri = URI.create("jar:file:/" + jarFile.replace(File.separatorChar, '/'));
            final Map<String, String> environment = new HashMap<String, String>() {{
                put("create", "true");
            }};
            destinationJarFileSystem = FileSystems.newFileSystem(uri, environment);
            final Path destinationPath = destinationJarFileSystem.getPath(jarFolder);

            copyFromDirToJar(sourcePath, destinationPath, destinationJarFileSystem);
        }
        catch (Exception e) {
            exception = e;
        }
        finally {
            try {
                if (destinationJarFileSystem != null) {
                    destinationJarFileSystem.close();
                }
            }
            catch (Exception e) {
                if (exception == null) {
                    exception = e;
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    private static void copyFromDirToJar(Path sourcePath, Path destinationPath, FileSystem destinationFileSystem)
            throws Exception {

        if (!Files.exists(destinationPath)) {
            Files.createDirectories(destinationPath);
        }

        if (Files.isRegularFile(sourcePath) && Files.isRegularFile(destinationPath)) {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        }

        Exception [] exception = new Exception [] {null};
        Files.list(sourcePath).forEachOrdered(sourceSubPath -> {
            try {
                Path fileOrFolder = sourceSubPath.getFileName();
                Path destinationSubPath = destinationFileSystem.getPath(destinationPath.toString(), fileOrFolder.toString());

                if (Files.isDirectory(sourceSubPath)) {
                    copyFromDirToJar(sourceSubPath, destinationSubPath, destinationFileSystem);
                }
                else {
                    Files.copy(sourceSubPath, destinationSubPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            catch (Exception e) {
                exception[0] = e;
            }
        });

        if (exception[0] != null) {
            throw exception[0];
        }
    }
}
