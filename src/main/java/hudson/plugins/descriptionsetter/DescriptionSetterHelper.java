package hudson.plugins.descriptionsetter;

import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.Run;
import hudson.model.StringParameterValue;
import hudson.model.TaskListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class that performs common functionality used by both
 * DescriptionSetterBuilder and DescriptionSetterPublisher.
 * 
 */
public class DescriptionSetterHelper {

	private static final String LOG_PREFIX = "[description-setter]";

	/**
	 * Sets the description on the given build based on the specified regular
	 * expression and description.
	 * 
	 * @param build the build whose description to set.
	 * @param listener the build listener to report events to.
	 * @param regexp the regular expression to apply to the build log.
	 * @param description the description to set.
	 * @throws InterruptedException if the build is interrupted by the user.
	 */
	public static void setDescription(Run<?, ?> build,
			TaskListener listener, String regexp, String description)
			throws InterruptedException {

		try {
			Matcher matcher;
			String result = null;

			matcher = parseLog(build.getLogFile(), regexp);
			if (matcher != null) {
				result = getExpandedDescription(matcher, description);
				result = build.getEnvironment(listener).expand(result);
			} else {
				if (result == null && regexp == null && description != null) {
					result = description;
				}
			}

			if (result == null) {
				listener.getLogger().println(
						LOG_PREFIX + " Could not determine description.");
				return;
			}

			result = urlify(result);

			build.addAction(new DescriptionSetterAction(result));
			if(build.getDescription() == null)
			{
				build.setDescription(result);
			}
			else
			{
				String oldDescr = build.getDescription();
				String newDescr = oldDescr + "<br />" + result;
				build.setDescription(newDescr);
			}

			setEnvironmentVariable(result, build);

			listener.getLogger().println(LOG_PREFIX + " Description set: " + result);
		} catch (IOException e) {
			e.printStackTrace(listener.error(LOG_PREFIX
					+ " Error while parsing logs for description-setter"));
		}
	}
	
	private static void setEnvironmentVariable(String result, Run<?, ?> build)
	{
		List<ParameterValue> params = new ArrayList<ParameterValue>();
		params.add(new StringParameterValue("DESCRIPTION_SETTER_DESCRIPTION", result));
		build.addAction(new ParametersAction(params));
	}

	private static Matcher parseLog(File logFile, String regexp)
			throws IOException, InterruptedException {

		if (regexp == null) {
			return null;
		}

		// Assume default encoding and text files
		String line;
		Pattern pattern = Pattern.compile(regexp);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(logFile));
			while ((line = reader.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					return matcher;
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return null;
	}

	private static String getExpandedDescription(Matcher matcher,
			String description) {
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
			result =
					result.replace("\\" + i,
							matcher.group(i) == null ? "" : matcher.group(i));
		}
		return result;
	}

	private static String urlify(String text) {
		try {
			new URL(text);
			return String.format("<a href=\"%s\">%s</a>", text, text);
		} catch (MalformedURLException e) {
			return text;
		}
	}
}
