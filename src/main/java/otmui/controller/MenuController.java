package otmui.controller;

import otmui.MainApp;
import otmui.event.NewScenarioEvent;
import otmui.event.ResetScenarioEvent;
import otmui.model.*;
import otmui.simulation.OTMTask;
import otmui.view.ParametersWindow;
import otmui.view.PlotRequestWindow;
import otmui.view.UIFactory;
import error.OTMException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xml.JaxbLoader;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private MainApp myApp;
    private static Map<String,String> testFiles = new HashMap<>();

    static {
        for(String testname : JaxbLoader.get_test_config_names())
            testFiles.put(testname,JaxbLoader.get_test_fullpath(testname));
    }

    @FXML
    private MenuBar menubar;

    @FXML
    private Menu openTest;

    @FXML
    private MenuItem menuRun;

    @FXML
    private MenuItem menuPause;

    @FXML
    private MenuItem menuRewind;

    @FXML
    private MenuItem menuParameters;

    @FXML
    private MenuItem menuPlots;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(Map.Entry<String,String> r : testFiles.entrySet()){
            MenuItem x = new MenuItem(r.getKey());
            x.setOnAction(e->menuOpenTest(e));
            openTest.getItems().add(x);
        }
        disableRun();
        disablePause();
        disableRewind();
        disablePlots();
        disableParameters();
    }

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

    /////////////////////////////////////////////////
    // menu File
    /////////////////////////////////////////////////

    @FXML
    private void menuOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(null);
        if (file==null)
            return;
        try {
            loadFile(file.getAbsolutePath());
        } catch (OTMException ex) {
            UIFactory.createExceptionDialog(ex).showAndWait();
        }
    }

    @FXML
    private void menuOpenTest(ActionEvent event) {
        String item = ((MenuItem) event.getSource()).getText();
        if(!testFiles.containsKey(item))
            return;
        try {
            loadTest(item);
        } catch (OTMException ex) {
            UIFactory.createExceptionDialog(ex).showAndWait();
        }
    }

    @FXML
    private void menuExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void menuAction(ActionEvent event) {

    }

    /////////////////////////////////////////////////
    // menu Simulation
    /////////////////////////////////////////////////

    @FXML
    private void menuRun(ActionEvent event) {
        OTMTask otm_task = new OTMTask(myApp.otm,myApp.params,myApp.menuController,myApp.graphpaneController,myApp.statusbarController);
        myApp.statusbarController.bind_progress(otm_task.progressProperty());
        myApp.statusbarController.bind_text(otm_task.messageProperty());
        new Thread(otm_task).start();
    }

    @FXML
    private void menuPause(ActionEvent event) {
        System.err.println("Pause not implemented!");
    }

    @FXML
    private void menuRewind(ActionEvent event) {
        menubar.getScene().getRoot().fireEvent(new ResetScenarioEvent());
        myApp.menuController.disableRewind();
        myApp.menuController.enableRun();
    }

    /////////////////////////////////////////////////
    // menu View
    /////////////////////////////////////////////////

    @FXML
    private void menuPlots(ActionEvent event) {
        Stage dialog = new Stage();
        dialog.setTitle("Plot requests");
        PlotRequestWindow plotWindow = new PlotRequestWindow(myApp.params);
        Scene dialogScene = new Scene(plotWindow, 500, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void menuParameters(ActionEvent event) {
        Stage dialog = new Stage();
        dialog.setTitle("Global Parameters");
        ParametersWindow paramWindow = new ParametersWindow(myApp.params);
        Scene dialogScene = new Scene(paramWindow, 500, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void menuVersion(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Version");
        alert.setHeaderText(null);
        String str =
                "UI : " + MainApp.getGitHash() + "\n" +
                "Simulator : " + myApp.otm.otm.get_version();
        alert.setContentText(str);
        alert.showAndWait();
    }

    /////////////////////////////////////////////////
    // get / set
    /////////////////////////////////////////////////

    public void enableRun(){ menuRun.setDisable(false); }
    public void disableRun(){ menuRun.setDisable(true); }
    public void enablePause(){ menuPause.setDisable(false); }
    public void disablePause(){ menuPause.setDisable(true); }
    public void enableRewind(){ menuRewind.setDisable(false); }
    public void disableRewind(){ menuRewind.setDisable(true); }
    public void enableParameters(){ menuParameters.setDisable(false); }
    public void disableParameters(){ menuParameters.setDisable(true); }
    public void enablePlots(){ menuPlots.setDisable(false); }
    public void disablePlots(){ menuPlots.setDisable(true); }

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private void loadFile(String filename) throws OTMException {

        boolean validate = false;

        // load the scenario from XML
        try {
            myApp.otm.otm.load(filename,validate,false);
        } catch (Exception e) {
            throw new OTMException(e);
        }

        // check
        if(myApp.otm.scenario==null)
            return;

        Scenario scenario = new Scenario(myApp.otm);

        if(scenario!=null) {
            enablePlots();
            enableParameters();
            enableRun();
            menubar.getScene().getRoot().fireEvent(new NewScenarioEvent(scenario));
        }
    }

    private void loadTest(String testname) throws OTMException {

        boolean validate = false;

        // TODO PUT THIS BACK
//        // load the scenario from XML
//        try {
//            myApp.otm.otm.load_test(testname+".xml",validate);
//        } catch (Exception e) {
//            throw new OTMException(e);
//        }

        // check
        if(myApp.otm.scenario==null)
            return;

        Scenario scenario = new Scenario(myApp.otm);

        if(scenario!=null) {
            enablePlots();
            enableParameters();
            enableRun();
            menubar.getScene().getRoot().fireEvent(new NewScenarioEvent(scenario));
        }
    }

}