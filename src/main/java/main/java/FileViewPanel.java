package main.java;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.util.FontPropertiesManager;
import org.icepdf.ri.util.ViewerPropertiesManager;

import main.java.interfaces.FileSelectionListener;

public class FileViewPanel extends JPanel implements FileSelectionListener {

    private static final long serialVersionUID = 294107391689362780L;
    
    private SwingController controller;

    public FileViewPanel(JFrame applicationFrame) {
        // build a component controller     
        controller = new SwingController();
        controller.setIsEmbeddedComponent(true);

        // Read stored system font properties.
        FontPropertiesManager.getInstance().loadOrReadSystemFonts();

        ViewerPropertiesManager properties = ViewerPropertiesManager.getInstance();

        // Clear preferences
        // Preferences preferences = properties.getPreferences();
        // properties.clearPreferences();

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
    }

    @Override
    public void onFileSelected(String filePath) {
        controller.openDocument(filePath);
        //TODO: Set by user preferred zoom - Current PAGE_FIT_WINDOW_HEIGHT is bugged in IcePDF 
        controller.setPageFitMode(DocumentViewController.PAGE_FIT_WINDOW_HEIGHT, true);
    }
}
