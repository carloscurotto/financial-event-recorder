package ar.com.financial.event.recorder.ui.model;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.Validate;

@Data
@ToString
public class EventTypeQuantity {

    private String type;
    private Long quantity;

    public EventTypeQuantity(final String type, final Long quantity) {
        Validate.notBlank(type, "The type cannot be blank");
        Validate.notNull(quantity, "The quantity cannot be null");
        this.type = type;
        this.quantity = quantity;
    }

}
