/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;

/**
 * The FormEventPublisher interface is implemented by components that tell other
 * components there is an event they should respond to.
 */
public interface FormEventPublisher {
    /**
     * Adds a listener that will be notified of an event.
     * 
     * @param listener The listener to be added.
     */
    void addFormEventListener(FormEventListener listener);

    /**
     * Removes a previously added listener.
     * 
     * @param listener The listener to be removed.
     */
    void removeFormEventListener(FormEventListener listener);

    /**
     * Notifies all registered listeners of the form event.
     * 
     * @param event the listener should respond to.
     */
    void notifyFormEventListeners(EFormEvent event);
}
