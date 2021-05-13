This plugin sets the description for each build, based upon a
[RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html)
test of the build log file.  
To use the plug in, you must tick it in the Post Build Actions, in your
job configuration page. Once ticked, you will notice that it asks for a
[RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html).
This
[RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html)
is used to scan the build log file, line by line (multi lines are not
supported), and it will take the first line that matches your
[RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html).
The description that is then set for that particular build, is by
default taken from the first Capturing Group. You must specify at least
one capturing group in your
[RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html)
by putting the part you want to select between parentheses (if you
specify more, these will be ignored).

You can thus use this to have your build script echo out some particular
information such as the modifying authors from SCM, or the datestamp, or
the published version number, or....

For example, if your build file contains the lines

    ...
    [version] My Application Version XYZ
    ...

Then in order to set the build description to My Application Version
XYZ, you would set the
[RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html)
to be:

    \[version\] (.*)

A Maven release build would typically have a line like:

    ...
    [INFO] Uploading project information for my.project.name 0.4.0
    ...

So the following regex would select the version:

     .*\[INFO\] Uploading project information for [^\s]* ([^\s]*)

People interested in this plugin might also find [Build Name Setter
Plugin](http://wiki.jenkins-ci.org/display/JENKINS/Build+Name+Setter+Plugin)
useful, as it does similar thing for the display name, instead of
description.

### Using multiple capture groups

You can now use different groups captured by the RegEx (e.g. "foo \\1
bar \\2") by referencing them in the Description field. (If you leave
the description field empty, the first match group will be used.)

## Changelog

#### Release 1.9 (Jun 13, 2014)

-   Set description during build as a build step
    ([JENKINS-23097](https://issues.jenkins-ci.org/browse/JENKINS-23097))
-   Updated maven pom to use repo.jenkins-ci.org repository
-   Localization for ja

#### Release 1.8 (Mar 26, 2011)

-   Handle regex matches even when the match group element is null.

#### Release 1.7 (Dec 31, 2010)

-   Fixed a problem with handling more than one match group
    substitution.
    ([JENKINS-4980](https://issues.jenkins-ci.org/browse/JENKINS-4980),
    [JENKINS-8328](https://issues.jenkins-ci.org/browse/JENKINS-8328))

#### Release 1.6 (Dec 1, 2009)

-   Bugfix (1.5 is only usable for matrix projects)

#### Release 1.5

-   Large parts are rewritten. Should be backwards compatible -- create
    an issue if not.
-   You can now use groups captured by the regex in descriptions (e.g.
    "foo \\1 bar \\2"). Default is still to use the first group as
    description.
-   Also works for matrix projects

#### Release 1.4

-   Added option "Use explicit description instead of regexp".  Allows
    user to hard code the description instead of doing a regexp search
    of build log.  Default is off.

#### Release 1.3

-   Fixes an issue where the
    [RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html) could
    return only 1 item and cause and array out of bounds problem. 

#### Release 1.2

-   Add ability to set the description for failed builds. On your job
    configuration page you will need to check the box for setting
    description upon build failure and if you wish to use a different
    [RegEx](http://java.sun.com/j2se/1.5.0/docs/api/java/util/regex/Pattern.html) then
    the main one you can enter it in the text box below.

