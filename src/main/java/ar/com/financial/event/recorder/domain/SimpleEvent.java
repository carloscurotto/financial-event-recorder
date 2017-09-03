package ar.com.financial.event.recorder.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@ToString
@NoArgsConstructor
@IdClass(SimpleEventKey.class)
public class SimpleEvent implements Serializable {

    @Id
    private String localBic;
    @Id
    private String session;
    @Id
    private String sequence;
    private String remoteBic;
    private Date originTime;
    private Date arrivalTime;
    private String code;
    private String inputOutput;
    private String type;
    private String suffix;

    SimpleEvent(final String localBic,
                final String session,
                final String sequence,
                final String remoteBic,
                final Date originTime,
                final Date arrivalTime,
                final String code,
                final String inputOutput,
                final String type,
                final String suffix) {
        Validate.notBlank(localBic, "The local bic cannot be blank");
        Validate.notBlank(remoteBic, "The remote bic cannot be blank");
        Validate.notNull(originTime, "The origin time cannot be null");
        Validate.notNull(arrivalTime, "The arrival time cannot be null");
        Validate.notNull(inputOutput, "The intput output cannot be null");
        this.localBic = localBic;
        this.session = session;
        this.sequence = sequence;
        this.remoteBic = remoteBic;
        this.originTime = originTime;
        this.arrivalTime = arrivalTime;
        this.code = code;
        this.inputOutput = inputOutput;
        this.type = type;
        this.suffix = suffix;
    }

}
