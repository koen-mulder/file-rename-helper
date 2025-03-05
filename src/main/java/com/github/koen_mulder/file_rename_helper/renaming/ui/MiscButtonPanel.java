package com.github.koen_mulder.file_rename_helper.renaming.ui;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.renaming.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.shared.WrapLayout;

public class MiscButtonPanel extends JPanel implements IOpenFileActionListener{

    private static final long serialVersionUID = 5711654747954407097L;

    private List<JButton> buttons;

    public MiscButtonPanel(NewFilenameFieldController newFilenameFieldController) {
        
        setLayout(new WrapLayout(WrapLayout.LEFT));
        setBorder(new TitledBorder("Misc functions"));
        
        buttons = Lists.newArrayList();
        buttons.add(new JButton(new ConsecutiveReplaceButtonAction(newFilenameFieldController,
                "Remove consecutive special characters", "Remove consecutive special characters", "([^a-zA-Z0-9\\s])\\1+")));
        buttons.add(new JButton(new ConsecutiveReplaceButtonAction(newFilenameFieldController,
                "Remove consecutive white spaces", "Remove consecutive whitespaces", "([\\s])\\1+")));
        buttons.add(new JButton(new AddWhiteSpaceButtonAction(newFilenameFieldController)));

        // Add disabled buttons to panel
        for (JButton button : buttons) {
            button.setEnabled(false);
            add(button);
        }
        
    }
    
    @Override
    public void setEnabled(boolean enable) {
        for (JButton button : buttons) {
            button.setEnabled(enable);
        }
    }
    
    @Override
    public void onOpenFileAction(FileProcessingItem fileItem) {
        setEnabled(fileItem != null);
    }

    private class ConsecutiveReplaceButtonAction extends AbstractAction {

        private static final long serialVersionUID = -5004018547334595184L;
        
        private NewFilenameFieldController newFilenameFieldController;
        private String patternString;

        public ConsecutiveReplaceButtonAction(NewFilenameFieldController newFilenameFieldController, String title, String description, String patternString) {
            this.newFilenameFieldController = newFilenameFieldController;
            this.patternString = patternString;
            
            putValue(NAME, title);
            putValue(SHORT_DESCRIPTION, description);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Pattern pattern = Pattern.compile(patternString); // Group the character and use \1 (backreference) to find consecutive occurrences.

            Matcher matcher = pattern.matcher(newFilenameFieldController.getText());
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                // matcher.group(1) gets the *first* capturing group, which is the single special character.
                //  matcher.group() would get the entire matched sequence (e.g., "___"). We only want one "_".
                matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group(1))); // Use group 1 (the single char) as replacement
            }
            matcher.appendTail(result);

            newFilenameFieldController.setText(result.toString());
        }

    }
    
    private class AddWhiteSpaceButtonAction extends AbstractAction {

        private static final long serialVersionUID = -5004018547334595184L;
        
        private NewFilenameFieldController newFilenameFieldController;

        public AddWhiteSpaceButtonAction(NewFilenameFieldController newFilenameFieldController) {
            this.newFilenameFieldController = newFilenameFieldController;
            
            putValue(NAME,  "Add white space");
            putValue(SHORT_DESCRIPTION, "Add white space around numbers and before capitals");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Regular expression to find the places to insert spaces
            String regex = "(?<=[a-z])(?=[A-Z])|" +  // Lowercase followed by Uppercase
                           "(?<=[A-Z])(?=[A-Z][a-z])|" + // Uppercase followed by Uppercase then lowercase
                           "(?<=[0-9])(?=[A-Za-z])|" + // Digit followed by Letter
                           "(?<=[A-Za-z])(?=[0-9])";    // Letter followed by Digit

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(newFilenameFieldController.getText());
            newFilenameFieldController.setText( matcher.replaceAll(" ").toString());
        }

    }

}
