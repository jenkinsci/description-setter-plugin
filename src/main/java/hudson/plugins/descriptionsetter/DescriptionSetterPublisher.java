package hudson.plugins.descriptionsetter;

import hudson.Extension;
import hudson.Launcher;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class DescriptionSetterPublisher extends Recorder {

	private final boolean explicitNotRegexp;
	private final String regexp;
	private final  boolean setForFailed;
	private final String regexpForFailed;

	@DataBoundConstructor
	public DescriptionSetterPublisher(boolean explicitNotRegexp, String regexp, boolean setForFailed, String regexpForFailed) {
		this.explicitNotRegexp = explicitNotRegexp;
		this.regexp = regexp;
		this.setForFailed = setForFailed;
		this.regexpForFailed = regexpForFailed;
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException {

		try {
          if (!explicitNotRegexp) {
			if (setForFailed && build.getResult().isWorseThan(Result.UNSTABLE)) {
				String description = null;
				if(regexpForFailed != null && !regexpForFailed.equals("")) {
					description = parseFailLog(build.getLogFile());
				}

				if(description == null) {
					description = parseLog(build.getLogFile());
				}
				
				if (description != null) {
					build.addAction(new DescriptionSetterAction(description));
					listener.getLogger().println("Description found: " + description);
				} else {
					listener.getLogger().println("Description not found.");
				}
				build.setDescription(description);
			} else if (setForFailed || build.getResult().isBetterOrEqualTo(Result.UNSTABLE)) {
				String description = parseLog(build.getLogFile());
				if (description != null) {
					build.addAction(new DescriptionSetterAction(description));
					listener.getLogger().println("Description found: " + description);
				} else {
					listener.getLogger().println("Description not found.");
				}

				build.setDescription(description);
			}
          } else { // Hard Code
            if (setForFailed && build.getResult().isWorseThan(Result.UNSTABLE)) {
                build.setDescription(regexpForFailed);
            } else if (setForFailed || build.getResult().isBetterOrEqualTo(Result.UNSTABLE)) {
                build.setDescription(regexp);
            }
          }
		} catch (IOException e) {
			listener.getLogger().println("Description Setter: " + e.getMessage());
			e.printStackTrace(listener.getLogger());
		}

		return true;
	}

	private String parseLog(File logFile) throws IOException,
			InterruptedException {
		String version;
		// Assume default encoding and text files
		String line;
		Pattern pattern = Pattern.compile(regexp);
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				if(matcher.groupCount() == 0) {
					version = matcher.group();
				} else {					
					version = matcher.group(1);
				}
				return version;
			}
		}
		return null;
	}
	
	private String parseFailLog(File logFile) throws IOException,
			InterruptedException {
		String version;
		// Assume default encoding and text files
		String line;
		Pattern pattern = Pattern.compile(regexpForFailed);
		BufferedReader reader = new BufferedReader(new FileReader(logFile));
		while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				if(matcher.groupCount() == 0) {
					version = matcher.group();
				} else {					
					version = matcher.group(1);
				}
				return version;
			}
		}
		return null;
	}

	@Extension
	public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

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
	}

	@Override
	public DescriptorImpl getDescriptor() {
		return (DescriptorImpl)super.getDescriptor();
	}
    
    public boolean isExplicitNotRegexp() {
        return explicitNotRegexp;
	}

	public String getRegexp() {
		return regexp;
	}

	public boolean isSetForFailed() {
		return setForFailed;
	}

	public String getRegexpForFailed() {
		return regexpForFailed;
	}
}
