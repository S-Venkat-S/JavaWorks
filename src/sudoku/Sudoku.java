//$Id$
package sudoku;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adventnet.iam.AccessControlMetaData.groupadmin;
import com.adventnet.iam.AccessControlMetaData.userdefault;

public class Sudoku {
	public static String data = "{2:2,7:4,10:6,13:2,18:3,19:9,15:7,27:5,30:3,31:5,32:7,39:4,40:3,45:8,47:1,50:8,55:7,56:3,60:6,68:3,70:2,74:8,75:5,80:1}";
	public static String subGridStr = "{\",0,1,2,9,10,11,18,19,20,\" : \"[0,1,2,9,10,11,18,19,20]\",\",3,4,5,12,13,14,21,22,23,\" : \"[3,4,5,12,13,14,21,22,23]\",\",6,7,8,15,16,17,24,25,26,\" : \"[6,7,8,15,16,17,24,25,26]\",\",27,28,29,36,37,38,45,46,47,\" : \"[27, 28, 29,36, 37, 38,45, 46, 47]\",\",30,31,32,39,40,41,48,49,50,\" : \"[30, 31, 32,39, 40, 41,48, 49, 50]\",\",33,34,35,42,43,44,51,52,53,\" : \"[33, 34, 35,42, 43, 44,51, 52, 53]\",\",54,55,56,63,64,65,72,73,74,\" : \"[54, 55, 56,63, 64, 65,72, 73, 74]\",\",57,58,59,66,67,68,75,76,77,\" : \"[57, 58, 59,66, 67, 68,75, 76, 77]\",\",60,61,62,69,70,71,78,79,80,\" : \"[60, 61, 62,69, 70, 71,78, 79, 80]\"}";
	public static int max = 0;
	
	
	public static JSONArray mapToArray (JSONObject grid) throws JSONException
	{
		JSONArray out = new JSONArray();
		for (int i=1;i<82;i++)
		{
			out.put(grid.getInt(i+""));
		}
		return out;
	}
	
	public static boolean test() {
		return false;
	}
	
	public static void main(String[] args) {
		try {
			//printGrid(initGrid(data));
			JSONArray grid = mapToArray(initGrid(data));
			//System.out.println(grid);
			//System.out.println(grid.length());
			//System.out.println(getPossibilities(7, grid));
			//System.out.println(getRows(8, grid));
			printGrid(grid,data);
			solve(grid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int isZero (JSONArray grid) throws JSONException {
		for (int i=0;i<grid.length();i++)
		{
			if (grid.getInt(i)==0)
			{
				return i;
			}
		}
		return -1;
	} 
	
	public static JSONArray remove (int n,JSONArray out) throws JSONException
	{
		JSONArray newOut = new JSONArray();
		for (int i=0;i<out.length();i++)
		{
			if (out.getInt(i) != n)
			{
				newOut.put(out.getInt(i));
			}
		}
		return newOut;
	}
	
	public static void solve (JSONArray grid) throws JSONException
	{
		if (isZero(grid) == -1)
		{
			System.out.println("SOLVED");
			return;
		}
		else
		{
			int curPos = isZero(grid);
			for (int i=1;i<10;i++)
			{
				JSONArray out = getPossibilities(curPos, grid);
				out = remove(grid.getInt(curPos),out);
				if (!isPresent(i, out))
				{
					grid.put(curPos, i);
					if (curPos > max)
					{
						printGrid(grid,data);
						max = curPos;
					}
					solve(grid);
				}
			}
			grid.put(curPos,0);
		}
		return;
	}
	
	public static boolean isPresent (int n,JSONArray grid) throws JSONException {
		for (int i=0;i<grid.length();i++)
		{
			if (grid.getInt(i) == n)
			{
				return true;
			}
		}
		return false;
	}
	
	public static JSONObject initGrid(String data) {
		JSONObject out = new JSONObject();
		try {
			JSONObject initData = new JSONObject(data);
			for (int i=0;i<82;i++)
			{
				String curKey = i+1+"";
				if (initData.optString(curKey) != "")
				{
					out.put(i+1+"", initData.getString(curKey));
				}
				else
				{
					out.put(i+1+"", "0");
				}
			}
			return out;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return out;
		} 
	}
	public static void printGrid(JSONArray data,String question) throws JSONException {
		String out="";
		JSONObject ques = new JSONObject(question);
		int count = 1;
		System.out.println("____________________");
		for (int i=0;i<81;i++)
		{
			if (ques.optString(i+1+"") != "")
			{
				out+="+"+data.getString(i)+"+";
			}
			else
			{
				out+="*"+data.getString(i)+"*";
			}
			if (count%9 == 0)
			{
				System.out.println(out);
				out="";
			}
			count++;
		}
		System.out.println("--------------------");
	}
	public static JSONArray getPossibilities(int n,JSONArray grid) throws JSONException {
		JSONArray out = new JSONArray();
		JSONArray row = getRows(n, grid);
		JSONArray column = getColums(n, grid);
		JSONArray subGrid =getSubGrid(n, grid);
		out = extend(out, row);
		out = extend(out, column);
		out = extend(out, subGrid);
		return out;
	}
	public static JSONArray getRows(int n,JSONArray grid) throws JSONException {
		JSONArray out = new JSONArray();
		int temp = n+1;
		while (true) {
			temp--;
			if (temp%9 == 0 || temp < 0)
			{
				break;
			}
			out.put(grid.getInt(temp-1));
		}
		temp = n+1;
		while (true) {
			if ((temp)%9 == 0 || temp < 0)
			{
				break;
			}
			out.put(grid.getInt(temp));
			temp++;
		}
		return out;
	}
	public static JSONArray getColums(int n,JSONArray grid) throws JSONException {
		JSONArray out = new JSONArray();
		int temp = n;
		while (true) {
			temp-=9;
			if (temp < 1)
			{
				break;
			}
			out.put(grid.getInt(temp));
		}
		temp = n;
		while (true) {
			temp+=9;
			if (temp >= 81)
			{
				break;
			}
			out.put(grid.getInt(temp));
		}
		return out;
	}
	public static JSONArray getSubGrid(int n,JSONArray grid) throws JSONException {
		JSONArray out = new JSONArray();
		JSONObject subGrid = new JSONObject(subGridStr);
		@SuppressWarnings("unchecked")
		Iterator<String> iter = subGrid.keys();
		String matchKey = "";
		String curIndx = ","+n+",";
		while (iter.hasNext()) {
			String curKey = iter.next();
			if (curKey.indexOf(curIndx) != -1)
			{
				matchKey = curKey;
				break;
			}
		}
		String indexListStr = subGrid.getString(matchKey);
		JSONArray indexList = new JSONArray(indexListStr);
		for (int i=0;i<indexList.length();i++) {
			int curPos = indexList.getInt(i);
			out.put(grid.getInt(curPos));
		}
		return out;
	}
	public static JSONArray extend(JSONArray out,JSONArray add) throws JSONException {
		for (int i=0;i<add.length();i++) {
			out.put(add.get(i));
		}
		return out;
	}
}
