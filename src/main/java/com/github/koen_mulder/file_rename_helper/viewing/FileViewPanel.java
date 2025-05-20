package com.github.koen_mulder.file_rename_helper.viewing;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.ViewerPropertiesManager;

import com.github.koen_mulder.file_rename_helper.processing.FileProcessingItem;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionListener;
import com.github.koen_mulder.file_rename_helper.processing.api.IOpenFileActionPublisher;

@SuppressWarnings("serial") // Same-version serialization only
public class FileViewPanel extends JPanel {

    // TODO: The SwingController should be a singleton that is injected
    private static final SwingController controller = new SwingController();

    public FileViewPanel(IOpenFileActionPublisher openFileActionPublisher, JFrame applicationFrame) {
        // build a component controller     
        controller.setIsEmbeddedComponent(true);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        // Read stored system font properties.
        FontPropertiesManager.getInstance().loadOrReadSystemFonts();

        ViewerPropertiesManager properties = ViewerPropertiesManager.getInstance();
        properties.setFloat(ViewerPropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, 1.25f);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_UTILITY, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_PAGENAV, true);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_ZOOM, true);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_FIT, true);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_FULL_SCREEN, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_ROTATE, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_TOOL, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_ANNOTATION, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_FORMS, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_TOOLBAR_SEARCH, false);
        properties.setBoolean(ViewerPropertiesManager.PROPERTY_SHOW_STATUSBAR, true);
        controller.setToolBarVisible(false);

        SwingViewBuilder factory = new SwingViewBuilder(controller, properties);

        JPanel viewerComponentPanel = factory.buildViewerPanel();
        add(viewerComponentPanel);

        // Add the window event callback to dispose the controller and
        // currently open document.
        applicationFrame.addWindowListener(controller);
        
        addOpenFileActionListener(openFileActionPublisher);
    }
    
    private void addOpenFileActionListener(IOpenFileActionPublisher openFileActionPublisher) {
        openFileActionPublisher.addOpenFileActionListener(new IOpenFileActionListener() {
            
            @Override
            public void onOpenFileAction(FileProcessingItem fileItem) {
                controller.closeDocument();
                if (fileItem != null) {
                    controller.openDocument(fileItem.getTemporaryFilePath().toFile().getAbsolutePath());
                    //TODO: Set by user preferred zoom - Current PAGE_FIT_WINDOW_HEIGHT is bugged in IcePDF 
                    controller.setPageFitMode(DocumentViewController.PAGE_FIT_WINDOW_HEIGHT, true);
                }
            }
        });
    }
}
