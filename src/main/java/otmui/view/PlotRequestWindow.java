/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.view;

import otmui.GlobalParameters;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PlotRequestWindow extends VBox {

//    private org.controlsfx.control.PropertySheet propertySheet;

//    class CustomPropertyItem implements org.controlsfx.control.PropertySheet.Item {
//
//        private String key;
//        private String category, name;
//        private Property property;
//
//        public CustomPropertyItem(String key,Property property) {
//            this.key = key;
//            String[] skey = key.split("#");
//            this.category = skey[0];
//            this.name = skey[1];
//            this.property = property;
//        }
//
//        @Override
//        public Class<?> getType() {
//            return Float.class;
//        }
//
//        @Override
//        public String getCategory() {
//            return category;
//        }
//
//        @Override
//        public String getName() {
//            return name;
//        }
//
//        @Override
//        public String getDescription() {
//            return null;
//        }
//
//        @Override
//        public Object getValue() {
//            return  property.getValue();
//        }
//
//        @Override
//        public void setValue(Object value) {
//            property.setValue(value);
//        }
//
//        @Override
//        public Optional<ObservableValue<? extends Object>> getObservableValue() {
//            return Optional.of(property);
//        }
//
//        @Override
//        public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
//            return Optional.empty();
//        }
//
//        @Override
//        public boolean isEditable() {
//            return true;
//        }
//    }

//    public ObservableList<PropertySheet.Item> getItems(){
//        return propertySheet.getItems();
//    }

    public PlotRequestWindow(GlobalParameters params) {

        try {
            FXMLLoader scenarioTreeLoader = new FXMLLoader(getClass().getResource("/fxml/plotrequestDialog.fxml"));
            AnchorPane scenarioTreePane = scenarioTreeLoader.load();
            getChildren().add(scenarioTreePane);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        scenarioTreeController = scenarioTreeLoader.getController();
//        scenarioTreeController.setApp(this);
//        splitPaneH.getItems().add(scenarioTreePane);

//        ObservableList<org.controlsfx.control.PropertySheet.Item> list = FXCollections.observableArrayList();
//
//        list.add(new CustomPropertyItem("Simulation#Time step [seconds]", params.sim_dt));
//        list.add(new CustomPropertyItem("Simulation#Start time [seconds]",params.start_time));
//        list.add(new CustomPropertyItem("Simulation#Duration [seconds]",params.duration));
//        list.add(new CustomPropertyItem("Simulation#Delay [ms]",params.sim_delay));
//
//        list.add(new CustomPropertyItem("Display#Lane width [m]",params.lane_width_meters));
//        list.add(new CustomPropertyItem("Display#Lane offset [m]",params.link_offset));
//        list.add(new CustomPropertyItem("Display#Node radius [m]",params.node_size));
//        list.add(new CustomPropertyItem("Display#Color scheme",params.color_map));
//        list.add(new CustomPropertyItem("Display#Max MN density [veh/km/lane]",params.max_density_vpkpl));
//
//        propertySheet = new org.controlsfx.control.PropertySheet(list);
//        propertySheet.setPropertyEditorFactory(new Callback<PropertySheet.Item, PropertyEditor<?>>() {
//            @Override
//            public PropertyEditor<?> call(PropertySheet.Item param) {
//                if(param.getValue() instanceof GlobalParameters.ColorScheme) {
//                    return Editors.createChoiceEditor(param, new ArrayList<>(Arrays.asList(GlobalParameters.ColorScheme.values())) );               } else
//                if (param.getValue() instanceof Boolean) {
//                    return Editors.createCheckEditor(param);
//                } else if (param.getValue() instanceof Integer) {
//                    return Editors.createNumericEditor(param);
//                } else if (param.getValue() instanceof Float) {
//                    return Editors.createNumericEditor(param);
//                } else {
//                    return Editors.createTextEditor(param);
//                }
//            }
//        });
//
//        VBox.setVgrow(propertySheet, Priority.ALWAYS);
//        getChildren().add(propertySheet);
    }

}
