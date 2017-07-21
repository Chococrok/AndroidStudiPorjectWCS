
package patrimoine.wcs.fr.toulousemonuments.models;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.Map;

public class Record {

    @Key
    private String datasetid;
    @Key
    private String recordid;
    @Key
    private Fields fields;
    @Key
    private Geometry geometry;
    @Key
    private String recordTimestamp;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(String datasetid) {
        this.datasetid = datasetid;
    }

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getRecordTimestamp() {
        return recordTimestamp;
    }

    public void setRecordTimestamp(String recordTimestamp) {
        this.recordTimestamp = recordTimestamp;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
