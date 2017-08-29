package ar.com.financial.event.recorder.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
public class SimpleEvent {

    private Date arrivalTime;
    private Date originTime;
    private String code;
    private String inputOutput;
    private String remoteBic;
    private String type;
    private String suffix;
    private String session;
    @Id
    private String sequence;
    private String localBic;

    SimpleEvent(final Date arrivalTime,
                final Date originTime,
                final String code,
                final String inputOutput,
                final String remoteBic,
                final String type,
                final String suffix,
                final String session,
                final String sequence,
                final String localBic) {
        Validate.notNull(arrivalTime, "The arrival time cannot be null");
        Validate.notNull(originTime, "The origin time cannot be null");
        Validate.notNull(inputOutput, "The intput output cannot be null");
        Validate.notBlank(remoteBic, "The remote bic cannot be blank");
        Validate.notBlank(localBic, "The local bic cannot be blank");
        this.arrivalTime = arrivalTime;
        this.originTime = originTime;
        this.code = code;
        this.inputOutput = inputOutput;
        this.remoteBic = remoteBic;
        this.type = type;
        this.suffix = suffix;
        this.session = session;
        this.sequence = sequence;
        this.localBic = localBic;
    }

}
