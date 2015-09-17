package hudson.plugins.descriptionsetter;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * The DescriptionSetterBuilder allows the description of a build to be set as a
 * part of building. Setting the description early is useful if you have time
 * consuming builds.
 * 
 */
public class DescriptionSetterBuilder extends Builder {

	private final String regexp;
	private final String description;
	private final boolean appendMode;

	@DataBoundConstructor
	public DescriptionSetterBuilder(String regexp, String description, boolean appendMode) {
		this.regexp = regexp;
		this.description = Util.fixEmptyAndTrim(description);
		this.appendMode = appendMode;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException {

		return DescriptionSetterHelper.setDescription(build, listener, regexp,
				description, appendMode);
	}

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Builder> {

		public DescriptorImpl() {
			super(DescriptionSetterBuilder.class);
		}

		@Override
		public String getDisplayName() {
			return Messages.DescriptionSetter_DisplayName();
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		@Override
		public Builder newInstance(StaplerRequest req, JSONObject formData)
				throws FormException {
			return req.bindJSON(DescriptionSetterBuilder.class, formData);
		}
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	public String getRegexp() {
		return regexp;
	}

	public String getDescription() {
		return description;
	}

}
