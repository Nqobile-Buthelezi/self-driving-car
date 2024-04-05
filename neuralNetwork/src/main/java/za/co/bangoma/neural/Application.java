package za.co.bangoma.neural;

/**
 * The main entry point to the application
 * <p>
 *     This class initialises the application and serves as an entry point
 *     for execution it creates a {@link Screen} to start the GUI user interface
 *     of the application.
 * </p>
 */
public class Application {

    /**
     * The main method to start the application.
     * <p>
     *     This method initializes the application and starts its execution.
     *     It creates a {@link Screen} instance, which serves as the main user interface.
     * </p>
     * @param args The command-line arguments passed to the application (currently unused).
     */
    public static void main(String[] args) {
        createScreen(false);
    }

    /**
     * Returns a new instance of the screen object
     *
     * @return Screen object or null
     */
    public static Screen createScreen(boolean testEnvironment) {
        try {
            return testEnvironment ? new Screen(testEnvironment) : new Screen();
        } catch (Exception e) {
            System.out.println("Error creating screen instance: " + e);
            return null;
        }
    }

}
