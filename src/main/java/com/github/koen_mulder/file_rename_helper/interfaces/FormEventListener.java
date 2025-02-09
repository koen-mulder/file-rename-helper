/**
 * 
 */
package com.github.koen_mulder.file_rename_helper.interfaces;

import com.github.koen_mulder.file_rename_helper.gui.EFormEvent;

/**
 * The FormEventListener interface is implemented by components that should
 * respond to form events. Form events, like enable/disable/clear, are published by a
 * {@link FormEventPublisher}.
 */
public interface FormEventListener {
    
    /**
     * This method will be called when there is an event the component respond to.
     * 
     * @param event to handle
     */
    void onFormEvent(EFormEvent event);

}
