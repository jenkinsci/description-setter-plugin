package hudson.plugins.descriptionsetter;

import hudson.model.Action;

public class DescriptionSetterAction implements Action {

	private static final long serialVersionUID = 1L;
	private final String description;

	public DescriptionSetterAction(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return "Description";
	}

	public String getIconFileName() {
		return null;
	}

	public String getUrlName() {
		return "description";
	}

	public String getDescription() {
		return description;
	}
}
