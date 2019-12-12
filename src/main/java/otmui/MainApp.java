package otmui;

import api.OTMdev;
import api.info.DemandInfo;
import api.info.SplitInfo;
import otmui.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;
import otmui.item.AbstractItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MainApp extends Application {

    // OTM holds the runnable scenario
    public OTMdev otm;  // runnable OTM scenario.

    // app data
    public Map<Long,Set<DemandInfo>> demands;
    public Map<Long,Set<SplitInfo>> splits;
    public ItemPool itempool;

    public Stage stage;
    public SelectionManager selectionManager;


    public MenuController menuController;
    public StatusBarController statusbarController;
    public TreeController treeController;
    public GraphPaneController graphpaneController;
    public DataPaneController datapaneController;

    public GlobalParameters params;

    /////////////////////////////////////////////////
    // launch
    /////////////////////////////////////////////////

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        // Assemble the UI elements
        build_ui();

        // Create an OTM object
        Parameters args = getParameters();
        File configfile = args.getUnnamed().size()>0 ? new File(args.getUnnamed().get(0)) : null;
        if (configfile!=null && configfile.exists())
            menuController.loadFile(configfile.getAbsolutePath());

        graphpaneController.merge_nodes();
    }

    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////

    public static String getGitHash(){
        InputStream inputStream = MainApp.class.getResourceAsStream("/sim.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read properties file", e);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
        return properties.getProperty("sim.git");
    }

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private void build_ui() throws IOException {

        // Layout .......................................
        BorderPane layout = new BorderPane();

        // put into a scene and show ........................
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("Open Traffic Modeller");
        stage.setScene(scene);

        SplitPane splitPaneH = new SplitPane();
        splitPaneH.setOrientation(Orientation.HORIZONTAL);
        layout.setCenter(splitPaneH);

        // Selection manager ............................
        selectionManager = new SelectionManager();
        selectionManager.attach_event_listeners(this);

        // Menu ..........................................
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/menuLayout.fxml"));
        AnchorPane menuPane = menuLoader.load();
        menuController = menuLoader.getController();
        layout.setTop(menuPane.lookup("#menubar"));
        menuController.attach_event_listeners(this);

        // Status bar ....................................
        StatusBar statusBar = new StatusBar();
        statusbarController = new StatusBarController(statusBar);
        layout.setBottom(statusBar);
        statusbarController.attach_event_listeners(this);

        // Scenario tree ................................
        FXMLLoader scenarioTreeLoader = new FXMLLoader(getClass().getResource("/fxml/treeLayout.fxml"));
        AnchorPane scenarioTreePane = scenarioTreeLoader.load();
        splitPaneH.getItems().add(scenarioTreePane);
        treeController = scenarioTreeLoader.getController();
        treeController.attach_event_listeners(this);

        // Graph .........................................
        SplitPane splitPaneV = new SplitPane();
        splitPaneV.setOrientation(Orientation.VERTICAL);
        splitPaneH.getItems().add(splitPaneV);

        FXMLLoader graphPaneLoader = new FXMLLoader(getClass().getResource("/fxml/graphLayout.fxml"));
        AnchorPane graphPane = graphPaneLoader.load();
        splitPaneV.getItems().add(graphPane);
        graphpaneController = graphPaneLoader.getController();
        graphpaneController.attach_event_listeners(this);

        // Data ...........................................
        FXMLLoader dataPaneLoader = new FXMLLoader(getClass().getResource("/fxml/dataLayout.fxml"));
        AnchorPane dataPane = dataPaneLoader.load();
        splitPaneV.getItems().add(dataPane);
        datapaneController = dataPaneLoader.getController();
        datapaneController.attach_event_listeners(this);

        // create parameters
        params = new GlobalParameters(scene);

        // stage position
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        double H = primScreenBounds.getHeight();
        double W = primScreenBounds.getWidth();
        stage.setHeight(0.8*H);
        stage.setWidth(0.8*W);
        stage.setX(0.1*W);
        stage.setY(0.1*H);
//        stage.setMaximized(true);

        stage.show();
    }


//    private void reset(){
//        if(otm.scenario==null)
//            return;
////        try {
////            scenario.reset();
//            treeController.reset();
//            graphpaneController.reset();
//            datapaneController.reset();
//            statusbarController.reset();
////        } catch (OTMException e) {
////            e.printStackTrace();
////        }
//    }

}
