package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.validator.ValidEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 8:04 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@Entity
//@Table(name = "Mention")
public final class Mention {
    // TODO(fuxing): Allow multiple places to be mentioned

    /**
     * Mention uses L16 ids, mention is not a top level node
     * In comparision with places mention can generate as many as 10_000x of place (too many of them)
     */
    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{16}$")
    @Id
    @Column(length = 16, updatable = false, nullable = false, unique = true)
    private String id;


    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private MentionType type;

    // MentionStatus status;
    // MentionClassification classification; (Linked externally?)

    // Mention can exists without place linking (status as pending?)

    // Mention has classification, AKA can have ML features attached to it.
}
