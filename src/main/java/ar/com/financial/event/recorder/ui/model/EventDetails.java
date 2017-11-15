package ar.com.financial.event.recorder.ui.model;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.util.Date;

@Data
@ToString
public class EventDetails {

    private String localBic;
    private String sequence;
    private String session;
    private String inputOutput;
    private Date originTime;
    private String remoteBic;
    private String suffix;
    private String type;

    public EventDetails(final String localBic, final String sequence, final String session, final String inputOutput,
                        final Date originTime, final String remoteBic, final String suffix, final String type) {
        Validate.notBlank(localBic, "The local bic cannot be blank");
        Validate.notNull(originTime, "The origin time cannot be null");
        Validate.notBlank(remoteBic, "The remote bic cannot be blank");
        this.localBic = localBic;
        this.sequence = sequence;
        this.session = session;
        this.inputOutput = inputOutput;
        this.originTime = originTime;
        this.remoteBic = remoteBic;
        this.suffix = suffix;
        this.type = type;
    }

}
