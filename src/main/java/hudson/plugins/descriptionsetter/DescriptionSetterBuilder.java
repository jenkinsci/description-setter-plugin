package hudson.plugins.descriptionsetter;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.Launcher;
import hudson.Util;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
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
    private final boolean envVariable;

    @DataBoundConstructor
    public DescriptionSetterBuilder(String regexp, String description, boolean appendMode, boolean envVariable) {
        this.regexp = regexp;
        this.description = Util.fixEmptyAndTrim(description);
        this.appendMode = appendMode;
        this.envVariable = envVariable;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException {

        return DescriptionSetterHelper.setDescription(build, listener, regexp, description, appendMode, envVariable);
    }

    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

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
        public Builder newInstance(StaplerRequest req, @NonNull JSONObject formData) throws FormException {
            if (req == null) {
                return null;
            }
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
