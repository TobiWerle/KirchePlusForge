package UC.KirchePlus.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;

import UC.KirchePlus.Events.Displayname;
import UC.KirchePlus.main.main;


public class TabellenMethoden {
	
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Hausverbot Kirche";
	private static String SPREADSHEET_ID = "1qBE8L2aL22BRdwfOOdsTd7rBRzwGjGFx2Bui0ZOaf0s";
	
	
	
	public static Credential authorize()throws IOException, GeneralSecurityException{
		InputStream in = TabellenMethoden.class.getResourceAsStream("/credentials.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
				JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
		
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
		
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
				clientSecrets, scopes)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
				.setAccessType("offline")
				.build();
		Credential credential = new AuthorizationCodeInstalledApp(
				flow, new LocalServerReceiver())
				.authorize("user");
		return credential;
		
	}
	
	public static Sheets getSheetsService() throws IOException, GeneralSecurityException{
		Credential credential = authorize();
		
		return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				JacksonFactory.getDefaultInstance(), credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
	
	public static void init() {
		File file = new File(System.getenv("APPDATA") + "/.minecraft/tokens");
		if(!file.exists()) file.mkdirs();
		
		
		File credentialsFile = new File(System.getenv("APPDATA") + "/.minecraft/tokens/StoredCredential");
		InputStream credentials = TabellenMethoden.class.getResourceAsStream("/StoredCredential");
		if(!credentialsFile.exists())
			try {
				Files.copy(credentials, credentialsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e1) {}
		
		
		
		try {
			sheetsService = getSheetsService();
		} catch (IOException | GeneralSecurityException e) {}
	}
	
	public static void getHVList() throws IOException, GeneralSecurityException {
		Displayname.HVs.clear();
		
		String range = "Hausverbote!B8:G81";
		
		ValueRange response = sheetsService.spreadsheets().values()
				.get(SPREADSHEET_ID, range)
				.execute();
		List<List<Object>> values = response.getValues();
		if(values == null || values.isEmpty()) {
			System.out.println("No data found!");
		}else {
			for(List row : values) {
				try {
					new HV_User(row.get(0).toString(), row.get(1).toString(), row.get(2).toString(), row.get(4).toString(), row.get(5).toString(), row.get(3).toString());	
				} catch (Exception e) {}
			}
		}
	}
	
	public static void getBrotList() throws IOException, GeneralSecurityException {
		Displayname.BrotUser.clear();
		
		String range = "Brot!B6:E65";
		
		ValueRange response = sheetsService.spreadsheets().values()
				.get(SPREADSHEET_ID, range)
				.execute();
		List<List<Object>> values = response.getValues();
		if(values == null || values.isEmpty()) {
			System.out.println("No data found!");
		}else {
			for(List row : values) {
				try {
					new Brot_User(row.get(0).toString(), row.get(1).toString(), row.get(2).toString());
				} catch (Exception e) {}
			}
		}
	}
	
    public static boolean isSameDay(String s) {
        Calendar calendar = Calendar.getInstance();
        int currentday = calendar.get(Calendar.DAY_OF_MONTH);
        int currentmonth = calendar.get(Calendar.MONTH)+1;
        int currentyear = calendar.get(Calendar.YEAR);
        String[] date = s.replace(".", "-").split("-");
        
        
        if(date.length == 2) {
            if(Integer.parseInt(date[0]) == currentday && Integer.parseInt(date[1]) == currentmonth) {
            	return true;
            }
        }
        if(date.length == 3) {
            if(Integer.parseInt(date[0]) == currentday && Integer.parseInt(date[1]) == currentmonth && Integer.parseInt(date[2]) == currentyear) {
            	return true;
            }
        }
        
    	return false;
    }
    
    public static boolean isDayNotOver(String s) {
        Calendar calendar = Calendar.getInstance();
        int currentday = calendar.get(Calendar.DAY_OF_MONTH);
        int currentmonth = calendar.get(Calendar.MONTH)+1;
        int currentyear = calendar.get(Calendar.YEAR);
        String[] date = s.replace(".", "-").split("-");
        
        
        if(date.length == 2) {
            if(Integer.parseInt(date[0]) >= currentday && Integer.parseInt(date[1]) >= currentmonth) {
            	return true;
            }
        }
        if(date.length == 3) {
            if(Integer.parseInt(date[0]) >= currentday && Integer.parseInt(date[1]) >= currentmonth && Integer.parseInt(date[2]) == currentyear) {
            	return true;
            }
        }
        
    	return false;
    }
    

}
