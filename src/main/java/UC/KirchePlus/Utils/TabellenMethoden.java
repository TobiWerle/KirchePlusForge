package UC.KirchePlus.Utils;

import UC.KirchePlus.Events.Displayname;
import UC.KirchePlus.main.main;
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
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class TabellenMethoden {
	
	private static Sheets sheetsService;
	private static String APPLICATION_NAME = "Hausverbot Kirche";
	private static String SPREADSHEET_ID = "1qBE8L2aL22BRdwfOOdsTd7rBRzwGjGFx2Bui0ZOaf0s";
	private static ArrayList<String> memberSheets = new ArrayList<>();
	
	
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
		
		String range = "Hausverbote!B19:G92";
		
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

		//Perma
		String range2 = "Hausverbote!B9:G17";

		ValueRange response2 = sheetsService.spreadsheets().values()
				.get(SPREADSHEET_ID, range2)
				.execute();
		List<List<Object>> values2 = response2.getValues();
		if(values2 == null || values2.isEmpty()) {
			System.out.println("No data found!");
		}else {
			for(List row : values2) {
				try {
					new HV_User(row.get(0).toString(), row.get(1).toString(), row.get(2).toString(), row.get(4).toString(), "Nie", "Permanent");
				} catch (Exception e) {}
			}
		}
	}

	//LoadÖffi


	public static void checkDonations() throws IOException {
		main.spender.clear();
		for (String members : TabellenMethoden.memberSheets) {
			String Spenden1 = members + "!C76:D91";
			String Spenden2 = members + "!H76:I91";
			List<List<Object>> list1 = TabellenMethoden.getList(Spenden1);
			List<List<Object>> list2 = TabellenMethoden.getList(Spenden2);
			try {
				for (List row : list1) {
					if (SpenderInfo.exists(row.get(0).toString())) {
						SpenderInfo.getByName(row.get(0).toString()).addAmount(Integer.valueOf(row.get(1).toString()).intValue());
						continue;
					}
					main.spender.add(new SpenderInfo(row.get(0).toString(), row.get(1).toString()));
				}
			} catch (Exception exception) {}
			try {
				for (List row : list2) {
					if (SpenderInfo.exists(row.get(0).toString())) {
						SpenderInfo.getByName(row.get(0).toString()).addAmount(Integer.valueOf(row.get(1).toString()).intValue());
						continue;
					}
					main.spender.add(new SpenderInfo(row.get(0).toString(), row.get(1).toString()));
				}
			} catch (Exception exception) {}
		}
		System.out.println("Done Tabellen");
	}

	private static List<List<Object>> getList(String range) throws IOException {
		ValueRange response2 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
		List<List<Object>> values = response2.getValues();
		return values;
	}

	public static void getAllMemberSheets() throws IOException {
		Spreadsheet sp = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();
		List<Sheet> sheets = sp.getSheets();
		for(Sheet sheet : sheets){
			String[] sortOut = {"Übersicht","Hausverbote","Vorlage Tabelle"};
			String name = sheet.getProperties().getTitle();
			if(!Arrays.stream(sortOut).anyMatch(name::contains) && !name.toLowerCase().contains("frei")){
				memberSheets.add(name);
			}
		}
		for(String s : memberSheets){
			//System.out.println(s);
		}
	}

	
    public static boolean isSameDay(String s) {
		if(s.equals("Nie")){
			return false;
		}
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
    
    public static boolean isDayOver(String s) {
		if(s.equals("Nie")){
			return false;
		}
        Calendar calendar = Calendar.getInstance();
        int currentday = calendar.get(Calendar.DAY_OF_MONTH);
        int currentmonth = calendar.get(Calendar.MONTH)+1;
        int currentyear = calendar.get(Calendar.YEAR);
        String[] date = s.replace(".", "-").split("-");

        int dayEnd = Integer.parseInt(date[0]);
        int monthEnd = Integer.parseInt(date[1]);
        
        if(date.length == 2) {
        	if(currentday > dayEnd){
        		if(currentmonth == monthEnd){
        			return true;
				}
			}
        }
        if(date.length == 3) {
        	int yearEnd = Integer.parseInt(date[2]);

        	if(currentyear == yearEnd){
				if(currentday > dayEnd){
					if(currentmonth == monthEnd){
						return true;
					}
				}
			}
		}
    	return false;
    }
    

}
