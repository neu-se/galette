package edu.neu.ccs.prl.galette.bench.extension;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;

import java.util.LinkedList;
import java.util.List;
import org.junit.platform.launcher.*;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

final class TestCollector {
    static List<TestIdentifier> collectTests() {
        System.setProperty(TagManagerResolver.MANAGER_KEY, EmptyTagManager.class.getName());
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("edu.neu.ccs.prl.galette.bench"))
                .filters(includeClassNamePatterns(".*ITCase"))
                .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan plan = launcher.discover(request);
        List<TestIdentifier> testIdentifiers = new LinkedList<>();
        TestExecutionListener listener = new TestExecutionListener() {
            @Override
            public void executionStarted(TestIdentifier testIdentifier) {
                if (testIdentifier.isTest()) {
                    testIdentifiers.add(testIdentifier);
                }
            }
        };
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(plan);
        return testIdentifiers;
    }

    private static final class EmptyTagManager implements TagManager {

        @Override
        public void setUp() {}

        @Override
        public void tearDown() {}

        @Override
        public boolean setLabels(boolean value, Object[] labels) {
            return value;
        }

        @Override
        public byte setLabels(byte value, Object[] labels) {
            return value;
        }

        @Override
        public char setLabels(char value, Object[] labels) {
            return value;
        }

        @Override
        public short setLabels(short value, Object[] labels) {
            return value;
        }

        @Override
        public int setLabels(int value, Object[] labels) {
            return value;
        }

        @Override
        public long setLabels(long value, Object[] labels) {
            return value;
        }

        @Override
        public float setLabels(float value, Object[] labels) {
            return value;
        }

        @Override
        public double setLabels(double value, Object[] labels) {
            return value;
        }

        @Override
        public <T> T setLabels(T value, Object[] labels) {
            return value;
        }

        @Override
        public Object[] getLabels(boolean value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(byte value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(char value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(short value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(int value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(long value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(float value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(double value) {
            return new Object[0];
        }

        @Override
        public Object[] getLabels(Object value) {
            return new Object[0];
        }
    }
}
