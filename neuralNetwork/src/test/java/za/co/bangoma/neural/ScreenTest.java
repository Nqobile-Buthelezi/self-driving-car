package za.co.bangoma.neural;

import junit.framework.TestCase;

public class ScreenTest extends TestCase {

    public void testNonInitialisationComponents() {
        Screen screen = new Screen(true);

        assertNull("Road Canvas is not initialised.", screen.roadCanvas);
        assertNull("Network Canvas is not initialised.", screen.networkCanvas);
    }

}