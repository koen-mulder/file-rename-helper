package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

public class CompletionItem implements AutocompleteItem {

    private String leadingText;
    private String completion;
    private String trailingText;

    public CompletionItem(String leadingText, String completion, String trailingText) {
        this.leadingText = leadingText;
        this.completion = completion;
        this.trailingText = trailingText;
    }

    public String getLeadingText() {
        return leadingText;
    }

    public String getCompletion() {
        return completion;
    }

    public String getTrailingText() {
        return trailingText;
    }

    @Override
    public String getPlainText() {
        return leadingText + completion + trailingText;
    }
}
