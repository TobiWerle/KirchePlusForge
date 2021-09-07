package UC.KirchePlus.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MarryFile {
	
	public enum types{
		MM,
		FF,
		MF,
		none;
	}
	
	
	public static void load() {
		try {
			File file = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/Vorlagen");
			if(!file.exists()) file.mkdirs();
			
			File MM = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/Vorlagen/MM.txt");
			InputStream MM2 = getOriginalFile(types.MM);
			if(!MM.exists()) Files.copy(MM2, MM.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			File MF = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/Vorlagen/MF.txt");
			InputStream MF2 = getOriginalFile(types.MF);
			if(!MF.exists()) Files.copy(MF2, MF.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			File FF = new File(System.getenv("APPDATA") + "/.minecraft/Kirche+/Vorlagen/FF.txt");
			InputStream FF2 = getOriginalFile(types.FF);
			if(!FF.exists()) Files.copy(FF2, FF.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
		} catch (Exception e) {}
	}

	
	
	private static InputStream getOriginalFile(types Type) {
		if(Type == types.MM)
            return MarryFile.class.getResourceAsStream("/Mann-Mann.txt");
		 if(Type == types.MF)
             return MarryFile.class.getResourceAsStream("/Mann-Frau.txt");
 	     if(Type == types.FF)
             return MarryFile.class.getResourceAsStream("/Frau-Frau.txt");
 	     
 	     
		return null;
	}
	
	
	private static InputStream getFile(types Type) {
		load();
		String Path = System.getenv("APPDATA") + "/.minecraft/Kirche+/Vorlagen/";
		try {
			if(Type == types.MM) {
				InputStream file = new FileInputStream(Path + "MM.txt");
				return file;
			}
			if(Type == types.MF) {
				InputStream file = new FileInputStream(Path + "MF.txt");
				return file;
			}
			if(Type == types.FF) {
				InputStream file = new FileInputStream(Path + "FF.txt");
				return file;
			}
		} catch (Exception e) {}
   	    return null;
	}
	
	
	public static void createMarryRP(types Type, String Name1, String Name2) {
		createCopyFromFile(Type, Name1, Name2, false);
	}
	
	
	private static void createCopyFromFile(types Type, String Name1, String Name2, boolean oneDrive) {
		FileOutputStream outstream = null;
 
    	try{
    		String onedrive = "";
    		if(oneDrive == true) onedrive = "/OneDrive";
    	    File outfile = new File(System.getProperty("user.home") + onedrive +"/Desktop/"+ "MarryRP "+Type.name()+" "+Name1+"-"+Name2+".txt");
    	    InputStream instream = getFile(Type);    	    
    	    outstream = new FileOutputStream(outfile);
 
    	    byte[] buffer = new byte[1024];
 
    	    int length;

    	    while ((length = instream.read(buffer)) > 0){
    	    	outstream.write(buffer, 0, length);
    	    }
    	    
    	    String content = new String(Files.readAllBytes(outfile.toPath()), StandardCharsets.UTF_8);
    	    if(Type == types.MM) content = content.replaceAll("<Mann1>", Name1).replaceAll("<Mann2>", Name2);
    	    if(Type == types.FF) content = content.replaceAll("<Frau1>", Name1).replaceAll("<Frau2>", Name2);
    	    if(Type == types.MF) content = content.replaceAll("<Mann>", Name1).replaceAll("<Frau>", Name2);
    	    
    	    Files.write(outfile.toPath(), content.getBytes(StandardCharsets.UTF_8));
    	    
    	    instream.close();
    	    outstream.close();
    	    
    	}catch(IOException ioe){
    		if(oneDrive == false){
    			createCopyFromFile(Type, Name1, Name2, true);
    			return;
			}
    		ioe.printStackTrace();
    	 }
    }
	
	

}
