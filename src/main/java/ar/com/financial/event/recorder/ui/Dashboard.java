package ar.com.financial.event.recorder.ui;

import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.writer.database.SimpleEventRepository;
import ar.com.financial.event.recorder.writer.database.SummaryEventRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderCell;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.cell.GridCellFilter;

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

        addSimpleEventGridFilters(simpleEventGrid);
        addSummaryEventGridFilters(summaryEventGrid);

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

    private void addSummaryEventGridFilters(Grid<SummaryEvent> grid) {
        final GridCellFilter filter = new GridCellFilter(grid, SummaryEvent.class);
        filter.setTextFilter("session", true, true);
        filter.setDateFilter("startSessionTime");
        filter.setDateFilter("endSessionTime");
        filter.setTextFilter("quantityMessagesSent", true, true);
        filter.setTextFilter("quantityMessagesReceived", true, true);
        filter.setTextFilter("firstMessageSentSequence", true, true);
        filter.setTextFilter("lastMessageSentSequence", true, true);
        filter.setTextFilter("firstMessageReceivedSequence", true, true);
        filter.setTextFilter("lastMessageReceivedSequence", true, true);
    }

    private void addSimpleEventGridFilters(Grid<SimpleEvent> grid) {
        final GridCellFilter filter = new GridCellFilter(grid, SimpleEvent.class);
        filter.setDateFilter("arrivalTime");
        filter.setDateFilter("originTime");
        filter.setTextFilter("code", true, true);
        filter.setTextFilter("inputOutput", true, true);
        filter.setTextFilter("remoteBic", true, true);
        filter.setTextFilter("type", true, true);
        filter.setTextFilter("suffix", true, true);
        filter.setTextFilter("session", true, true);
        filter.setTextFilter("sequence", true, true);
        filter.setTextFilter("localBic", true, true);
    }

}
