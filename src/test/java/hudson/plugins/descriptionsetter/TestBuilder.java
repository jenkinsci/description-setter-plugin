package hudson.plugins.descriptionsetter;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import java.io.IOException;

public class TestBuilder extends Builder {
    private final String text;
    private final Result result;

    public TestBuilder(String text, Result result) {
        super();
        this.text = text;
        this.result = result;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener)
            throws InterruptedException, IOException {
        listener.getLogger().println(text);
        build.setResult(result);
        return true;
    }

    public Descriptor<Builder> getDescriptor() {
        return new BuildStepDescriptor<Builder>() {

            @Override
            public String getDisplayName() {
                return "TestBuilder";
            }

            @Override
            public boolean isApplicable(Class<? extends AbstractProject> jobType) {
                return true;
            }
        };
    }
}
