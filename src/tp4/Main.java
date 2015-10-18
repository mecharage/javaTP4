package tp4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

class WeatherInfo {
	
	public static class Info {
		
		@SerializedName("temp")
		public double temp;
		
		@SerializedName("temp_min")
		public double minTemp;
		
		@SerializedName("temp_max")
		public double maxTemp;
		
		@SerializedName("humidity")
		public int humidity;
	}
	
	@SerializedName("main")
	public Info info;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("cod")
	public int code;
	
	@SerializedName("message")
	public String message;
}

public class Main {
	
	public static void main(String[] args) {
		
		if(args.length < 1) {
			System.out.println("Please pass in a city name.");
			return;
		}
		
		String urlRoot = "http://api.openweathermap.org/data/2.5/weather";
		String city = args[0];
		String appId = "bd82977b86bf27fb59a04b61b657fb6f";
		
		String urlStr = String.format("%s?q=%s&appid=%s", urlRoot, city, appId);
		
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			try {
				InputStream istream = new BufferedInputStream(conn.getInputStream());
				
				Reader reader = new InputStreamReader(istream);
				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				WeatherInfo weatherInfo = gson.fromJson(reader, WeatherInfo.class);
				
				if (weatherInfo.code != 200) {
					System.err.println("Code : " + weatherInfo.code);
					System.err.println(weatherInfo.message);
					return;
				}
				
				System.out.println("Weather in " + weatherInfo.name);
				System.out.println("----------");
				System.out.println("T°     : " + weatherInfo.info.temp);
				System.out.println("T° min : " + weatherInfo.info.minTemp);
				System.out.println("T° max : " + weatherInfo.info.maxTemp);
				System.out.println("Humid. : " + weatherInfo.info.humidity);
				
			} finally {
				conn.disconnect();
			}
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL : " + urlStr);
			return;
		} catch (IOException ex) {
			System.err.println("I/O exception while contacting " + urlStr + " :");
			ex.printStackTrace(System.err);
			return;
		}
	}
	
}
