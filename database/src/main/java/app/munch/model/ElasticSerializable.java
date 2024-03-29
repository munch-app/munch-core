package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Used to mark that can be mapped into ElasticDocument
 * <p>
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 4:29 pm
 */
public interface ElasticSerializable {

    @JsonIgnore
    ElasticDocumentType getElasticDocumentType();

    @JsonIgnore
    Map<String, String> getElasticDocumentKeys();
}
