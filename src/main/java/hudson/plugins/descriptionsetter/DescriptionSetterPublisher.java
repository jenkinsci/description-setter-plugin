package hudson.plugins.descriptionsetter;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.matrix.MatrixAggregatable;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixRun;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.IOException;
import java.io.ObjectStreamException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * The DescriptionSetterPublisher allows the description of a build to be set as
 * a post-build action, after the build has completed.
 * 
 */
public class DescriptionSetterPublisher extends Recorder implements
		MatrixAggregatable {

	private final String regexp;
	private final String regexpForFailed;
	private final String description;

	private final String descriptionForFailed;
	private final boolean setForMatrix;
	private final boolean appendMode;

	@Deprecated
	private transient boolean setForFailed = false;

	@Deprecated
	private transient boolean explicitNotRegexp = false;

	@DataBoundConstructor
	public DescriptionSetterPublisher(String regexp, String regexpForFailed,
			String description, String descriptionForFailed,
			boolean setForMatrix, boolean appendMode) {
		this.regexp = regexp;
		this.regexpForFailed = regexpForFailed;
		this.description = Util.fixEmptyAndTrim(description);
		this.descriptionForFailed = Util.fixEmptyAndTrim(descriptionForFailed);
		this.setForMatrix = setForMatrix;
		this.appendMode = appendMode;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException {

		Result result = build.getResult();
		boolean useUnstable = (regexpForFailed != null || descriptionForFailed != null)
				&& result != null
				&& result.isWorseThan(Result.UNSTABLE);
		return DescriptionSetterHelper.setDescription(build, listener,
				useUnstable ? regexpForFailed : regexp,
				useUnstable ? descriptionForFailed : description,
				appendMode);
	}

	private Object readResolve() throws ObjectStreamException {
		if (explicitNotRegexp) {
			return new DescriptionSetterPublisher(null, null, regexp,
					setForFailed ? regexpForFailed : null, false, false);
		} else {
			return this;
		}
	}

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Publisher> {

		public DescriptorImpl() {
			super(DescriptionSetterPublisher.class);
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
		public Publisher newInstance(StaplerRequest req, @NonNull JSONObject formData)
				throws FormException {
			if (req == null) {
				return null;
			}
			return req.bindJSON(DescriptionSetterPublisher.class, formData);
		}

		public boolean isMatrixProject(AbstractProject project) {
			return project instanceof MatrixProject;
		}
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl) super.getDescriptor();
	}

	@Deprecated
	public boolean isExplicitNotRegexp() {
		return explicitNotRegexp;
	}

	public String getRegexp() {
		return regexp;
	}

	@Deprecated
	public boolean isSetForFailed() {
		return setForFailed;
	}

	public String getRegexpForFailed() {
		return regexpForFailed;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionForFailed() {
		return descriptionForFailed;
	}

	public MatrixAggregator createAggregator(final MatrixBuild build,
			Launcher launcher, final BuildListener listener) {

		if (!isSetForMatrix()) {
			return null;
		}

		return new MatrixAggregator(build, launcher, listener) {
			@Override
			public boolean endRun(MatrixRun run) throws InterruptedException,
					IOException {

				if (build.getDescription() == null
						&& run.getDescription() != null) {
					build.setDescription(run.getDescription());
				}
				else if(build.getDescription() != null && run.getDescription() != null)
				{
					build.setDescription(
						(appendMode ? build.getDescription() + "<br />" : "")
						+ run.getDescription());
				}
				return true;
			}
		};
	}

	public boolean isSetForMatrix() {
		return setForMatrix;
	}

	public boolean isAppendMode() { return appendMode; }

}
