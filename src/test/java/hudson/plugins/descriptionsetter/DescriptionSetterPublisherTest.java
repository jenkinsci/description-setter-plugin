package hudson.plugins.descriptionsetter;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.Builder;

import java.io.IOException;
import java.io.Serializable;

import org.jvnet.hudson.test.HudsonTestCase;

import static org.junit.Assert.*;

public class DescriptionSetterPublisherTest extends HudsonTestCase {

	private static final class MyBuilder extends Builder {
		private final String text;
		private final Result result;

		public MyBuilder(String text, Result result) {
			super();
			this.text = text;
			this.result = result;
		}

		@Override
		public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
				BuildListener listener) throws InterruptedException,
				IOException {
			listener.getLogger().println(text);
			build.setResult(result);
			return true;
		}
	}

	public void test1() throws Exception {
		assertEquals("one", getDescription("text one", Result.SUCCESS,
				"text (.*)", null, null, null));
	}

	public void test2() throws Exception {
		assertEquals("description one", getDescription("text one",
				Result.SUCCESS, "text (.*)", null, "description \\1", null));
	}

	public void test3() throws Exception {

		assertEquals("one", getDescription("text one", Result.FAILURE,
				"text (.*)", null, null, null));
	}

	public void test4() throws Exception {

		assertEquals("text", getDescription("text one", Result.FAILURE,
				"text (.*)", "(.*) one", null, null));
	}

	public void test5() throws Exception {

		assertEquals("description one", getDescription("text one",
				Result.SUCCESS, "text (.*)", null, "description \\1", null));
	}

	public void test6() throws Exception {

		assertEquals("description text", getDescription("text one",
				Result.FAILURE, "text (.*)", "(.*) one", null,
				"description \\1"));
	}

	public void test7() throws Exception {

		assertEquals("description success", getDescription("xxx",
				Result.SUCCESS, null, null, "description success",
				"description failure"));
	}

	public void test8() throws Exception {

		assertEquals("description failure", getDescription("xxx",
				Result.FAILURE, null, null, "description success",
				"description failure"));
	}

	public void test9() throws Exception {

		assertEquals("description failure", getDescription("xxx",
				Result.FAILURE, "regex", null, "description success",
				"description failure"));
	}

	public void test10() throws Exception {

		assertEquals(null, getDescription("xxx",
				Result.SUCCESS, "regex", null, "description success",
				null));
	}

	public void test11() throws Exception {

		assertEquals(null, getDescription("regex",
				Result.SUCCESS, "xxx", null, "description success",
				null));
	}

	private String getDescription(String text, Result result, String regexp,
			String regexpForFailed, String description,
			String descriptionForFailed) throws Exception {
		FreeStyleProject project = createFreeStyleProject();
		project.getBuildersList().add(new MyBuilder(text, result));
		project.getPublishersList().add(
				new DescriptionSetterPublisher(regexp, regexpForFailed,
						description, descriptionForFailed));
		FreeStyleBuild build = project.scheduleBuild2(0).get();
		return build.getDescription();
	}

}
