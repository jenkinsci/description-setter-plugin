package hudson.plugins.descriptionsetter;

import hudson.model.FreeStyleBuild;
import hudson.model.Result;
import hudson.model.FreeStyleProject;

import org.jvnet.hudson.test.HudsonTestCase;

public class DescriptionSetterBuilderTest extends HudsonTestCase {

	public void testSuccessDefaultDescription() throws Exception {
		assertEquals("one",
				getDescription("text one", Result.SUCCESS, "text (.*)", null));
	}

	public void testSuccessConfiguredDescription() throws Exception {
		assertEquals(
				"description one",
				getDescription("text one", Result.SUCCESS, "text (.*)",
						"description \\1"));
	}

	public void testSuccessConfiguredDescription2() throws Exception {
		assertEquals(
				"description one two",
				getDescription("text one two", Result.SUCCESS,
						"text (\\w+) (\\w+)", "description \\1 \\2"));
	}

	public void testFailureWithNoFailureRegex() throws Exception {
		assertEquals("one",
				getDescription("text one", Result.FAILURE, "text (.*)", null));
	}

	public void testSuccessWithFixedDescription() throws Exception {
		assertEquals(
				"description success",
				getDescription("xxx", Result.SUCCESS, null,
						"description success"));
	}

    public void testSuccessWithValidTokenMacro() throws Exception {
        FreeStyleProject fooProject = createFreeStyleProject("foo");
        fooProject.scheduleBuild2(0).get();

        assertEquals(
                "#1",
                getDescription("xxx", Result.SUCCESS, null,
                        "#${BUILD_NUMBER}"));
    }

    public void testSuccessWithInvalidTokenMacro() throws Exception {
        FreeStyleProject fooProject = createFreeStyleProject("foo");
        fooProject.scheduleBuild2(0).get();

        assertEquals(
                null,
                getDescription("xxx", Result.SUCCESS, null,
                        "${xxx}"));
    }

	public void testSuccessNoMatch() throws Exception {
		assertEquals(
				null,
				getDescription("xxx", Result.SUCCESS, "regex",
						"description success"));
	}

	public void testURL() throws Exception {
		assertEquals(
				"<a href=\"http://foo/bar\">http://foo/bar</a>",
				getDescription("url:http://foo/bar", Result.SUCCESS,
						"url:(.*)", null));
	}

	public void testNullMatch1() throws Exception {
		assertEquals(
				"Match=(MatchOne) MatchTwo",
				getDescription("Prefix: MatchOne MatchTwo", Result.SUCCESS,
						"^Prefix: (\\S+)( .*)?$", "Match=(\\1)\\2"));
	}

	public void testNullMatch2() throws Exception {
		assertEquals(
				"Match=(MatchOne)",
				getDescription("Prefix: MatchOne", Result.SUCCESS,
						"^Prefix: (\\S+)( .*)?$", "Match=(\\1)\\2"));
	}

	private String getDescription(String text, Result result, String regexp,
			String description) throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new TestBuilder(text, result));
		project.getBuildersList().add(
				new DescriptionSetterBuilder(regexp, description));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		return build.getDescription();
	}

}
