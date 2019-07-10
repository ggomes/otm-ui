package otmui.graph;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;

public class ZoomableScrollPane2 extends ScrollPane {

    public double scaleValue = 1.0;
    public double delta = 1.1;
    Group zoomGroup;

    public ZoomableScrollPane2(Node content) {
//        this.content = content;
        Group contentGroup = new Group();

        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);

        zoomGroup.getChildren().add(content);
        setContent(contentGroup);
//        scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
//        zoomGroup.getTransforms().add(scaleTransform);

//        translateTransform = new Translate();
//        contentGroup.getTransforms().add(translateTransform);

        setPannable(true);
        addEventFilter(ScrollEvent.ANY, new ZoomHandler());
    }

    public double getScaleValue(){
        return getScaleX();
    }

    private class ZoomHandler implements EventHandler<ScrollEvent> {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            if (scrollEvent.isControlDown()) {

                double scaleFactor = scrollEvent.getDeltaY() < 0 ?
                        1/delta : delta;
                zoomGroup.setScaleX( zoomGroup.getScaleX() * scaleFactor);
                zoomGroup.setScaleY( zoomGroup.getScaleY() * scaleFactor);
                scrollEvent.consume();
            }
        }
    }
}
