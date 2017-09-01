package ar.com.financial.event.recorder.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class SummaryEventKey implements Serializable {

    private String localBic;
    private String session;

    public SummaryEventKey(final String localBic, final String session) {
        Validate.notBlank(localBic, "The local bic cannot be blank");
        Validate.notBlank(session, "The session cannot be blank");
        this.localBic = localBic;
        this.session = session;
    }

}
