//$Id$
package permutations;

import java.util.ArrayList;
import java.util.Date;

class Combi {
	ArrayList<String> list = new ArrayList<String>();
	int[] a = new int[4];
	int count;
	int outCount = 0;
	long stTime = 0;
	long spTime = 0;
	
	public void test(String args,Integer numberOfComb){
		stTime = new Date().getTime();
		for (String j : args.split(",")) {
			list.add(j);
		}
		count = numberOfComb == null ? args.length() : numberOfComb;
		for (int j=0;j<list.size();j++)
		{
			count--;
			for (String i : list) {
				ArrayList<String> tempArray = getNew(list);
				tempArray.remove(i);
				findComb(i, getNew(tempArray));
			}
		}
		spTime = new Date().getTime();
		System.out.println("Total Count : "+outCount);
		System.out.print("Total Time Taken : ");
		System.out.print(spTime-stTime);
		System.out.println("ms");
	}
	
	public void findComb(String current,ArrayList<String> tempArray) {
		if (tempArray.size() == count) {
			System.out.println(current);
			outCount++;
		}
		else {
			for (String i : tempArray) {
				ArrayList<String> tempList = getNew(tempArray);
				tempList.remove(i);
				findComb(current+i, getNew(tempList));
			}
		}
	}
	
	public ArrayList<String> getNew(ArrayList<String> inp) {
		ArrayList<String> out = new ArrayList<String>();
		for (String i : inp) {
			out.add(i);
		}
		return out;
	}
	
}

public class premutations {
	public static void main(String[] args) {
		Combi a = new Combi();
		a.test("A,B,C,D",null);
	}
}

