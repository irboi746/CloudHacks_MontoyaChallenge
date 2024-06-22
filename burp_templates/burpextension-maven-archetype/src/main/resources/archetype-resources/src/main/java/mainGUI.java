#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


public class mainGUI extends JPanel {
	private JTextField textFieldImportPath;

	/**
	 * Create the panel.
	 */
	public mainGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPaneNorth = new JSplitPane();
		splitPaneNorth.setResizeWeight(0.9);
		add(splitPaneNorth, BorderLayout.NORTH);
		
		textFieldImportPath = new JTextField();
		textFieldImportPath.setEditable(false);
		splitPaneNorth.setLeftComponent(textFieldImportPath);
		textFieldImportPath.setColumns(10);
		
		JButton btnImport = new JButton("Import");
		splitPaneNorth.setRightComponent(btnImport);
		btnImport.addActionListener(l -> importJson());
	}
	
	private void importJson() {
		// Create a file chooser
		JFileChooser fileChooser = new JFileChooser();
		  // Show the file open dialog
		  int returnVal = fileChooser.showOpenDialog(this); 

		  // Handle the user's choice
		  if (returnVal == JFileChooser.APPROVE_OPTION) {
		    // Get the selected file
		    File selectedFile = fileChooser.getSelectedFile();
		    // Update the text field with the selected file path
		    if (validateJson(selectedFile)) {
		    	// Update the text field with the selected file path
		    	textFieldImportPath.setText(selectedFile.getAbsolutePath());
		    }   
		    
		    }
	}
	private boolean validateJson(File file) {
		if (file == null || !file.exists()) {
			return false;
			}
		try {
			JsonElement jsonElement = new Gson().fromJson(new FileReader(file), JsonElement.class);
			return true;
			} catch (Exception e) {
				return false;
			}
	}
}
