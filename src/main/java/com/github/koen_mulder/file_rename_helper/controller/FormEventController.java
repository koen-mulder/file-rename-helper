package com.github.koen_mulder.file_rename_helper.controller;

import java.util.ArrayList;

import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventListener;
import com.github.koen_mulder.file_rename_helper.interfaces.FormEventPublisher;
import com.google.common.collect.Lists;

public class FormEventController implements FormEventPublisher {

    private ArrayList<FormEventListener> formClearListeners = Lists.newArrayList();

    @Override
    public void addFormEventListener(FormEventListener listener) {
        this.formClearListeners.add(listener);
    }

    @Override
    public void removeFormEventListener(FormEventListener listener) {
        this.formClearListeners.remove(listener);
    }

    @Override
    public void notifyFormEventListeners(EFormEvent event) {
        for (FormEventListener listener : formClearListeners) {
            listener.onFormEvent(event);
        }
    }

}
