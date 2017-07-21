
package com.wcs.geolocation;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.Map;

public class Sys {

    @Key
    private String pod;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
