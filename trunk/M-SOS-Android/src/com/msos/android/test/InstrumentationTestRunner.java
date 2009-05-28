package com.msos.android.test;

import junit.framework.TestSuite;
import android.test.InstrumentationTestSuite;

public class InstrumentationTestRunner extends
		android.test.InstrumentationTestRunner {
	
    @Override
    public TestSuite getAllTests() {
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
        suite.addTestSuite(SosRestClientTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return InstrumentationTestRunner.class.getClassLoader();
    }
}
