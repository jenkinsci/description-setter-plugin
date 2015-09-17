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

	public void testDescriptionInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDescriptionInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDescriptionInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDescriptionInBuilderAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTwoTheSameDescriptionsInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTwoTheSameDescriptionsInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1", build.getDescription());
	}

	public void testTwoTheSameDescriptionsInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTwoTheSameDescriptionsInBuilderAppendandUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTwoDifferentDescriptionsInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testTwoDifferentDescriptionsInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2", build.getDescription());
	}

	public void testTwoDifferentDescriptionsInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testTwoDifferentDescriptionsInBuilderAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1<br />test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInBuilderAppendAndAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2<br />test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInBuilderAppendAndAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInBuilderAppendAndAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInBuilderDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInBuilderAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test3<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInBuilderUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInBuilderAppendAndAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));

		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test3<br />test2", build.getDescription());
	}

	private String getDescription(String text, Result result, String regexp,
		String description, boolean appendMode, boolean unique) throws Exception {

		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new TestBuilder(text, result));
		project.getBuildersList().add(
			new DescriptionSetterBuilder(regexp, description, appendMode, unique));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		return build.getDescription();
	}

	private String getDescription(String text, Result result, String regexp,
		String description) throws Exception {

		return getDescription(text, result, regexp, description, true);
	}

	private String getDescription(String text, Result result, String regexp,
		String description, boolean appendMode) throws Exception {

		return getDescription(text, result, regexp, description, true, false);
	}

}
