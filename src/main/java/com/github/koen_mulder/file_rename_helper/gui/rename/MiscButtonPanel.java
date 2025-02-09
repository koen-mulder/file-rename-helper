package com.github.koen_mulder.file_rename_helper.gui.rename;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.controller.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventListener;

public class MiscButtonPanel extends JPanel implements FormEventListener{

    private static final long serialVersionUID = 5711654747954407097L;

    private NewFilenameFieldController newFilenameFieldController;
    
    private List<JButton> buttons;

    public MiscButtonPanel(NewFilenameFieldController newFilenameFieldController) {
        
        this.newFilenameFieldController = newFilenameFieldController;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Misc functions"));
        
        JButton removeConsecutiveSpecialCharactersButton = new JButton("Consecutive special characters");
        removeConsecutiveSpecialCharactersButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Pattern pattern = Pattern.compile("([^a-zA-Z0-9\\s])\\1+"); // Group the character and use \1 (backreference) to find consecutive occurrences.

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
        });
        add(removeConsecutiveSpecialCharactersButton);
        
        buttons = Lists.newArrayList();
//        buttons.add(new JButton(
//                new ReplaceCharacterButtonAction(newFilenameFieldController, "[space] → _ [underscore]", " ", "_")));
//        buttons.add(new JButton(
//                new ReplaceCharacterButtonAction(newFilenameFieldController, "[dash] - → _ [underscore]", "-", "_")));
//        buttons.add(new JButton(
//                new ReplaceCharacterButtonAction(newFilenameFieldController, "[underscore] _ → [space]", "_", " ")));
//
//        // Add disabled buttons to panel
//        for (JButton button : buttons) {
//            button.setEnabled(false);
//            add(button);
//        }
        
    }
    
    @Override
    public void setEnabled(boolean enable) {
        for (JButton button : buttons) {
            button.setEnabled(enable);
        }
    }
    
    public void clearPanel() {
        // Remove all keyword buttons
        removeAll();
    }

    @Override
    public void onFormEvent(EFormEvent event) {
        // Disable on form events unless it is the enable event
        setEnabled(event == EFormEvent.ENABLE);
    }

//    private class ReplaceCharacterButtonAction extends AbstractAction {
//
//        private static final long serialVersionUID = 6958961519876494377L;
//
//        private NewFilenameFieldController newFilenameFieldController;
//
//        private String target;
//        private String replacement;
//
//        public ReplaceCharacterButtonAction(NewFilenameFieldController newFilenameFieldController, String title,
//                String target, String replacement) {
//
//            this.newFilenameFieldController = newFilenameFieldController;
//
//            this.target = target;
//            this.replacement = replacement;
//
//            putValue(NAME, title);
//            putValue(SHORT_DESCRIPTION,
//                    String.format("Replace \"{}\" to \"{}\" in the filename field.", target, replacement));
//        }
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            newFilenameFieldController.replace(target, replacement);
//        }
//
//    }
}
