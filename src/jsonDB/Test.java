//$Id$
package jsonDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;

public class Test {
	
	public static void main(String[] args) throws JSONException, IOException {
		String currentDir = System.getProperty("user.dir");
		FileReader file = new FileReader(currentDir+File.separator+"src"+File.separator+"jsonDB"+File.separator+"data.json"); 
		BufferedReader reader = new BufferedReader(file); 
		String testData = "";
		String cache;
		while((cache = reader.readLine()) != null) { 
			testData+=cache; 
		} 
		JSONArray data = new JSONArray(testData);
		JsonDB db = new JsonDB();
		db.addTable("PERSONS", data);
		JSONArray out = db.select("ID","COUNTRY").from("PERSONS").where("FAV_COLOR").contains("Blue").or().where("COUNTRY").is("Philippines").get();
		for (int i=0;i<out.length();i++) {
			System.out.println(out.get(i));
		}
		System.out.println("DONE");
	}
	
}