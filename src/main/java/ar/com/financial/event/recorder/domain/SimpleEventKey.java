package ar.com.financial.event.recorder.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class SimpleEventKey implements Serializable {

    private String localBic;
    private String session;
    private String sequence;

    public SimpleEventKey(final String localBic, final String session, final String sequence) {
        Validate.notBlank(localBic, "The local bic cannot be blank");
        Validate.notBlank(session, "The session cannot be blank");
        Validate.notBlank(sequence, "The sequence cannot be blank");
        this.localBic = localBic;
        this.session = session;
        this.sequence = sequence;
    }

}
