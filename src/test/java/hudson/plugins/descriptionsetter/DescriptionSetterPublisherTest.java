package hudson.plugins.descriptionsetter;

import hudson.model.FreeStyleBuild;
import hudson.model.Result;
import hudson.model.FreeStyleProject;

import org.jvnet.hudson.test.HudsonTestCase;

public class DescriptionSetterPublisherTest extends HudsonTestCase {

	public void testSuccessDefaultDescription() throws Exception {
		assertEquals("one", getDescription("text one", Result.SUCCESS,
				"text (.*)", null, null, null));
	}

	public void testSuccessConfiguredDescription() throws Exception {
		assertEquals("description one", getDescription("text one",
				Result.SUCCESS, "text (.*)", null, "description \\1", null));
	}

        public void testSuccessConfiguredDescription2() throws Exception {
		assertEquals("description one two", getDescription("text one two",
				Result.SUCCESS, "text (\\w+) (\\w+)", null, "description \\1 \\2", null));
        }

	public void testFailureWithNoFailureRegex() throws Exception {
		assertEquals("one", getDescription("text one", Result.FAILURE,
				"text (.*)", null, null, null));
	}

	public void testFailureWithFailureRegexAndDefaultDescrption() throws Exception {
		assertEquals("text", getDescription("text one", Result.FAILURE,
				"text (.*)", "(.*) one", null, null));
	}

	public void testFailureWithFailureRegexAndConfiguredDescription() throws Exception {
		assertEquals("description text", getDescription("text one",
				Result.FAILURE, "text (.*)", "(.*) one", null,
				"description \\1"));
	}

	public void testSuccessWithFixedDescription() throws Exception {
		assertEquals("description success", getDescription("xxx",
				Result.SUCCESS, null, null, "description success",
				"description failure"));
	}

	public void testFailureWithFixedDescription() throws Exception {
		assertEquals("description failure", getDescription("xxx",
				Result.FAILURE, null, null, "description success",
				"description failure"));
	}

	public void testSuccessNoMatch() throws Exception {
		assertEquals(null, getDescription("xxx",
				Result.SUCCESS, "regex", null, "description success",
				null));
	}

	public void testURL() throws Exception {
		assertEquals("<a href=\"http://foo/bar\">http://foo/bar</a>", getDescription("url:http://foo/bar",
				Result.SUCCESS, "url:(.*)", null, null,
				null));
	}
	
	public void testNullMatch1() throws Exception {
		assertEquals("Match=(MatchOne) MatchTwo",
				getDescription("Prefix: MatchOne MatchTwo", Result.SUCCESS,
						"^Prefix: (\\S+)( .*)?$", null, 
						"Match=(\\1)\\2", null));
	}

	public void testNullMatch2() throws Exception {
		assertEquals("Match=(MatchOne)",
				getDescription("Prefix: MatchOne", Result.SUCCESS,
						"^Prefix: (\\S+)( .*)?$", null,
						"Match=(\\1)\\2", null));
	}

	public void testDescriptionInPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDescriptionInPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDescriptionInPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTheSameDescriptionInBuilderAndPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTheSameDescriptionInBuilderAndPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1", build.getDescription());
	}

	public void testTheSameDescriptionInBuilderAndPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testTheSameDescriptionInBuilderAndPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDifferentDescriptionInBuilderAndPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDifferentDescriptionInBuilderAndPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2", build.getDescription());
	}

	public void testDifferentDescriptionInBuilderAndPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDifferentDescriptionInBuilderAndPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1<br />test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddAlreadyExistedDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousTheSameDescriptionAndAddANewDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2<br />test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddAlreadyExistedDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test1", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInPublisherDefaultBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, false, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInPublisherAppendBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, true, false));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test3<br />test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInPublisherUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, false, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test2", build.getDescription());
	}

	public void testDoNotTouchPreviousDifferentDescriptionAndAddANewDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
		project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
		project.getPublishersList().add(
				new DescriptionSetterPublisher("", "", "test2", "", false, true, true));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		assertEquals("test1<br />test3<br />test2", build.getDescription());
	}

	private String getDescription(String text, Result result, String regexp,
		String regexpForFailed, String description,
		String descriptionForFailed, boolean appendMode, boolean unique) throws Exception {

		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new TestBuilder(text, result));
		project.getPublishersList().add(
				new DescriptionSetterPublisher(regexp, regexpForFailed,
						description, descriptionForFailed, false, appendMode, unique));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		return build.getDescription();
	}

	private String getDescription(String text, Result result, String regexp,
			String regexpForFailed, String description,
			String descriptionForFailed) throws Exception {
		return getDescription(text, result, regexp, regexpForFailed, description,
			descriptionForFailed, true);
	}

	private String getDescription(String text, Result result, String regexp,
			String regexpForFailed, String description,
			String descriptionForFailed, boolean appendMode) throws Exception {
		return getDescription(text, result, regexp, regexpForFailed, description,
			descriptionForFailed, true, false);

	}

}
