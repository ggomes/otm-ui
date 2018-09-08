package otmui.controller;

import otmui.MainApp;
import otmui.event.ChangeScenarioEvent;
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

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private MainApp myApp;
    private static Map<String,String> recentFiles = new HashMap<>();

    static {
        recentFiles.put("seven links pq","C:\\Users\\gomes\\code\\ta_solver\\configfiles\\seven_links_pq.xml");
        recentFiles.put("seven links mn","C:\\Users\\gomes\\code\\ta_solver\\configfiles\\seven_links_mn.xml");
        recentFiles.put("test net 4"    ,"C:\\Users\\gomes\\code\\otmui\\data\\config\\cfg_net4.xml");
        recentFiles.put("L1 estim"      ,"C:\\Users\\gomes\\code\\aimsun_extract_freeway\\output\\L0_scenario_v22.xml");
        recentFiles.put("aimsun full"   ,"C:\\Users\\gomes\\Dropbox\\gabriel\\work\\aimsun2xml_2017_11_07\\beats_scenario_fixed.xml");
        recentFiles.put("intersection"   ,"C:\\Users\\gomes\\Dropbox\\gabriel\\work\\otm\\beats_share\\intersection.xml");
    }

    @FXML
    private MenuBar menubar;

    @FXML
    private Menu openRecent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(Map.Entry<String,String> r : recentFiles.entrySet()){
            MenuItem x = new MenuItem(r.getKey());
            x.setOnAction(e->menuOpenRecent(e));
            openRecent.getItems().add(x);
        }
    }

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

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
    private void menuExit(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void menuAction(ActionEvent event) {
        System.out.println("\n");
    }

    @FXML
    private void menuOpenRecent(ActionEvent event) {
        String item = ((MenuItem) event.getSource()).getText();
        if(!recentFiles.containsKey(item))
            return;
        try {
            loadFile(recentFiles.get(item));
        } catch (OTMException ex) {
            UIFactory.createExceptionDialog(ex).showAndWait();
        }
    }

    @FXML
    private void menuRun(ActionEvent event) {
        OTMTask beats_task = new OTMTask(myApp.otm,myApp.params,myApp);
        myApp.statusbarController.bind_progress(beats_task.progressProperty());
        myApp.statusbarController.bind_text(beats_task.messageProperty());
        new Thread(beats_task).start();
    }

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
                "Simulator : " + myApp.otm.api.get_version();
        alert.setContentText(str);
        alert.showAndWait();
    }

    private void loadFile(String filename) throws OTMException {

        // TEMPORARY!!! DONT VALIDATE THE INPUT FILE
        boolean validate = false;

        // load the scenario from XML
        try {
            myApp.otm.api.load(filename,myApp.params.sim_dt.floatValue(),validate);
        } catch (Exception e) {
            throw new OTMException(e);
        }

        // check
        if(myApp.otm.scenario()==null)
            return;

        Scenario scenario = new Scenario(myApp.otm);

        if(scenario!=null)
            menubar.getScene().getRoot().fireEvent(new ChangeScenarioEvent(scenario));
    }

}