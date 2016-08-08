//$Id$
package jsonDB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonDB {
	private JSONObject db = new JSONObject();
	
	private JSONArray currentTable = null;
	
	private String[] currentColumns = null;
	
	private ArrayList<String> checkColumnName = new ArrayList<String>();
	
	private ArrayList<String> currentCheck = new ArrayList<String>();
	
	private ArrayList<String> query = new ArrayList<String>();
	
	private ArrayList<String> chain = new ArrayList<String>();
	
	public void addTable(String tableName,JSONObject object) throws JSONException {
		JSONObject newObject = new JSONObject(object.toString());
		JSONArray outObject = new JSONArray();
		Iterator<String> iter = (Iterator<String>)newObject.keys();
		while (iter.hasNext()) {
			String curKey = iter.next();
			Object curVal = newObject.get(curKey);
			JSONObject value = null;
			if (curVal instanceof String) {
				value = new JSONObject((String)curVal);
			}
			else if (curVal instanceof JSONObject) {
				value = (JSONObject)curVal;
			}
			value.put(JsonDBConstants.KEY, curKey);
			outObject.put(value);
		}
		addTable(tableName, outObject);
	}
	
	private void flush() {
		currentTable = null;
		
		currentColumns = null;
		
		checkColumnName = new ArrayList<String>();
		
		currentCheck = new ArrayList<String>();
		
		query = new ArrayList<String>();
		
		chain = new ArrayList<String>();
	}
	
	public void addTable(String tableName,JSONArray object) throws JSONException {
		db.put(tableName, object);
	}
	
	public JsonDB select(String... strings) {
		currentColumns = strings;
		return this;
	}
	
	public JsonDB from (String tableName) throws JSONException {
		currentTable = db.getJSONArray(tableName);
		return this;
	}
	
	public JsonDB where(String columnName) {
		checkColumnName.add(columnName);
		return this;
	}
	
	public JsonDB is (String value)  {
		currentCheck.add(JsonDBConstants.IS);
		query.add(value);
		return this;
	}
	
	public JsonDB startsWith (String value)  {
		currentCheck.add(JsonDBConstants.STARTS);
		query.add(value);
		return this;
	}
	
	public JsonDB endsWith (String value)  {
		currentCheck.add(JsonDBConstants.ENDS);
		query.add(value);
		return this;
	}
	
	public JsonDB contains (String value)  {
		currentCheck.add(JsonDBConstants.CONTAINS);
		query.add(value);
		return this;
	}
	
	public JsonDB not (String value)  {
		currentCheck.add(JsonDBConstants.NOT);
		query.add(value);
		return this;
	}
	
	public JsonDB and() {
		chain.add(JsonDBConstants.AND);
		return this;
	}
	
	public JsonDB or() {
		chain.add(JsonDBConstants.OR);
		return this;
	}
	
	public JSONArray get() throws JSONException {
		JSONArray out = new JSONArray();
		for (int i=0;i<currentTable.length();i++) {
			JSONObject curObj = currentTable.getJSONObject(i);
			
			boolean isPositiveResult = search(curObj,0);
			if (isPositiveResult) {
				out.put(getByColumns(curObj));
			}
		}
		flush();
		return out;
	}
	
	private boolean search(JSONObject curObj,int curChainIndex) {
		boolean isPositiveResult = false;
		String columnName = checkColumnName.get(curChainIndex);
		String curQuery = query.get(curChainIndex);
		String actualValue = curObj.optString(columnName);
		String check = currentCheck.get(curChainIndex);
		if (check.equals(JsonDBConstants.IS) && curQuery.equals(actualValue)) {
			isPositiveResult = true;
		}
		else if (check.equals(JsonDBConstants.CONTAINS) && actualValue.contains(curQuery)) {
			isPositiveResult = true;
		}
		else if (check.equals(JsonDBConstants.STARTS) && actualValue.startsWith(curQuery)) {
			isPositiveResult = true;
		}
		else if (check.equals(JsonDBConstants.ENDS) && actualValue.endsWith(curQuery)) {
			isPositiveResult = true;
		}
		else if (check.equals(JsonDBConstants.NOT) && !curQuery.equals(curQuery)) {
			isPositiveResult = true;
		}
		if (isPositiveResult == true && curChainIndex < chain.size() && chain.get(curChainIndex).equals(JsonDBConstants.AND)) {
			isPositiveResult = search(curObj, curChainIndex+1);
		}
		if (isPositiveResult == false && curChainIndex < chain.size() && chain.get(curChainIndex).equals(JsonDBConstants.OR)) {
			isPositiveResult = search(curObj, curChainIndex+1);
		}
		return isPositiveResult;
	}
	
	private JSONObject getByColumns(JSONObject curObj) throws JSONException {
		JSONObject outObj = new JSONObject();
		if (currentColumns.length == 1 && currentColumns[0].equals("*")) {
			outObj = curObj;
		}
		else {
			for (String column:currentColumns) {
				outObj.put(column, curObj.opt(column));
			}
		}
		return outObj;
	}
	
	public String queryString(String query) {
		try {
			return (String) jsonFinder(db, query);
		} catch (Exception e) {
			return null;
		}
	}

	public Integer queryInt(String query) {
		try {
			String out = (String) jsonFinder(db, query);
			return Integer.parseInt(out);
		} catch (Exception e) {
			return null;
		}
	}

	public Object queryObject(String query) {
		try {
			Object out = jsonFinder(db, query);
			return out;
		} catch (Exception e) {
			return null;
		}
	}

	private Object jsonFinder(JSONObject respObject, String query)
			throws JSONException {
		try {
			try {
				respObject.put("MYRESPONSE", respObject);
				query = "MYRESPONSE." + query;
			} catch (JSONException e) {
				respObject.put("MYRESPONSE", respObject);
				query = "MYRESPONSE" + query;
			}
			String[] curParam = query.split("\\.");
			for (int i = 0; i < curParam.length; i++) {
				String positionPattern = "(\\[\\d+\\])";
				Pattern reg = Pattern.compile(positionPattern);
				Matcher match = reg.matcher(curParam[i]);
				if (i == curParam.length - 1) {
					if (!match.find()) {
						if (curParam[i].equals("?")) {
							return respObject.get((String) respObject.keys()
									.next());
						} else {
							return respObject.get(curParam[i]);
						}
					} else {
						int position = Integer.parseInt(match.group(1)
								.replace("[", "").replace("]", ""));
						String objName = curParam[i].split("\\[")[0];
						return respObject.getJSONArray(objName).getString(
								position);
					}
				} else if (match.find()) {
					int position = Integer.parseInt(match.group(1)
							.replace("[", "").replace("]", ""));
					String objName = curParam[i].split("\\[")[0];
					respObject = respObject.getJSONArray(objName)
							.getJSONObject(position);
				} else {
					if (curParam[i].equals("?")) {
						respObject = respObject
								.getJSONObject((String) respObject.keys()
										.next());
					} else {
						respObject = respObject.getJSONObject(curParam[i]);
					}
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
