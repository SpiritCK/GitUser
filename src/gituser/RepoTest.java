package gituser;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit test for Repo
 * @author Kevin Jonathan
 */
public class RepoTest {

	@Test
	public void testRefineDescription() {
		String description = "When you fetch a list of resources, the response includes a subset of the attributes for that resource. This is the \"summary\" representation of the resource. (Some attributes are computationally expensive for the API to provide. For performance reasons, the summary representation excludes those attributes. To obtain those attributes, fetch the \"detailed\" representation.)";
		String expected = "When you fetch a list of resources, the response includes a subset of the attributes for that resource. This  ...";
		String shortened = Repo.refineDescription(description);
		assertEquals(expected,shortened);
	}

}
