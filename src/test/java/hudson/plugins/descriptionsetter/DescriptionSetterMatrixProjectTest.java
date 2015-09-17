package hudson.plugins.descriptionsetter;

import hudson.matrix.Axis;
import hudson.matrix.AxisList;
import hudson.matrix.MatrixBuild;
import hudson.matrix.MatrixProject;
import hudson.matrix.MatrixRun;

import org.jvnet.hudson.test.HudsonTestCase;

public class DescriptionSetterMatrixProjectTest extends HudsonTestCase {

    public void testDescriptionInBuilderDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInPublisherDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInPublisherAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInPublisherUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderAndPublisherDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderAndPublisherAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderAndPublisherUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", false, true));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testDescriptionInBuilderAndPublisherAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, true));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", false, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertNull(build.getDescription());
    }

    public void testSetMatrixDescriptionInPublisherDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1<br />test1", build.getDescription());
    }

    public void testSetMatrixDescriptionInPublisherAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1<br />test1", build.getDescription());
    }

    public void testSetMatrixDescriptionInPublisherUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1", build.getDescription());
    }

    public void testSetMatrixDescriptionInPublisherAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionTheSameDescriptionInRunsDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1<br />test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionTheSameDescriptionInRunsAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test1<br />test1", run.getDescription());
        }
        assertEquals("test1<br />test1<br />test1<br />test1<br />test1<br />test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionTheSameDescriptionInRunsUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionTheSameDescriptionInRunsAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test1", run.getDescription());
        }
        assertEquals("test1<br />test1", build.getDescription());
    }

    public void testSetDifferentMatrixDescriptionTheSameDescriptionInRunsDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test2", run.getDescription());
        }
        assertEquals("test2<br />test2", build.getDescription());
    }

    public void testSetDifferentMatrixDescriptionTheSameDescriptionInRunsAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test1<br />test2", run.getDescription());
        }
        assertEquals("test1<br />test1<br />test2<br />test1<br />test1<br />test2", build.getDescription());
    }

    public void testSetDifferentMatrixDescriptionTheSameDescriptionInRunsUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test2", run.getDescription());
        }
        assertEquals("test2", build.getDescription());
    }

    public void testSetDifferentMatrixDescriptionTheSameDescriptionInRunsAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test1<br />test2", run.getDescription());
        }
        assertEquals("test1<br />test1<br />test2", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionDifferentDescriptionInRunsDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1<br />test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionDifferentDescriptionInRunsAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test2<br />test1", run.getDescription());
        }
        assertEquals("test1<br />test2<br />test1<br />test1<br />test2<br />test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionDifferentDescriptionInRunsUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1", run.getDescription());
        }
        assertEquals("test1", build.getDescription());
    }

    public void testSetTheSameMatrixDescriptionDifferentDescriptionInRunsAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test2", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test1", "", true, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test2", run.getDescription());
        }
        assertEquals("test1<br />test2", build.getDescription());
    }

    public void testDifferentSameMatrixDescriptionDifferentDescriptionInRunsDefaultBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, false, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test2", run.getDescription());
        }
        assertEquals("test2<br />test2", build.getDescription());
    }

    public void testDifferentSameMatrixDescriptionDifferentDescriptionInRunsAppendBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, true, false));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test3<br />test2", run.getDescription());
        }
        assertEquals("test1<br />test3<br />test2<br />test1<br />test3<br />test2", build.getDescription());
    }

    public void testDifferentSameMatrixDescriptionDifferentDescriptionInRunsUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, false, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test2", run.getDescription());
        }
        assertEquals("test2", build.getDescription());
    }

    public void testDifferentSameMatrixDescriptionDifferentDescriptionInRunsAppendAndUniquenessBehavior() throws Exception {
        MatrixProject project = createMatrixProject();
        project.setAxes(new AxisList(new Axis("axis", "a", "b")));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test1", true, false));
        project.getBuildersList().add(new DescriptionSetterBuilder("", "test3", true, false));
        project.getPublishersList().add(
                new DescriptionSetterPublisher("", "", "test2", "", true, true, true));
        MatrixBuild build = project.scheduleBuild2(0).get();
        for(MatrixRun run : build.getExactRuns()) {
            assertEquals("test1<br />test3<br />test2", run.getDescription());
        }
        assertEquals("test1<br />test3<br />test2", build.getDescription());
    }
}
