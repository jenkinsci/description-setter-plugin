package hudson.plugins.descriptionsetter;

import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.matrix.MatrixAggregatable;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.matrix.MatrixRun;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class DescriptionSetterPublisher extends Recorder implements
		MatrixAggregatable {

	private final String regexp;
	private final String regexpForFailed;
	private final String description;

	private final String descriptionForFailed;
	private final boolean setForMatrix;

	@Deprecated
	private transient boolean setForFailed = false;

	@Deprecated
	private transient boolean explicitNotRegexp = false;

	@DataBoundConstructor
	public DescriptionSetterPublisher(String regexp, String regexpForFailed,
			String description, String descriptionForFailed,
			boolean setForMatrix) {
		this.regexp = regexp;
		this.regexpForFailed = regexpForFailed;
		this.description = Util.fixEmptyAndTrim(description);
		this.descriptionForFailed = Util.fixEmptyAndTrim(descriptionForFailed);
		this.setForMatrix = setForMatrix;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException {

		try {
			Matcher matcher;
			String result = null;

			boolean useUnstable = (regexpForFailed != null || descriptionForFailed != null)
					&& build.getResult().isWorseThan(Result.UNSTABLE);

			matcher = parseLog(build.getLogFile(),
					useUnstable ? regexpForFailed : regexp);
			if (matcher != null) {
				result = getExpandedDescription(matcher,
						useUnstable ? descriptionForFailed : description);
				result = build.getEnvironment(listener).expand(result);
			} else {
				if (useUnstable) {
					if (result == null && regexpForFailed == null
							&& descriptionForFailed != null) {
						result = descriptionForFailed;
					}
				} else {
					if (result == null && regexp == null && description != null) {
						result = description;
					}
				}
			}

			if (result == null) {
				listener
						.getLogger()
						.println(
								"[description-setter] Could not determine description.");
				return true;
			}

			result = urlify(result);

			build.addAction(new DescriptionSetterAction(result));
			listener.getLogger().println("Description set: " + result);
			build.setDescription(result);
		} catch (IOException e) {
			e.printStackTrace(listener
					.error("error while parsing logs for description-setter"));
		}

		return true;
	}

	private Matcher parseLog(File logFile, String regexp) throws IOException,
			InterruptedException {

		if (regexp == null) {
			return null;
		}

		// Assume default encoding and text files
		String line;
		Pattern pattern = Pattern.compile(regexp);
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				return matcher;
			}
		}
		return null;
	}

	private Object readResolve() throws ObjectStreamException {
		if (explicitNotRegexp) {
			return new DescriptionSetterPublisher(null, null, regexp,
					setForFailed ? regexpForFailed : null, false);
		} else {
			return this;
		}
	}

	private String getExpandedDescription(Matcher matcher, String description) {
		String result = description;
		if (result == null) {
			if (matcher.groupCount() == 0) {
				result = "\\0";
			} else {
				result = "\\1";
			}
		}

        // Expand all groups: 1..Count, as well as 0 for the entire pattern
		for (int i = matcher.groupCount(); i >= 0; i--) {
			result = result.replace("\\" + i, matcher.group(i));
		}
		return result;
	}

	private String urlify(String text) {
		try {
			new URL(text);
			return String.format("<a href=\"%s\">%s</a>", text, text);
		} catch (MalformedURLException e) {
			return text;
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
			return "Set build description";
		}

		@Override
		public boolean isApplicable(Class<? extends AbstractProject> jobType) {
			return true;
		}

		@Override
		public Publisher newInstance(StaplerRequest req, JSONObject formData)
				throws FormException {
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
				return true;
			}
		};
	}

	public boolean isSetForMatrix() {
		return setForMatrix;
	}

}
