# Description setter plugin

This plugin sets the description for each build, based on a [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html) match of the contents of the build log.
To use the plugin, you must enable it in the Post Build actions of your freestyle or matrix job configuration.

Once enabled, you will notice that it asks for a [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html).
This [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html) is used to scan the build log file, line by line (multi lines are notsupported).
It will take the first line that matches the [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html).
The description is  set for that particular build from the first [capturing group](https://docs.oracle.com/javase/tutorial/essential/regex/groups.html) of the regular expression.
The  [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html) must specify at least one capturing group by putting the part you want to select between parentheses.

You can thus use this to have your build script echo out some particular information such as the modifying authors from SCM, or the datestamp, or the published version number, or....

For example, if your build file contains the lines

    ...
    [version] My Application Version XYZ
    ...

Then in order to set the build description to My Application Version XYZ, you would set the [regular expression](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html) to be:

    \[version\] (.*)

An Apache Maven release build would typically have a line like:

    ...
    [INFO] Uploading project information for my.project.name 0.4.0
    ...

So the following regular expression would select the version:

     .*\[INFO\] Uploading project information for [^\s]* ([^\s]*)

The [Build Name Setter plugin](https://plugins.jenkins.io/build-name-setter/) provides similiar functionality for the display name, instead of description.

### Using multiple capture groups

You can now use different groups captured by the regular expression (e.g. "foo \\1 bar \\2") by referencing them in the Description field.
(If you leave the description field empty, the first match group will be used.)

## Changelog

Release notes are recorded in [GitHub](https://github.com/jenkinsci/description-setter-plugin/releases).
