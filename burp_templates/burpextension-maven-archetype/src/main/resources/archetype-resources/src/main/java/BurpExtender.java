#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

public class BurpExtender implements BurpExtension {
	
	private static String EXTENSION_NAME = "Hello World";
	
    @Override
    public void initialize(MontoyaApi api)
    {
        // set extension name
        api.extension().setName(EXTENSION_NAME);

        //registersuitetab with a new panel
        api.userInterface().registerSuiteTab(EXTENSION_NAME, new mainGUI());
        
        Logging logging = api.logging();

        // write a message to our output stream
        logging.logToOutput("Extension Loaded");

        // throw an exception that will appear in our error stream
        throw new RuntimeException("Error Occured");
    }

}
