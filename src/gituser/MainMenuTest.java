package gituser;
import static org.junit.Assert.*;

import javax.swing.JFrame;

import org.junit.Test;

/**
 * Unit test for MainMenu
 * @author Kevin Jonathan
 */
public class MainMenuTest {

	/**
	 * Unit test for method generateQuery()
	 */
	@Test
	public void testGenerateQuery() {
		MainMenu test = new MainMenu(new JFrame());
		test.keyword.setText("Kevin Jonathan");
		test.SearchBy.setSelectedIndex(1);
		test.RepoFilter.setSelected(true);
		test.RepoValue.setText("10");
		test.FollowFilter.setSelected(true);
		test.FollowValue.setText("5");
		String query = test.generateQuery();
		String expected = "https://api.github.com/search/users?q=Kevin+Jonathan+in%3Afullname+repos%3A<10+followers%3A<5&type=Users";
		assertEquals(expected,query);
	}

	/**
	 * Unit test for method reset()
	 */
	@Test
	public void testReset() {
		MainMenu test = new MainMenu(new JFrame());
		test.keyword.setText("Kevin Jonathan");
		test.SearchBy.setSelectedIndex(1);
		test.RepoFilter.setSelected(true);
		test.RepoValue.setText("10");
		test.FollowFilter.setSelected(true);
		test.FollowValue.setText("5");
		assertNotSame("",test.keyword.getText());
		assertNotSame(0,test.SearchBy.getSelectedIndex());
		assertNotSame(false,test.RepoFilter.isSelected());
		assertNotSame("",test.RepoValue.getText());
		assertNotSame(false,test.FollowFilter.isSelected());
		assertNotSame("",test.FollowValue.getText());
		test.reset();
		assertEquals("",test.keyword.getText());
		assertEquals(0,test.SearchBy.getSelectedIndex());
		assertEquals(false,test.RepoFilter.isSelected());
		assertEquals("",test.RepoValue.getText());
		assertEquals(false,test.FollowFilter.isSelected());
		assertEquals("",test.FollowValue.getText());
	}

}
