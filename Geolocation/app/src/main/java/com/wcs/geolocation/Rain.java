
package com.wcs.geolocation;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.Map;

public class Rain {

    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
