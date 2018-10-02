/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui;

import api.API;
import api.APIopen;
import otmui.controller.*;
import otmui.event.*;
import otmui.model.Scenario;
import otmui.simulation.OTMTask;
import error.OTMException;
import javafx.application.Application;
import javafx.event.Event;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainApp extends Application {

    public APIopen otm;  // runnable OTM scenario.

    public Stage stage;

    public Scenario scenario;
    public SelectionManager selectionManager;
    public MenuController menuController;
    public StatusBarController statusbarController;
    public TreeController scenarioTreeController;
    public GraphPaneController graphpaneController;
    public DataPaneController datapaneController;

    public GlobalParameters params;

    /////////////////////////////////////////////////
    // launch
    /////////////////////////////////////////////////

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        // construct the OTM api .....................
        this.otm = new APIopen(new API());

        // UI ...........................................
        selectionManager = new SelectionManager(this);

        BorderPane layout = new BorderPane();

        // add menu
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/menuLayout.fxml"));

        AnchorPane menuPane = menuLoader.load();
        menuController = menuLoader.getController();
        layout.setTop(menuPane.lookup("#menubar"));
        menuController.setApp(this);

        // add status bar
        StatusBar statusBar = new StatusBar();
        statusbarController = new StatusBarController(statusBar,this);
        layout.setBottom(statusBar);

        // add splitpane
        SplitPane splitPaneH = new SplitPane();
        splitPaneH.setOrientation(Orientation.HORIZONTAL);
        layout.setCenter(splitPaneH);

        // add scenario tree
        FXMLLoader scenarioTreeLoader = new FXMLLoader(getClass().getResource("/fxml/treeLayout.fxml"));
        AnchorPane scenarioTreePane = scenarioTreeLoader.load();
        scenarioTreeController = scenarioTreeLoader.getController();
        scenarioTreeController.setApp(this);
        splitPaneH.getItems().add(scenarioTreePane);

        // add vertical splitsForNode pane
        SplitPane splitPaneV = new SplitPane();
        splitPaneV.setOrientation(Orientation.VERTICAL);
        splitPaneH.getItems().add(splitPaneV);

        // add graph
        FXMLLoader graphPaneLoader = new FXMLLoader(getClass().getResource("/fxml/graphLayout.fxml"));
        AnchorPane graphPane = graphPaneLoader.load();
        graphpaneController = graphPaneLoader.getController();
        graphpaneController.setApp(this);
        splitPaneV.getItems().add(graphPane);

        // add data pane
        FXMLLoader dataPaneLoader = new FXMLLoader(getClass().getResource("/fxml/dataLayout.fxml"));
        AnchorPane dataPane = dataPaneLoader.load();
        datapaneController = dataPaneLoader.getController();
        datapaneController.setApp(this);
        splitPaneV.getItems().add(dataPane);

        // put into a scene and show
        Scene scene = new Scene(layout);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setTitle("OTM Browser");
        stage.setScene(scene);

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
        stage.setMaximized(true);

        stage.show();

        // Event filters and handlers .......................

        // loading new scenario
        scene.addEventFilter(ChangeScenarioEvent.LOADED,e->processNewScenario(e.scenario) );

        // graph, link or node click
        scene.addEventFilter(GraphSelectEvent.CLICK1_NODE, e->selectionManager.graphFirstClickNode(e));
        scene.addEventFilter(GraphSelectEvent.CLICK2_NODE, e->selectionManager.graphSecondClickNode(e));
        scene.addEventFilter(GraphSelectEvent.CLICK1_LINK, e->selectionManager.graphFirstClickLink(e));
        scene.addEventFilter(GraphSelectEvent.CLICK2_LINK, e->selectionManager.graphSecondClickLink(e));
        scene.addEventFilter(GraphSelectEvent.CLICK1_SENSOR, e->selectionManager.graphFirstClickSensor(e));
        scene.addEventFilter(GraphSelectEvent.CLICK2_SENSOR, e->selectionManager.graphSecondClickSensor(e));

        // tree, setText
        scene.addEventFilter(TreeSelectEvent.CLICK1, e->selectionManager.treeFirstClick(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_LINK,e->selectionManager.treeSecondClickLink(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_COMMODITY,e->selectionManager.treeSecondClickCommodity(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_SUBNETWORK,e->selectionManager.treeSecondClickSubnetwork(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_DEMAND,e->selectionManager.treeSecondClickDemand(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_SPLIT,e->selectionManager.treeSecondClickSplit(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_ACTUATOR,e->selectionManager.treeSecondClickActuator(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_CONTROLLER,e->selectionManager.treeSecondClickController(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_SENSOR,e->selectionManager.treeSecondClickSensor(e));

        // data forms
        scene.addEventFilter(FormSelectEvent.CLICK1_LINK, e->selectionManager.formFirstClickLink(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_NODE, e->selectionManager.formFirstClickNode(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_SUBNETWORK, e->selectionManager.formFirstClickSubnetwork(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_COMMODITY, e->selectionManager.formFirstClickCommodity(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_DEMAND, e->selectionManager.formFirstClickDemand(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_SPLIT, e->selectionManager.formFirstClickSplit(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_ACTUATOR, e->selectionManager.formFirstClickActuator(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_CONTROLLER, e->selectionManager.formFirstClickController(e.itemId));
        scene.addEventFilter(FormSelectEvent.CLICK1_SENSOR, e->selectionManager.formFirstClickSensor(e.itemId));

        scene.addEventFilter(FormSelectEvent.CLICK2_LINK, e->selectionManager.formSecondClickLink(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_NODE, e->selectionManager.formSecondClickNode(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_SUBNETWORK, e->selectionManager.formSecondClickSubnetwork(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_COMMODITY, e->selectionManager.formSecondClickCommodity(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_DEMAND, e->selectionManager.formSecondClickDemand(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_SPLIT, e->selectionManager.formSecondClickSplit(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_ACTUATOR, e->selectionManager.formSecondClickActuator(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_CONTROLLER, e->selectionManager.formSecondClickController(e));
        scene.addEventFilter(FormSelectEvent.CLICK2_SENSOR, e->selectionManager.formSecondClickSensor(e));

        // parameter changes
        scene.addEventFilter(ParameterChange.SIMULATION,e->System.out.println("Simulation parameter changed"));
        scene.addEventFilter(ParameterChange.DISPLAY,e->Event.fireEvent(scene,new ChangeScenarioEvent(scenario)));
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

    private void processNewScenario(Scenario scenario)  {
        if(scenario==null)
            return;
        try {
            this.scenario = scenario;
            scenarioTreeController.loadScenario(scenario);
            graphpaneController.loadScenario(scenario);
            datapaneController.loadScenario(scenario);
            statusbarController.loadScenario(scenario);
        } catch (OTMException e) {
            e.printStackTrace();
        }
    }

}
