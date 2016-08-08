//$Id$
package sudoku;
import sudoku.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Sudoku.class})

public class SudokuJunit {
	
	@Test
	public void firstTest() throws JSONException {
		JSONArray list = new JSONArray("[1,2,3,4,5]");
		int pos = 3;
		int neg = 7;
		assertEquals(true,Sudoku.isPresent(pos, list));
		assertEquals(false,Sudoku.isPresent(neg, list));
	}
}
