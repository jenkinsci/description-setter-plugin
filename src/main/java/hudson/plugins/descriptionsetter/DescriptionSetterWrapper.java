package hudson.plugins.descriptionsetter;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.matrix.MatrixAggregatable;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildWrapper;
import hudson.tasks.BuildWrapperDescriptor;
import hudson.tasks.Builder;
import net.sf.json.JSONObject;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * The DescriptionSetterBuilder allows the description of a build to be set as a
 * part of building. Setting the description early is useful if you have time
 * consuming builds.
 * 
 */
public class DescriptionSetterWrapper extends BuildWrapper {

	private final String regexp;
	private final String description;
	private final boolean appendMode;

	@DataBoundConstructor
	public DescriptionSetterWrapper(String regexp, String description, boolean appendMode) {
		this.regexp = regexp;
		this.description = Util.fixEmptyAndTrim(description);
		this.appendMode = appendMode;
	}

	@Override
    public Environment setUp(AbstractBuild build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
		setDescription(build, listener);
        return new Environment() {
            @Override
            public boolean tearDown(AbstractBuild build, BuildListener listener) throws IOException, InterruptedException {
            	if (build.getDescription() == null) {
            		setDescription(build, listener);
				}
                return true;
            }
        };
    }

	private void setDescription(AbstractBuild build, BuildListener listener) throws InterruptedException {
		DescriptionSetterHelper.setDescription(build, listener, regexp,
				description, appendMode);
	}

	@Extension
	public static final class DescriptorImpl extends BuildWrapperDescriptor {

		@Override
		public String getDisplayName() {
			return Messages.DescriptionSetter_DisplayName();
		}

		@Override
        public boolean isApplicable(AbstractProject<?, ?> item) {
            return true;
        }
	}

	public String getRegexp() {
		return regexp;
	}

	public String getDescription() {
		return description;
	}



}
