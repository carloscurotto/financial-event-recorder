package ar.com.financial.event.recorder.ui;

import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.writer.database.SimpleEventRepository;
import ar.com.financial.event.recorder.writer.database.SummaryEventRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("valo")
public class Dashboard extends UI {

    @Autowired
    private SimpleEventRepository simpleEventRepository;

    @Autowired
    private SummaryEventRepository summaryEventRepository;

    private Grid<SummaryEvent> summaryEventGrid = new Grid<>(SummaryEvent.class);

    private Grid<SimpleEvent> simpleEventGrid = new Grid<>(SimpleEvent.class);

    @Override
    protected void init(final VaadinRequest request) {
        initializeGrids();

        simpleEventGrid.setSizeFull();
        summaryEventGrid.setSizeFull();
        summaryEventGrid.setVisible(false);

        VerticalLayout content = new VerticalLayout();
        setContent(content);

        content.addComponent(summaryEventGrid);
        content.addComponent(simpleEventGrid);

        HorizontalLayout horizontalLayout = getButtons();
        content.addComponent(horizontalLayout);
    }

    private HorizontalLayout getButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        final Button simpleButton = new Button("Simple");
        simpleButton.setEnabled(false);
        final Button summaryButton = new Button("Summary");

        simpleButton.addClickListener(e -> {
            e.getButton().setEnabled(false);
            summaryButton.setEnabled(true);
            simpleEventGrid.setVisible(true);
            summaryEventGrid.setVisible(false);
        });

        summaryButton.addClickListener(e -> {
            e.getButton().setEnabled(false);
            simpleButton.setEnabled(true);
            simpleEventGrid.setVisible(false);
            summaryEventGrid.setVisible(true);
        });

        horizontalLayout.addComponent(simpleButton);
        horizontalLayout.addComponent(summaryButton);
        return horizontalLayout;
    }

    private void initializeGrids() {
        simpleEventGrid.setItems(simpleEventRepository.findAll());
        summaryEventGrid.setItems(summaryEventRepository.findAll());
    }

}
