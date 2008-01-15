package hudson.plugins.descriptionsetter;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * @plugin
 */
public class PluginImpl extends Plugin {

	public void start() throws Exception {
		BuildStep.PUBLISHERS
				.addRecorder(DescriptionSetterPublisher.DescriptorImpl.INSTANCE);
	}

}
