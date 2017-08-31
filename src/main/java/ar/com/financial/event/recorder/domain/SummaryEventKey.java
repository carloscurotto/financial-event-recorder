package ar.com.financial.event.recorder.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class SummaryEventKey implements Serializable {

    private String session;
    private Date startSessionTime;
    private Date endSessionTime;

    public SummaryEventKey(final String session, final Date startSessionTime, final Date endSessionTime) {
        Validate.notBlank(session, "The session cannot be blank");
        Validate.notNull(startSessionTime, "The start session time cannot be null");
        Validate.notNull(endSessionTime, "The end session time cannot be null");
        this.session = session;
        this.startSessionTime = startSessionTime;
        this.endSessionTime = endSessionTime;
    }

}
