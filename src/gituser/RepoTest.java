package gituser;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.json.simple.JSONObject;
import org.junit.Test;

/**
 * Unit test for Repo
 * @author Kevin Jonathan
 */
public class RepoTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testRefineDescription() {
		String description = "When you fetch a list of resources, the response includes a subset of the attributes for that resource. This is the \"summary\" representation of the resource. (Some attributes are computationally expensive for the API to provide. For performance reasons, the summary representation excludes those attributes. To obtain those attributes, fetch the \"detailed\" representation.)";
		String expected = "When you fetch a list of resources, the response includes a subset of the attributes for that resource. This  ...";
		JSONObject obj = new JSONObject();
		obj.put("name", "Repo");
		obj.put("html_url", "repo");
		obj.put("description", description);
		Repo repo = new Repo(obj);
		repo.setSize(new Dimension(450, 104));
		String shortened = repo.refineDescription(description);
		assertEquals(expected,shortened);
	}

}
