package HelloWorld;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class Main implements BurpExtension {
	private static String EXTENSION_NAME = "Hello World";
	
    @Override
    public void initialize(MontoyaApi api)
    {
        // set extension name
        api.extension().setName(EXTENSION_NAME);

        Logging logging = api.logging();

        // write a message to our output stream
        logging.logToOutput("Extension Loaded");

        // throw an exception that will appear in our error stream
        throw new RuntimeException("Error Occured");
    }
}