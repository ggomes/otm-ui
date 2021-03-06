package otmui.view;

import otmui.GlobalParameters;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.Editors;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.*;

public class WindowParameters extends VBox {

    private org.controlsfx.control.PropertySheet propertySheet;

    class CustomPropertyItem implements org.controlsfx.control.PropertySheet.Item {

        private String key;
        private String category, name;
        private Property property;

        public CustomPropertyItem(String key,Property property) {
            this.key = key;
            String[] skey = key.split("#");
            this.category = skey[0];
            this.name = skey[1];
            this.property = property;
        }

        @Override
        public Class<?> getType() {
            return Float.class;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDescription() {
            return null;
        }

        @Override
        public Object getValue() {
            return  property.getValue();
        }

        @Override
        public void setValue(Object value) {
           property.setValue(value);
        }

        @Override
        public Optional<ObservableValue<? extends Object>> getObservableValue() {
            return Optional.of(property);
        }

        @Override
        public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
            return Optional.empty();
        }

        @Override
        public boolean isEditable() {
            return true;
        }
    }

    public ObservableList<PropertySheet.Item> getItems(){
        return propertySheet.getItems();
    }

    public WindowParameters(GlobalParameters params) {
        ObservableList<org.controlsfx.control.PropertySheet.Item> list = FXCollections.observableArrayList();

        list.add(new CustomPropertyItem("Simulation#Start time [seconds]",params.start_time));
        list.add(new CustomPropertyItem("Simulation#Duration [seconds]",params.duration));
        list.add(new CustomPropertyItem("Simulation#Delay [ms]",params.sim_delay));

        list.add(new CustomPropertyItem("DrawLinks#Lane width [m]",params.lane_width_meters));
        list.add(new CustomPropertyItem("DrawLinks#Lane offset [m]",params.link_offset));
        list.add(new CustomPropertyItem("DrawNodes#Node size [m]",params.node_size));
        list.add(new CustomPropertyItem("DrawLinks#Color scheme",params.road_color_scheme));
        list.add(new CustomPropertyItem("DrawLinks#Max density [veh/km/lane]",params.max_density_vpkpl));

        list.add(new CustomPropertyItem("DrawNodes#Show nodes",params.view_nodes));
        list.add(new CustomPropertyItem("DrawActuators#Show actuators",params.view_actuators));

        propertySheet = new org.controlsfx.control.PropertySheet(list);
        propertySheet.setPropertyEditorFactory(new Callback<PropertySheet.Item, PropertyEditor<?>>() {
            @Override
            public PropertyEditor<?> call(PropertySheet.Item param) {
                if(param.getValue() instanceof GlobalParameters.RoadColorScheme) {
                    return Editors.createChoiceEditor(param, new ArrayList<>(Arrays.asList(GlobalParameters.RoadColorScheme.values())) );               } else
                    if (param.getValue() instanceof Boolean) {
                    return Editors.createCheckEditor(param);
                } else if (param.getValue() instanceof Integer) {
                    return Editors.createNumericEditor(param);
                    } else if (param.getValue() instanceof Float) {
                        return Editors.createNumericEditor(param);
                } else {
                    return Editors.createTextEditor(param);
                }
            }
        });

        VBox.setVgrow(propertySheet, Priority.ALWAYS);
        getChildren().add(propertySheet);
    }

}