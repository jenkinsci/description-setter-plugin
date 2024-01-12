package hudson.plugins.descriptionsetter;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Descriptor.FormException;
import hudson.model.Job;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.Run;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class JobByDescription extends JobProperty<Job<?, ?>> {

    @Override
    public Action getJobAction(Job<?, ?> job) {
        return new JobByDescriptionAction(owner);
    }

    @Extension
    public static class DescriptorImpl extends JobPropertyDescriptor {

        public DescriptorImpl() {
            super(JobByDescription.class);
        }

        @Override
        public boolean isApplicable(Class<? extends Job> arg0) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Allow getting a build by description";
        }

        @Override
        public JobProperty<?> newInstance(StaplerRequest req, JSONObject formData) throws FormException {
            return new JobByDescription();
        }
    }

    public static class JobByDescriptionAction implements Action {

        private Job<?, ?> owner;

        public JobByDescriptionAction(Job<?, ?> owner) {
            this.owner = owner;
        }

        public String getDisplayName() {
            return null;
        }

        public String getIconFileName() {
            return null;
        }

        public String getUrlName() {
            return "by-description";
        }

        public Object getDynamic(String token, StaplerRequest req, StaplerResponse rsp) {
            for (Run<?, ?> run : owner.getBuilds()) {
                DescriptionSetterAction action = run.getAction(DescriptionSetterAction.class);
                if (action != null && token.equals(action.getDescription())) {
                    return run;
                }
            }

            return null;
        }
    }
}
