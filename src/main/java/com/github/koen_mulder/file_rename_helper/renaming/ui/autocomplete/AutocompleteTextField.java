package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultEditorKit;

@SuppressWarnings("serial") // Same-version serialization only
public class AutocompleteTextField extends JTextField {

    // Action Keys
    private static final String ACCEPT_CHAR_ACTION_KEY = "acceptCharAction";
    private static final String ACCEPT_WORD_ACTION_KEY = "acceptWordAction";
    private static final String ACCEPT_FULL_ACTION_KEY = "acceptFullAction";
    private static final String REQUEST_AUTOCOMPLETE_ACTION_KEY = "requestAutocompleteAction";
    private static final String CARET_BACKWARD_ACTION_KEY = "caretBackwardAction";
    private static final String CARET_WORD_BACKWARD_ACTION_KEY = "caretWordBackwardAction";
    private static final String CANCEL_SUGGESTION_ACTION_KEY = "cancelSuggestionAction";
    private static final String DELETE_PREVIOUS_ACTION_KEY = "deletePreviousWordAction";
    private static final String ARROW_DOWN_ACTION_KEY = "arrowDownAction";
    private static final String ARROW_UP_ACTION_KEY = "arrowUpAction";
    private static final String ACCEPT_AUTOCOMPLETE_ACTION_KEY = "acceptAutocompleteAction";
    
    private static final String ACCEPT_SUGGESTION_0_ACTION_KEY = "acceptSuggestion0Action";
    private static final String ACCEPT_SUGGESTION_1_ACTION_KEY = "acceptSuggestion1Action";
    private static final String ACCEPT_SUGGESTION_2_ACTION_KEY = "acceptSuggestion2Action";
    private static final String ACCEPT_SUGGESTION_3_ACTION_KEY = "acceptSuggestion3Action";
    private static final String ACCEPT_SUGGESTION_4_ACTION_KEY = "acceptSuggestion4Action";
    private static final String ACCEPT_SUGGESTION_5_ACTION_KEY = "acceptSuggestion5Action";
    private static final String ACCEPT_SUGGESTION_6_ACTION_KEY = "acceptSuggestion6Action";
    private static final String ACCEPT_SUGGESTION_7_ACTION_KEY = "acceptSuggestion7Action";
    private static final String ACCEPT_SUGGESTION_8_ACTION_KEY = "acceptSuggestion8Action";
    private static final String ACCEPT_SUGGESTION_9_ACTION_KEY = "acceptSuggestion9Action";

    private JPopupMenu autocompletePopup;
    private DefaultListModel<AutocompleteItem> autocompleteListModel;
    private JList<AutocompleteItem> autocompleteList;

    private AutocompleteService autocompleteService;
    private DefaultListCellRenderer cellRenderer;

    private AutocompleteDocumentFilter filter;

    public AutocompleteTextField(AutocompleteService autocompleteService,
            DefaultListCellRenderer cellRenderer) {
        this.autocompleteService = autocompleteService;
        this.cellRenderer = cellRenderer;

        setupDocumentFilter();
        setupPopup();
        setupListeners();
        setupKeyBindings();
    }

    private void setupDocumentFilter() {
        filter = new AutocompleteDocumentFilter(this);
        ((AbstractDocument) getDocument()).setDocumentFilter(filter);
    }

    private void setupPopup() {
        autocompletePopup = new JPopupMenu();
        autocompletePopup.setFocusable(false);
        autocompletePopup.setOpaque(false);

        autocompleteListModel = new DefaultListModel<>();
        autocompleteListModel.addAll(autocompleteService.getSuggestions());

        autocompleteList = new JList<AutocompleteItem>(autocompleteListModel);
        autocompleteList.setFocusable(false);
        autocompleteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        autocompleteList.setCellRenderer(cellRenderer);
        autocompleteList.setRequestFocusEnabled(false);
        autocompleteList.setFont(autocompleteList.getFont().deriveFont(Font.PLAIN));
        
        autocompleteList.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 1) acceptSelectedAutocomplete(); }
        });

        autocompletePopup.setLayout(new BorderLayout());
        autocompletePopup.add(autocompleteList, BorderLayout.CENTER);
    }

    private void setupListeners() {
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                showSuggestionPopup();
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                Component oppositeComponent = e.getOppositeComponent();
                boolean focusToPopup = false;
                if (oppositeComponent != null) {
                    Window popupWindow = SwingUtilities.getWindowAncestor(autocompletePopup);
                    if (popupWindow != null) {
                        focusToPopup = (oppositeComponent == popupWindow || SwingUtilities.isDescendingFrom(oppositeComponent, popupWindow));
                    }
                }
                if (!focusToPopup) {
                    hideSuggestionPopup();
//                    Timer timer = new Timer(150, ae -> hideSuggestionPopup());
//                    timer.setRepeats(false);
//                    timer.start();
                }
            }
        });
    }

    private void setupKeyBindings() {
        InputMap inputMap = getInputMap(JComponent.WHEN_FOCUSED);
        ActionMap actionMap = getActionMap();

        // Right Arrow: Accept next character OR default
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ACCEPT_CHAR_ACTION_KEY);
        actionMap.put(ACCEPT_CHAR_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getCaretPosition() == getSelectionStart()
                        && getSelectionStart() != getSelectionEnd()) {
                    acceptNextCharacter();
                } else {
                    executeDefaultAction(DefaultEditorKit.forwardAction, actionMap, e);
                }
            }
        });

        // Ctrl + Right Arrow: Accept next word OR default
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK),
                ACCEPT_WORD_ACTION_KEY);
        actionMap.put(ACCEPT_WORD_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getCaretPosition() == getSelectionStart()
                        && getSelectionStart() != getSelectionEnd()) {
                    acceptNextWord();
                } else {
                    executeDefaultAction(DefaultEditorKit.nextWordAction, actionMap, e);
                }
            }
        });

        // Tab: Accept full suggestion or list selection OR default
        setFocusTraversalKeysEnabled(false);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), ACCEPT_FULL_ACTION_KEY);
        actionMap.put(ACCEPT_FULL_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getCaretPosition() == getSelectionStart()
                        && getSelectionStart() != getSelectionEnd()) {
                    acceptFullSuggestion();
                } else {
                    transferFocus();
                }
            }
        });

        // Ctrl + Space: Request suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, InputEvent.CTRL_DOWN_MASK),
                REQUEST_AUTOCOMPLETE_ACTION_KEY);
        actionMap.put(REQUEST_AUTOCOMPLETE_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Use invokeLater to ensure UI updates happen correctly after document changes
                SwingUtilities.invokeLater(() -> {
                    filter.setActive(false);
                    try {
                        String leadingText = getText().substring(0, getCaretPosition());
                        String trailingText = getText().substring(getCaretPosition());

                        handleInlineCompletion(leadingText, trailingText);
                        handlePopupCompletion(leadingText, trailingText);
                    } finally {
                        // Crucial: Reset the flag after processing is done
                        SwingUtilities.invokeLater(() -> filter.setActive(true));
                    }
                });
            }
        });

        // Left arrow: Move caret backward and remove active suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), CARET_BACKWARD_ACTION_KEY);
        actionMap.put(CARET_BACKWARD_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSuggestion();
                executeDefaultAction(DefaultEditorKit.backwardAction, actionMap, e);
            }
        });

        // Ctrl + Left arrow: Move caret backward a word and remove active suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK),
                CARET_WORD_BACKWARD_ACTION_KEY);
        actionMap.put(CARET_WORD_BACKWARD_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSuggestion();
                executeDefaultAction(DefaultEditorKit.previousWordAction, actionMap, e);
            }
        });

        // Ctrl + Backspace: Remove previous word and remove active suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, InputEvent.CTRL_DOWN_MASK),
                DELETE_PREVIOUS_ACTION_KEY);
        actionMap.put(DELETE_PREVIOUS_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSuggestion();
                executeDefaultAction(DefaultEditorKit.deletePrevWordAction, actionMap, e);
            }
        });

        // Escape: Cancel suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CANCEL_SUGGESTION_ACTION_KEY);
        actionMap.put(CANCEL_SUGGESTION_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSuggestion();
            }
        });

        // Arrow Down: Move selection down in the suggestion list
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ARROW_DOWN_ACTION_KEY);
        actionMap.put(ARROW_DOWN_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autocompletePopup.isShowing()) {
                    int selectedIndex = autocompleteList.getSelectedIndex();
                    if (selectedIndex < autocompleteListModel.getSize() - 1) {
                        autocompleteList.setSelectedIndex(selectedIndex + 1);
                        autocompleteList.ensureIndexIsVisible(selectedIndex + 1);
                    }
                } else {
                    executeDefaultAction(DefaultEditorKit.downAction, actionMap, e);
                }
            }
        });

        // Arrow Up: Move selection up in the suggestion list
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), ARROW_UP_ACTION_KEY);
        actionMap.put(ARROW_UP_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autocompletePopup.isShowing()) {
                    int selectedIndex = autocompleteList.getSelectedIndex();
                    if (selectedIndex > 0) {
                        autocompleteList.setSelectedIndex(selectedIndex - 1);
                        autocompleteList.ensureIndexIsVisible(selectedIndex - 1);
                    }
                } else {
                    executeDefaultAction(DefaultEditorKit.upAction, actionMap, e);
                }
            }
        });

        // Enter: Accept selected autocomplete
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ACCEPT_AUTOCOMPLETE_ACTION_KEY);
        actionMap.put(ACCEPT_AUTOCOMPLETE_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (autocompletePopup.isShowing()) {
                    acceptSelectedAutocomplete();
                } else {
                    executeDefaultAction(DefaultEditorKit.insertBreakAction, actionMap, e);
                }
            }
        });

        // Alt + 0: Accept first suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_0_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_0_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(0);
            }
        });
        
        // Alt + 1: Accept first suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_1_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_1_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(1);
            }
        });
        
        // Alt + 2: Accept second suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_2_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_2_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(2);
            }
        });
        
        // Alt + 3: Accept third suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_3_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_3_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(3);
            }
        });
        
        // Alt + 4: Accept fourth suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_4_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_4_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(4);
            }
        });
        
        // Alt + 5: Accept fifth suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_5_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_5_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(5);
            }
        });
        
        // Alt + 6: Accept sixth suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_6_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_6_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(6);
            }
        });
        
        // Alt + 7: Accept seventh suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_7_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_7_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(7);
            }
        });
        
        // Alt + 8: Accept eighth suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_8_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_8_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(8);
            }
        });
        
        // Alt + 9: Accept ninth suggestion
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.ALT_DOWN_MASK),
                ACCEPT_SUGGESTION_9_ACTION_KEY);
        actionMap.put(ACCEPT_SUGGESTION_9_ACTION_KEY, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acceptSuggestion(9);
            }
        });
    }

    protected void acceptNextCharacter() {
        moveCaretPosition(getCaretPosition() + 1);
    }

    protected void acceptNextWord() {
        int nextSpaceIndex = getText().indexOf(' ', getCaretPosition());
        int newCaretPosition = nextSpaceIndex == -1 ? getSelectionEnd()
                : Math.min(getSelectionEnd(), nextSpaceIndex + 1);
        moveCaretPosition(newCaretPosition);
    }

    protected void acceptFullSuggestion() {
        moveCaretPosition(getSelectionEnd());
    }

    protected void removeSuggestion() {
        // Use invokeLater to ensure UI updates happen correctly after document
        // changes
        SwingUtilities.invokeLater(() -> {
            filter.setActive(false);
            try {
                replaceSelection("");
            } finally {
                // Crucial: Reset the flag after processing is done
                SwingUtilities.invokeLater(() -> filter.setActive(true));
            }
        });
    }

    // Helper to execute default actions from the component's ActionMap
    private void executeDefaultAction(String actionKey, ActionMap actionMap, ActionEvent e) {
        Action defaultAction = actionMap.get(actionKey);
        if (defaultAction != null) {
            defaultAction
                    .actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    void handleInlineCompletion(String leadingText, String trailingText) {
        int currentCaretPosition = getCaretPosition();

        CompletionItem completionItem = autocompleteService.getCompletion(leadingText,
                trailingText);

        // Insert suggestion at the caret position
        replaceSelection(completionItem.getCompletion());

        // Select the suggestion
        setCaretPosition(currentCaretPosition + completionItem.getCompletion().length());
        moveCaretPosition(currentCaretPosition);
    }

    void handlePopupCompletion(String leadingText, String trailingText) {
        List<? extends AutocompleteItem> completions;
        if (leadingText.isEmpty() && trailingText.isEmpty()) {
            completions = autocompleteService.getSuggestions();
        } else {
            completions = autocompleteService.getCompletions(leadingText, trailingText);
        }
        autocompleteListModel.clear();
        autocompleteListModel.addAll(completions);
        showSuggestionPopup();
    }

    protected void acceptSelectedAutocomplete() {
        int selectedIndex = autocompleteList.getSelectedIndex();
        if (selectedIndex != -1) {
            AutocompleteItem selectedItem = autocompleteListModel
                    .getElementAt(selectedIndex);

            // Use invokeLater to ensure UI updates happen correctly after document
            // changes
            SwingUtilities.invokeLater(() -> {
                filter.setActive(false);
                try {
                    setText(selectedItem.getPlainText());
                    hideSuggestionPopup();
                } finally {
                    // Crucial: Reset the flag after processing is done
                    SwingUtilities.invokeLater(() -> filter.setActive(true));
                }
            });
        }
    }

    private void acceptSuggestion(int index) {
        if (index >= 0 && index < autocompleteListModel.getSize()) {
            AutocompleteItem selectedItem = autocompleteListModel.getElementAt(index);

            if (selectedItem instanceof SuggestionItem) {

                // Use invokeLater to ensure UI updates happen correctly after document changes
                SwingUtilities.invokeLater(() -> {
                    filter.setActive(false);
                    try {
                        setText(selectedItem.getPlainText());
                        hideSuggestionPopup();
                    } finally {
                        // Crucial: Reset the flag after processing is done
                        SwingUtilities.invokeLater(() -> filter.setActive(true));
                    }
                });
            }
        }
    }

    private void showSuggestionPopup() {
        autocompletePopup.pack();
        autocompletePopup.setPopupSize(getWidth(), autocompletePopup.getPreferredSize().height);
        autocompletePopup.show(this, 0, getHeight());
    }

    private void hideSuggestionPopup() {
        autocompletePopup.setVisible(false);
    }

}
