package hudson.plugins.descriptionsetter;

import hudson.model.Action;

public class DescriptionSetterAction implements Action {

	private static final long serialVersionUID = 1L;

	private final String version;

	public DescriptionSetterAction(String version) {
		super();
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public String getDisplayName() {
		return "Release Version";
	}

	public String getIconFileName() {
		return null;
	}

	public String getUrlName() {
		return "release-version";
	}
}
