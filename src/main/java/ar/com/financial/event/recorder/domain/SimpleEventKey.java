package ar.com.financial.event.recorder.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class SimpleEventKey implements Serializable {

    private String sequence;
    private Date originTime;
    private Date arrivalTime;

    public SimpleEventKey(final String sequence, final Date originTime, final Date arrivalTime) {
        Validate.notBlank(sequence, "The sequence cannot be blank");
        Validate.notNull(originTime, "The origin time cannot be null");
        Validate.notNull(arrivalTime, "The arrival time cannot be null");
        this.sequence = sequence;
        this.originTime = originTime;
        this.arrivalTime = arrivalTime;
    }

}
