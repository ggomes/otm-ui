package otmui.model;

import error.OTMException;
import keys.KeyCommodityDemandTypeId;
import profiles.DemandProfile;
import profiles.Profile1D;

import java.util.HashMap;
import java.util.Map;

public class DemandsForLink {

    public Long link_id;
    public Map<KeyCommodityDemandTypeId, Profile1D> demands;  // commodity id -> demandsForLink profile

    public DemandsForLink(Long link_id){
        this.link_id = link_id;
        demands = new HashMap<>();
    }

    public void add_profile(DemandProfile dp) throws OTMException {

        // check
        if(!dp.get_origin().getId().equals(link_id))
            throw new OTMException("dp.get_origin().get_link_id()!=link_id");

        KeyCommodityDemandTypeId key = new KeyCommodityDemandTypeId(
                dp.commodity.getId(),dp.get_link_or_path_id(),dp.get_type());

        demands.put(key,dp.profile);
    }

    public Long get_link_id(){
        return link_id;
    }

}
