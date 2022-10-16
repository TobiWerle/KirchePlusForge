package UC.KirchePlus.Utils;

import UC.KirchePlus.Config.KircheConfig;
import UC.KirchePlus.Events.Displayname;
import UC.KirchePlus.main.main;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;


public class TabellenMethoden {
	
	public static Sheets sheetsService;
	public static String APPLICATION_NAME = "Hausverbot Kirche";
	public static String SPREADSHEET_ID = "1qBE8L2aL22BRdwfOOdsTd7rBRzwGjGFx2Bui0ZOaf0s";
	public static ArrayList<String> memberSheets = new ArrayList<>();

	
	public static Credential authorize()throws IOException, GeneralSecurityException{
		InputStream in = TabellenMethoden.class.getResourceAsStream("/credentials.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));
		
		List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

		if(!KircheConfig.ownGMail){
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
					GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(),
					clientSecrets, scopes)
					.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
					.setAccessType("offline")
					.build();
			Credential credential = new AuthorizationCodeInstalledApp(
					flow, new LocalServerReceiver())
					.authorize("user");
			return credential;
		}

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(),
				clientSecrets, scopes)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens/own")))
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
				GsonFactory.getDefaultInstance(), credential)
				.setApplicationName(APPLICATION_NAME)
				.build();
	}
	
	public static void init() {
		File file = new File(System.getenv("APPDATA") + "/.minecraft/tokens");
		if(!file.exists()) file.mkdirs();

		File credentialsFile = new File(System.getenv("APPDATA") + "/.minecraft/tokens/StoredCredential");
		InputStream credentials = TabellenMethoden.class.getResourceAsStream("/StoredCredential");
		if (!credentialsFile.exists())
			try {
				Files.copy(credentials, credentialsFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ignored) {
			}

		try {
			sheetsService = getSheetsService();
		} catch (IOException | GeneralSecurityException ignored) {}

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
				} catch (Exception ignored) {}
			}
		}
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
				} catch (Exception ignored) {}
			}
		}
	}


	public static void checkDonations() throws IOException {
		main.spender.clear();
		for (String members : TabellenMethoden.memberSheets) {
			String Spenden1 = members + "!C76:D91";
			String Spenden2 = members + "!H76:I91";
			List<List<Object>> list1 = TabellenMethoden.getList(Spenden1);
			List<List<Object>> list2 = TabellenMethoden.getList(Spenden2);

			try {
				if(list1 != null) {
					for (List row : list1) {
						if (SpenderInfo.exists(row.get(0).toString())) {
							SpenderInfo.getByName(row.get(0).toString()).addAmount(Integer.valueOf(row.get(1).toString()).intValue());
							continue;
						}
						main.spender.add(new SpenderInfo(row.get(0).toString(), row.get(1).toString()));
					}
				}
			} catch (Exception exception) {exception.printStackTrace();}
			try {
				if(list2 != null) {
					for (List row : list2) {
						if (SpenderInfo.exists(row.get(0).toString())) {
							SpenderInfo.getByName(row.get(0).toString()).addAmount(Integer.valueOf(row.get(1).toString()).intValue());
							continue;
						}
						main.spender.add(new SpenderInfo(row.get(0).toString(), row.get(1).toString()));
					}
				}
			} catch (Exception exception) {exception.printStackTrace();}
		}
	}

	public static void getDonations() throws IOException {
		SpenderUtils.publicDonations.clear();
		String range = "Spender!K5:M998";

		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
		List<List<Object>> values = response.getValues();
		if(values == null || values.isEmpty()) {
			System.out.println("No data found!");
		}else {
			for(List row : values) {
				try {
					SpenderUtils.publicDonations.add(new publicDonators(row.get(0).toString(), row.get(1).toString(), Integer.parseInt(row.get(2).toString().replace("$", "").replace(".", ""))));
				} catch (Exception e) {e.printStackTrace();}
			}
		}
	}

	public static void updateName(String currentName, String UUID) throws IOException {
		String range = "Spender!K5:L998";
		int count = 0;
		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
		List<List<Object>> values = response.getValues();

		for(List row : values) {
			if(row.get(1).equals(UUID)){
				if(!row.get(0).equals(currentName)){
					List<Object> newvalues = Arrays.asList(currentName, UUID);
					values.set(count, newvalues);
					ValueRange body = new ValueRange().setValues(values);
					UpdateValuesResponse result =
							TabellenMethoden.sheetsService.spreadsheets().values().update( TabellenMethoden.SPREADSHEET_ID, range, body).setValueInputOption("RAW").execute();
					return;
				}
			}
			count++;
		}
	}

	public static List<List<Object>> getList(String range) throws IOException {
		ValueRange response2 = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, range).execute();
		List<List<Object>> values = response2.getValues();
		return values;
	}

	public static void getAllMemberSheets() throws IOException {
		Spreadsheet sp = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();
		List<Sheet> sheets = sp.getSheets();
		for(Sheet sheet : sheets){
			String[] get = {"#0","#1","#2", "#3", "#4", "#5", "#6"};
			String name = sheet.getProperties().getTitle();
			if(Arrays.stream(get).anyMatch(name::contains) && !name.toLowerCase().contains("frei")){
				memberSheets.add(name);
			}
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


	public static void getActivitys() throws IOException {
		String range = "Übersicht!B5:I34";
		HashMap<Activity_User, Integer> donations = new HashMap<Activity_User, Integer>();
		HashMap<Activity_User, Integer> total = new HashMap<Activity_User, Integer>();
		HashMap<Activity_User, Integer> roleplay = new HashMap<Activity_User, Integer>();
		ValueRange response = sheetsService.spreadsheets().values()
				.get(SPREADSHEET_ID, range)
				.execute();
		List<List<Object>> values = response.getValues();
		if(values == null || values.isEmpty()) {
			System.out.println("No data found!");
		}else {


			for(List row : values) {
				try {
					if(!row.get(1).toString().toLowerCase().contains("frei")){
						Activity_User user = new Activity_User(row.get(1).toString().replace("[","").replace("]", "").replace(" ", "").replace("L",""), row.get(6).toString(), row.get(5).toString(), row.get(7).toString());
						donations.put(user, Integer.parseInt(user.getDonationActivity()));
						total.put(user, Integer.parseInt(user.getTotalActivity()));
						roleplay.put(user, Integer.parseInt(user.getRpActivity()));
					}
				} catch (Exception ignored) {}
			}
			main.totalActivity = total.entrySet().stream()
					.sorted(Comparator.comparingInt(e -> -e.getValue()))
					.collect(Collectors.toMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(a, b) -> { throw new AssertionError(); },
							LinkedHashMap::new
					));

			main.roleplayActivity = roleplay.entrySet().stream()
					.sorted(Comparator.comparingInt(e -> -e.getValue()))
					.collect(Collectors.toMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(a, b) -> { throw new AssertionError(); },
							LinkedHashMap::new
					));
			main.donationActivity = donations.entrySet().stream()
					.sorted(Comparator.comparingInt(e -> -e.getValue()))
					.collect(Collectors.toMap(
							Map.Entry::getKey,
							Map.Entry::getValue,
							(a, b) -> { throw new AssertionError(); },
							LinkedHashMap::new
					));
		}
	}





}