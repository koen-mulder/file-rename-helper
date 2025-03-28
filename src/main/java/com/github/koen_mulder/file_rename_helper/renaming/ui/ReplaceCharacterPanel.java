package com.github.koen_mulder.file_rename_helper.renaming.ui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.compress.utils.Lists;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.renaming.NewFilenameFieldController;
import com.github.koen_mulder.file_rename_helper.shared.WrapLayout;

/**
 * Panel for replacing special characters in the new filename input field
 */
@SuppressWarnings("serial") // Same-version serialization only
class ReplaceCharacterPanel extends JPanel implements IOpenFileActionListener {

    private List<JButton> buttons;

    /**
     * Panel for replacing special characters in the new filename input field
     * 
     * @param newFilenameFieldController controller for interacting with the new filename field
     */
    public ReplaceCharacterPanel(NewFilenameFieldController newFilenameFieldController) {

        setLayout(new WrapLayout(WrapLayout.LEFT));
        setBorder(new TitledBorder("Replace"));

        buttons = Lists.newArrayList();
        buttons.add(new JButton(
                new ReplaceCharacterButtonAction(newFilenameFieldController, "[space] → _ [underscore]", " ", "_")));
        buttons.add(new JButton(
                new ReplaceCharacterButtonAction(newFilenameFieldController, "[dash] - → _ [underscore]", "-", "_")));
        buttons.add(new JButton(
                new ReplaceCharacterButtonAction(newFilenameFieldController, "[underscore] _ → [space]", "_", " ")));

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

    private class ReplaceCharacterButtonAction extends AbstractAction {

        private NewFilenameFieldController newFilenameFieldController;

        private String target;
        private String replacement;

        public ReplaceCharacterButtonAction(NewFilenameFieldController newFilenameFieldController, String title,
                String target, String replacement) {

            this.newFilenameFieldController = newFilenameFieldController;

            this.target = target;
            this.replacement = replacement;

            putValue(NAME, title);
            putValue(SHORT_DESCRIPTION,
                    String.format("Replace \"{}\" to \"{}\" in the filename field.", target, replacement));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            newFilenameFieldController.replace(target, replacement);
        }

    }

}
