package za.co.bangoma.neural;

public class Application {

    public static void main( String[] args ) {
        Screen s = createScreen( false );
    }

    public static Screen createScreen( boolean testEnvironment ) {
        try {
            return testEnvironment ? new Screen( testEnvironment ) : new Screen();
        } catch ( Exception e ) {
            System.out.println( "Error creating screen instance: " + e );
            return null;
        }
    }

}
