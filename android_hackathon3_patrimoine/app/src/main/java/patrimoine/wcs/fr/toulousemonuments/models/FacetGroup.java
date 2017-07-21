
package patrimoine.wcs.fr.toulousemonuments.models;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacetGroup {

    @Key
    private String name;
    @Key
    private List<Facet> facets = null;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Facet> getFacets() {
        return facets;
    }

    public void setFacets(List<Facet> facets) {
        this.facets = facets;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
