package com.github.koen_mulder.file_rename_helper.renaming.ui.autocomplete;

import java.util.Objects;

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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CompletionItem other = (CompletionItem) obj;
        return Objects.equals(completion, other.completion)
                && Objects.equals(leadingText, other.leadingText)
                && Objects.equals(trailingText, other.trailingText);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(completion, leadingText, trailingText);
    }
}
