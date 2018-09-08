package otmui.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Created by gomes on 2/4/2017.
 */
public abstract class AbstractData {

    protected ScrollPane scrollPane;
    protected AnchorPane anchorPane;
    protected VBox vbox;

    public AbstractData(){
        scrollPane = new ScrollPane();

        scrollPane.setMinWidth(0d);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMinHeight(0d);
        scrollPane.setMaxHeight(Double.MAX_VALUE);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;");

        AnchorPane.setRightAnchor(scrollPane,0d);
        AnchorPane.setLeftAnchor(scrollPane,0d);
        AnchorPane.setTopAnchor(scrollPane,0d);
        AnchorPane.setBottomAnchor(scrollPane,0d);

        // anchorpane, add to scrollPane
        anchorPane = new AnchorPane();
        scrollPane.setContent(anchorPane);

        anchorPane.setMinWidth(0d);
        anchorPane.setMaxWidth(Double.MAX_VALUE);
        anchorPane.setMinHeight(0d);
        anchorPane.setMaxHeight(Double.MAX_VALUE);

        AnchorPane.setRightAnchor(anchorPane,0d);
        AnchorPane.setLeftAnchor(anchorPane,0d);
        AnchorPane.setTopAnchor(anchorPane,0d);
        AnchorPane.setBottomAnchor(anchorPane,0d);

        // vbox, add to anchorPane
        vbox = new VBox();
        anchorPane.getChildren().add(vbox);

        vbox.setFillWidth(true);
        vbox.setMinWidth(0d);
        vbox.setMaxWidth(Double.MAX_VALUE);
        vbox.setMinHeight(0d);
        vbox.setMaxHeight(Double.MAX_VALUE);
        AnchorPane.setRightAnchor(vbox,0d);
        AnchorPane.setLeftAnchor(vbox,0d);
        AnchorPane.setTopAnchor(vbox,0d);
        AnchorPane.setBottomAnchor(vbox,0d);
    }

    public ScrollPane getScrollPane(){
        return this.scrollPane;
    }
}
