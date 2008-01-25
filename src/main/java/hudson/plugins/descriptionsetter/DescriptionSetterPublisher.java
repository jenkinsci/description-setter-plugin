package hudson.plugins.descriptionsetter;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.tasks.Publisher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class DescriptionSetterPublisher extends Publisher {

	private final String regexp;

	@DataBoundConstructor
	public DescriptionSetterPublisher(String regexp) {
		this.regexp = regexp;
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException {

		try {
			if (build.getResult().isBetterOrEqualTo(Result.UNSTABLE)) {
				String description = parseLog(build.getLogFile());
				if (description != null) {
					build.addAction(new DescriptionSetterAction(description));
					listener.getLogger().println("Description found: " + description);
				} else {
					listener.getLogger().println("Description not found.");
				}

				build.setDescription(description);
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
				version = matcher.group(1);
				return version;
			}
		}
		return null;
	}

	public static final class DescriptorImpl extends Descriptor<Publisher> {

		private DescriptorImpl() {
			super(DescriptionSetterPublisher.class);
		}

		@Override
		public String getDisplayName() {
			return "Set build description";
		}

		@Override
		public Publisher newInstance(StaplerRequest req, JSONObject formData)
				throws FormException {
			return req.bindJSON(DescriptionSetterPublisher.class, formData);
		}

		public static final DescriptorImpl INSTANCE = new DescriptorImpl();
	}

	public Descriptor<Publisher> getDescriptor() {
		return DescriptorImpl.INSTANCE;
	}

	public String getRegexp() {
		return regexp;
	}

}
