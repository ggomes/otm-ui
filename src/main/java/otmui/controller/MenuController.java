package otmui.controller;

import api.OTM;
import api.OTMdev;
import common.Node;
import javafx.stage.Modality;
import otmui.Data;
import otmui.MainApp;
import otmui.event.NewScenarioEvent;
import otmui.event.ResetScenarioEvent;
import otmui.simulation.OTMTask;
import otmui.view.WindowParameters;
import otmui.view.WindowPlotRequest;
import otmui.view.FactoryComponent;
import error.OTMException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class MenuController implements Initializable {

    private MainApp myApp;
//
//    private String projectFilePath = null;
//    private File projectFileDir = null;
//    private String optProjectFileDir_String = "optProjectFileDir_String";
//    private Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

    @FXML
    private MenuBar menubar;

//    @FXML
//    private Menu openTest;

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
        disableRun();
        disablePause();
        disableRewind();
        disablePlots();
        disableParameters();
    }

    public void attach_event_listeners(MainApp myApp){
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
            FactoryComponent.exception_dialog(ex).showAndWait();
        }
    }

    @FXML
    private void menuSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save the scenario");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OTM","*.xml"));
        File file = fileChooser.showSaveDialog(null);
        if (file==null)
            return;
        try {
            FileInfo file_info = new FileInfo(file.getAbsolutePath());
            create_marshaller(jaxb.Scenario.class).marshal(myApp.otm.scenario.to_jaxb(), file_info.get_file());
        } catch (Exception ex) {
            otmui.utils.Dialogs.ExceptionDialog("Error saving the scenario.", ex);
        }
    }

    private static Marshaller create_marshaller(Class clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }

    @FXML
    private void menuExit(ActionEvent event) {
        Platform.exit();
    }

    /////////////////////////////////////////////////
    // menu Simulation
    /////////////////////////////////////////////////

    @FXML
    private void menuRun(ActionEvent event) {
        OTMTask otm_task = new OTMTask(myApp.otm,myApp.params,myApp.menuController,myApp.graphController,myApp.statusbarController);
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
    private void menuZoomIn(ActionEvent event) {
        myApp.graphController.graphContainer.scrollPane.zoomIn();
    }

    @FXML
    private void menuZoomOut(ActionEvent event) {
        myApp.graphController.graphContainer.scrollPane.zoomOut();
    }

    @FXML
    private void menuPlots(ActionEvent event) {
        Stage dialog = new Stage();
        dialog.setTitle("Plot requests");
        WindowPlotRequest plotWindow = new WindowPlotRequest(myApp.params);
        Scene dialogScene = new Scene(plotWindow, 500, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void menuParameters(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Global Parameters");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(myApp.stage);
        WindowParameters paramWindow = new WindowParameters(myApp.params);
        Scene scene = new Scene(paramWindow, 500, 400);
        stage.setScene(scene);
        stage.show();
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
    // load
    /////////////////////////////////////////////////

    public void loadFile(String filename) throws OTMException {

        // load the scenario from XML
        try {
            myApp.otm = new OTMdev(new OTM());
            myApp.otm.otm.load(filename,false,false);
            myApp.otm = new OTMdev(myApp.otm.otm);
            if(myApp.otm.scenario==null)
                return;
        } catch (Exception e) {
            throw new OTMException(e);
        }

        // convert units
        if (!myApp.otm.scenario.network.node_positions_in_meters)
            convert_to_meters(myApp.otm);

        myApp.data = new Data(myApp,myApp.params);

        // enable stuff
        enablePlots();
        enableParameters();
        enableRun();

        // Fire new scenario event
        menubar.getScene().getRoot().fireEvent(new NewScenarioEvent(myApp.otm,myApp.data));

    }


    private static void convert_to_meters(OTMdev otm) {

        Collection<Node> nodes = otm.scenario.network.nodes.values();
        Collection<common.Link> links = otm.scenario.network.links.values();

        double R = 6378137.0;  // Radius of Earth in meters
        double conv = Math.PI / 180.0;
        double clat = nodes.stream().mapToDouble(n -> n.ycoord).average().getAsDouble()* conv;
        double clon = nodes.stream().mapToDouble(n -> n.xcoord).average().getAsDouble()* conv;
        double cos2 = Math.pow(Math.cos(clat),2);

        for(common.Node node : nodes){
            double lon = node.xcoord * conv;
            double lat = node.ycoord * conv;
            double dx = Math.acos(1-cos2*(1-Math.cos(lon-clon)))*R;
            node.xcoord = (float) (lon<clon ? -dx : dx);
            node.ycoord = (float) ( (lat - clat) * R );
        }

        for(common.Link link : links){
            for(common.Point point : link.shape){
                double lon = point.x * conv;
                double lat = point.y* conv;
                double dx = Math.acos(1-cos2*(1-Math.cos(lon-clon)))*R;
                point.x = (float) (lon<clon ? -dx : dx);
                point.y = (float) ( (lat - clat) * R );
            }
        }

        otm.scenario.network.node_positions_in_meters = true;
    }

    public static class FileInfo {
        String folder;
        String file_name;
        public FileInfo(String filePath){
            File f = new File(filePath);
            if (f.isDirectory())
                return;
            folder = f.getParent();
            String name = f.getName();
            final int lastPeriodPos = name.lastIndexOf('.');
            if (lastPeriodPos <= 0)
                file_name = name;
            else
                file_name = name.substring(0, lastPeriodPos);
        }
        public File get_file(){
            return new File(folder,file_name + ".xml");
        }
    }

}