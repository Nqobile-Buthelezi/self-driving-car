package za.co.bangoma.neural;

import junit.framework.TestCase;

public class ApplicationTest extends TestCase {

    public void testScreenInstantiation() {
        Screen testSCreen = Application.createScreen(true);
        assertNotNull("Screen instance is not null.", testSCreen);
    }

}