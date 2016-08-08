//$Id$
package KnightsTour;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class KnightTour {
	
	public static int count = 0;
	public HashMap<String, String> testCaseReRun;
	
	public void temp (Throwable e){
		
	}
	
	public static void test() throws java.io.IOException, JSONException {
		System.out.println(System.currentTimeMillis());
	}
	
	public static JSONArray add (JSONArray arr,int index,Object inp) throws JSONException {
		JSONArray out = new JSONArray();
		if (index == 0)
		{
			out.put(inp);
		}
		for (int i=0;i<arr.length();i++)
		{
			out.put(arr.get(i));
			if (i == index - 1)
			{
				out.put(inp);
			}
		}
		return out;
	}
	
	public static void main (String[] args) {
		try {
			JSONArray start = new JSONArray("['A6']");
			solve(start);
			//test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean isPresent (String n,JSONArray grid) throws JSONException {
		for (int i=0;i<grid.length();i++)
		{
			if (grid.getString(i).equals(n))
			{
				return true;
			}
		}
		return false;
	}
	
	//Main method to do backtracking.
	public static JSONArray solve(JSONArray out) throws JSONException {
		if (out.length() == 64)
		{
			System.out.println(out);
			System.out.println("Solved");
			return out;
		}
		else
		{
			String curPos = out.getString(out.length()-1);
			JSONArray possibles = getPossibles(curPos);
			for (int i=0;i<possibles.length();i++)
			{
				String nextMove = possibles.getString(i);
				if (!isPresent(nextMove, out))
				{
					out.put(nextMove);
					if (out.length() >= count)
					{
						count = out.length();
						System.out.print(count);
						System.out.println(out);
					}
					out = solve(out);
				}
			}
			out = remove(out,out.length()-1);
		}
		return out;
	}
	
	//Remove a Object form JSONArray by its index.
	public static JSONArray remove(JSONArray arr,int index) throws JSONException {
		JSONArray out = new JSONArray();
		for (int i=0;i<arr.length();i++)
		{
			if (i!=index)
			{
				out.put(arr.get(i));
			}
		}
		return out;
	}
 	
	public static JSONArray getPossibles (String from) throws JSONException {
		JSONArray out = new JSONArray();
		Character letter = from.charAt(0);
		JSONArray possibleLetters = new JSONArray("[1,2,1,2,-1,-2,-1,-2]");
		JSONArray possibleNumbers = new JSONArray("[2,1,-2,-1,2,1,-2,-1]");
		for (int i=0;i<8;i++)
		{
			int letterNumber = letter.hashCode();
			Integer number = Integer.parseInt(from.charAt(1)+"");
			letterNumber+=possibleLetters.getInt(i);
			number+=possibleNumbers.getInt(i);
			if (letterNumber>64 && letterNumber<73 && number>0 && number<9)
			{
				char[] curLetter = Character.toChars(letterNumber);
				String curMove = curLetter[0]+""+number;
				out.put(curMove);
			}
		}
		return out;
		
	}
	
}