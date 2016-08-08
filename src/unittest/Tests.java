//$Id$
package unittest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import sudoku.Sudoku;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Sudoku.class})

public class Tests {
	@Test
	public void firstTest() throws JSONException {
		JSONArray list = new JSONArray("[1,2,3,4,5]");
		int pos = 3;
		int neg = 7;
		PowerMockito.mock(Sudoku.class);
		Sudoku sudoku = mock(Sudoku.class);
		when(Sudoku.isPresent(pos, list)).thenReturn(true);
	}
}
