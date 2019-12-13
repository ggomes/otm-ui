package otmui.graph;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;
import otmui.Data;

public class ZoomableScrollPane extends ScrollPane {

    // this contains contentGroup contains zoomGroup contains content
    public Group zoomGroup;
    public Scale scaleTransform;
    public Node content;
    public double scaleValue = 1.0;
    public double delta = 1.1;

    public ZoomableScrollPane(Node content) {
        this.content = content;
        Group contentGroup = new Group();

        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);

        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
        zoomGroup.getTransforms().add(scaleTransform);

        setPannable(true);
        addEventFilter(ScrollEvent.ANY, new ZoomHandler());

    }

    /** scale / zoom **/

    public double getScaleValue() {
        return scaleValue;
    }

    public void zoomTo(double scaleValue) {
        double hvalue = getHvalue();
        double vvalue = getVvalue();

        this.scaleValue = scaleValue;
        scaleTransform.setX(scaleValue);
        scaleTransform.setY(scaleValue);

        setHvalue(hvalue);
        setVvalue(vvalue);
    }

    public void zoomOut() {
        zoomTo(scaleValue/delta);
    }

    public void zoomIn() {
        zoomTo(scaleValue*delta);
    }

    public void zoomToFit(Data data) {
        double scaleX = 0.95*getViewportBounds().getWidth() / data.getWidth();
        double scaleY = 0.95*getViewportBounds().getHeight() / data.getHeight();
        zoomTo(Math.min(scaleX, scaleY));
    }

    private class ZoomHandler implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown()) {
                if(scrollEvent.getDeltaY() < 0)
                    zoomOut();
                else if(scrollEvent.getDeltaY() > 0)
                    zoomIn();
                scrollEvent.consume();
            }
        }
    }
}