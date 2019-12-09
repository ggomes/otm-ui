package otmui.model;

import error.OTMException;
import otmui.graph.item.AbstractDrawNode;

public class Actuator {

    public enum Type {signal,stop,other}

    public actuator.AbstractActuator bactuator;
    public AbstractDrawNode drawActuator;
    public Actuator.Type type;

    private Float xcoord;
    private Float ycoord;

    public Actuator(actuator.AbstractActuator bactuator,otmui.model.Network network) throws OTMException {
        this.bactuator = bactuator;

        switch(bactuator.target.getScenarioElementType()){
            case node:
                Node node = network.nodes.get(bactuator.target.getId());
                xcoord = node.getXcoord();
                ycoord = node.getYcoord();
                break;
            default:
                throw new OTMException("Unknown actuator type");
        }

        switch(bactuator.getType()){
            case signal:
                this.type = Type.signal;
                break;
            case stop:
                this.type = Type.stop;
                break;
            default:
                this.type = Type.other;
        }
    }

    public Long getId(){
        return bactuator.getId();
    }

    public Float getXcoord() {
        return xcoord;
    }

    public Float getYcoord() {
        return ycoord;
    }

}
