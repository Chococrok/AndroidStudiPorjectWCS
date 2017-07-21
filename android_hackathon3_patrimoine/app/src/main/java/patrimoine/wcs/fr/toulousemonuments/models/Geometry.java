
package patrimoine.wcs.fr.toulousemonuments.models;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Geometry {

    @Key
    private String type;
    @Key
    private List<Double> coordinates = null;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
