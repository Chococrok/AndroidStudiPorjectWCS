
package com.wcs.geolocation;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.Map;

public class ForecastWeatherModel {

    @Key
    private String cod;
    @Key
    private Double message;
    @Key
    private Integer cnt;
    @Key
    private java.util.List<com.wcs.geolocation.List> list = null;
    @Key
    private City city;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<com.wcs.geolocation.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.wcs.geolocation.List> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
