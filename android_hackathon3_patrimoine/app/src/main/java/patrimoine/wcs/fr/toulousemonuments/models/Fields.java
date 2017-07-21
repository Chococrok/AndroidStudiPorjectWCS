
package patrimoine.wcs.fr.toulousemonuments.models;

import com.google.api.client.util.Key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fields {

    @Key
    private String commune;
    @Key
    private String descriptif;
    @Key
    private String nom_cdt;
    @Key
    private String codePost;
    @Key
    private String adresse;
    @Key
    private String precision;
    @Key
    private List<Double> geo_point_2d = null;
    @Key
    private String icone;
    @Key
    private String id_cdt;
    @Key
    private String etiquette;
    @Key
    private String source;
    @Key
    private String codeInsee;
    @Key
    private GeoShape geoShape;
    @Key
    private String nom;
    @Key
    private Double id;
    @Key
    private String img_cdt;
    @Key
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public String getNomCdt() {
        return nom_cdt;
    }

    public void setNomCdt(String nomCdt) {
        this.nom_cdt = nomCdt;
    }

    public String getCodePost() {
        return codePost;
    }

    public void setCodePost(String codePost) {
        this.codePost = codePost;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }

    public List<Double> getGeoPoint2d() {
        return geo_point_2d;
    }

    public void setGeoPoint2d(List<Double> geoPoint2d) {
        this.geo_point_2d = geoPoint2d;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getIdCdt() {
        return id_cdt;
    }

    public void setIdCdt(String idCdt) {
        this.id_cdt = idCdt;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCodeInsee() {
        return codeInsee;
    }

    public void setCodeInsee(String codeInsee) {
        this.codeInsee = codeInsee;
    }

    public GeoShape getGeoShape() {
        return geoShape;
    }

    public void setGeoShape(GeoShape geoShape) {
        this.geoShape = geoShape;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getImgCdt() {
        return img_cdt;
    }

    public void setImgCdt(String imgCdt) {
        this.img_cdt = imgCdt;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
