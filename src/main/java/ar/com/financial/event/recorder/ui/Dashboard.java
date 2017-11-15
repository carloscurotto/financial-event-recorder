package ar.com.financial.event.recorder.ui;

import ar.com.financial.event.recorder.EventSynchronizer;
import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.ui.model.EventDetails;
import ar.com.financial.event.recorder.ui.model.EventTypeQuantity;
import ar.com.financial.event.recorder.writer.database.SimpleEventRepository;
import ar.com.financial.event.recorder.writer.database.SummaryEventRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.FooterCell;
import com.vaadin.ui.components.grid.FooterRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gridutil.cell.GridCellFilter;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringUI
@Theme("valo")
public class Dashboard extends UI {

    @Autowired
    private SimpleEventRepository simpleEventRepository;

    @Autowired
    private SummaryEventRepository summaryEventRepository;

    @Autowired
    private EventSynchronizer eventSynchronizer;

    private Grid<EventTypeQuantity> simpleEventsByTypeGrid = new Grid<>(EventTypeQuantity.class);

    private Grid<EventDetails> detailEventsGrid = new Grid<>(EventDetails.class);

    private Grid<SimpleEvent> simpleEventGrid = new Grid<>(SimpleEvent.class);

    private Grid<SummaryEvent> summaryEventGrid = new Grid<>(SummaryEvent.class);

    @Override
    protected void init(final VaadinRequest request) {
        initializeGrids();

        simpleEventGrid.setSizeFull();
        //simpleEventsByTypeGrid.setSizeFull();
        summaryEventGrid.setSizeFull();
        //summaryEventGrid.setVisible(false);

        addSimpleEventGridFilters(simpleEventGrid);
        addSummaryEventGridFilters(summaryEventGrid);

        addSimpleEventGridFooter(simpleEventGrid);

        VerticalLayout content = new VerticalLayout();
        setContent(content);

        // Add page title
        Label label = new Label("<h2 style=\"text-align: center\">Swift Events Recorder</h2>");
        label.setSizeFull();
        label.setContentMode(ContentMode.HTML);
        content.addComponent(label);

        // Add tabs
        TabSheet tabsheet = new TabSheet();

        VerticalLayout simpleEventTab = new VerticalLayout();
        simpleEventTab.addComponent(simpleEventGrid);
        tabsheet.addTab(simpleEventTab, "Simple Events");

        VerticalLayout summaryEventTab = new VerticalLayout();
        summaryEventTab.addComponent(summaryEventGrid);
        tabsheet.addTab(summaryEventTab, "Summary Events");

        VerticalLayout eventsByCategoryTab = createEventsByCategoryTab();
        tabsheet.addTab(eventsByCategoryTab, "Events By Category");

        VerticalLayout detailEventsTab = createDetailEventsTab();
        tabsheet.addTab(detailEventsTab, "Detail Events");

        content.addComponent(tabsheet);

        // Add footer controls
        HorizontalLayout horizontalLayout = getButtons();
        content.addComponent(horizontalLayout);
    }

    private VerticalLayout createDetailEventsTab() {
        VerticalLayout tab = new VerticalLayout();
        //this.detailEventsGrid.setItems(simpleEventRepository.findAll());
        this.detailEventsGrid.setSizeFull();
        tab.addComponent(createDetailEventsSearchCriteria());
        this.detailEventsGrid.setColumnOrder("localBic", "sequence", "session", "inputOutput", "originTime", "remoteBic", "suffix", "type");
        this.detailEventsGrid.setHeightByRows(8d);
        tab.addComponent(this.detailEventsGrid);
        return tab;
    }

    private Component createDetailEventsSearchCriteria() {
        HorizontalLayout layout = new HorizontalLayout();

        // Bic
        layout.addComponent(new Label("BIC:"));

        List<String> bics = new ArrayList<>();
        bics.add("BBDEARBA");
        bics.add("MARIARBA");

        ComboBox<String> bicSelect = new ComboBox<>();
        bicSelect.setItems(bics);
        bicSelect.setValue(bics.get(0));
        bicSelect.setItemCaptionGenerator(String::toString);
        bicSelect.setEmptySelectionAllowed(false);

        layout.addComponent(bicSelect);

        // Day Start
        layout.addComponent(new Label("Start:"));

        DateField dateSelectStart = new DateField();
        dateSelectStart.setValue(LocalDate.now());
        dateSelectStart.setDateFormat("dd-MM-yyyy");
        dateSelectStart.setPlaceholder("dd-MM-yyyy");
        dateSelectStart.setTextFieldEnabled(false);

        layout.addComponent(dateSelectStart);

        // Day Start
        layout.addComponent(new Label("End:"));

        DateField dateSelectEnd = new DateField();
        dateSelectEnd.setValue(LocalDate.now());
        dateSelectEnd.setDateFormat("dd-MM-yyyy");
        dateSelectEnd.setPlaceholder("dd-MM-yyyy");
        dateSelectEnd.setTextFieldEnabled(false);

        layout.addComponent(dateSelectEnd);

        // Search
        Button searchButton = new Button("Search");
        searchButton.addClickListener(event -> {
            final String bicSelected = bicSelect.getValue();
            final LocalDate localDateSelectedStart = dateSelectStart.getValue();
            final LocalDate localDateSelectedEnd = dateSelectEnd.getValue();

            java.util.Date dateSelectedStart = Date.from(localDateSelectedStart.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
            java.util.Date dateSelectedEnd = Date.from(localDateSelectedEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

            List<SimpleEvent> items = this.simpleEventRepository.
                    findByLocalBicAndOriginTimeBetween(bicSelected, dateSelectedStart, dateSelectedEnd);
            System.out.println("Quantity items queried [" + items.size() + "]");

            List<EventDetails> eventDetails = items.stream().map(item -> {
                return new EventDetails(item.getLocalBic(), item.getSequence(), item.getSession(), item.getInputOutput(),
                        item.getOriginTime(), item.getRemoteBic(), item.getSuffix(), item.getType());
            }).collect(Collectors.toList());

            /*
            Map<String, EventTypeQuantity> eventTypesQuantity = new HashMap<>();
            for (SimpleEvent item : items) {
                EventTypeQuantity eventTypeQuantity = eventTypesQuantity.get(item.getType());
                if (eventTypeQuantity == null) {
                    eventTypeQuantity = new EventTypeQuantity(item.getType(), 1L);
                } else {
                    eventTypeQuantity.setQuantity(eventTypeQuantity.getQuantity() + 1L);
                }
                eventTypesQuantity.put(eventTypeQuantity.getType(), eventTypeQuantity);
            }
            */

            this.detailEventsGrid.setItems(eventDetails);
        });
        layout.addComponent(searchButton);

        return layout;
    }

    private VerticalLayout createEventsByCategoryTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.addComponent(createEventsByCategorySearchCriteria());
        this.simpleEventsByTypeGrid.setColumnOrder("type", "quantity");
        this.simpleEventsByTypeGrid.setHeightByRows(8d);
        tab.addComponent(this.simpleEventsByTypeGrid);
        return tab;
    }

    private HorizontalLayout createEventsByCategorySearchCriteria() {
        HorizontalLayout layout = new HorizontalLayout();

        // Bic
        layout.addComponent(new Label("BIC:"));

        List<String> bics = new ArrayList<>();
        bics.add("BBDEARBA");
        bics.add("MARIARBA");

        ComboBox<String> bicSelect = new ComboBox<>();
        bicSelect.setItems(bics);
        bicSelect.setValue(bics.get(0));
        bicSelect.setItemCaptionGenerator(String::toString);
        bicSelect.setEmptySelectionAllowed(false);

        layout.addComponent(bicSelect);

        // Day Start
        layout.addComponent(new Label("Start:"));

        DateField dateSelectStart = new DateField();
        dateSelectStart.setValue(LocalDate.now());
        dateSelectStart.setDateFormat("dd-MM-yyyy");
        dateSelectStart.setPlaceholder("dd-MM-yyyy");
        dateSelectStart.setTextFieldEnabled(false);

        layout.addComponent(dateSelectStart);

        // Day End
        layout.addComponent(new Label("End:"));

        DateField dateSelectEnd = new DateField();
        dateSelectEnd.setValue(LocalDate.now());
        dateSelectEnd.setDateFormat("dd-MM-yyyy");
        dateSelectEnd.setPlaceholder("dd-MM-yyyy");
        dateSelectEnd.setTextFieldEnabled(false);

        layout.addComponent(dateSelectEnd);

        // I/O
        layout.addComponent(new Label("I/O:"));

        List<String> ios = new ArrayList<>();
        ios.add("I");
        ios.add("O");

        ComboBox<String> ioSelect = new ComboBox<>();
        ioSelect.setItems(ios);
        ioSelect.setValue(ios.get(0));
        ioSelect.setItemCaptionGenerator(String::toString);
        ioSelect.setEmptySelectionAllowed(false);

        layout.addComponent(ioSelect);

        // Search
        Button searchButton = new Button("Search");
        searchButton.addClickListener(event -> {
            final String bicSelected = bicSelect.getValue();
            final LocalDate localDateSelectedStart = dateSelectStart.getValue();
            final LocalDate localDateSelectedEnd = dateSelectEnd.getValue();
            final String ioSelected = ioSelect.getValue();

            java.util.Date dateSelectedStart = Date.from(localDateSelectedStart.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
            java.util.Date dateSelectedEnd = Date.from(localDateSelectedEnd.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

            List<SimpleEvent> items = this.simpleEventRepository.
                    findByLocalBicAndOriginTimeBetweenAndInputOutput(bicSelected, dateSelectedStart, dateSelectedEnd, ioSelected);
            System.out.println("Quantity items queried [" + items.size() + "]");

            Map<String, EventTypeQuantity> eventTypesQuantity = new HashMap<>();
            for (SimpleEvent item : items) {
                EventTypeQuantity eventTypeQuantity = eventTypesQuantity.get(item.getType());
                if (eventTypeQuantity == null) {
                    eventTypeQuantity = new EventTypeQuantity(item.getType(), 1L);
                } else {
                    eventTypeQuantity.setQuantity(eventTypeQuantity.getQuantity() + 1L);
                }
                eventTypesQuantity.put(eventTypeQuantity.getType(), eventTypeQuantity);
            }

            this.simpleEventsByTypeGrid.setItems(eventTypesQuantity.values());
        });
        layout.addComponent(searchButton);

        return layout;
    }

    private String calculateTotalItems(List<SimpleEvent> items) {
        return "<b>Quantity Results: " + items.size() + "</b>";
    }

    private void addSimpleEventGridFooter(final Grid<SimpleEvent> grid) {
        FooterRow footer = grid.appendFooterRow();
        FooterCell totalLabel = footer.getCell("sequence");
        totalLabel.setHtml("<b>Quantity Results:</b>");

        footer.getCell("originTime").setHtml(calculateTotal(grid.getDataProvider()));
        grid.getDataProvider().addDataProviderListener(event ->
                footer.getCell("originTime").setHtml(calculateTotal(grid.getDataProvider())));
    }

    private String calculateTotal(DataProvider<SimpleEvent, ?> provider) {
        return "<b>" + provider.fetch(new Query<>()).count() + "</b>";
    }

    private HorizontalLayout getButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        //final Button simpleButton = new Button("Simple");
        //simpleButton.setEnabled(false);
        //final Button summaryButton = new Button("Summary");
        final Button synchronizeButton = new Button("Synchronize");

        /*
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
        */

        synchronizeButton.addClickListener(e -> {
            Notification.show("Starting event synchronization...");
            eventSynchronizer.synchronize();
            Notification.show("Event synchronization completed.");
        });

        //horizontalLayout.addComponent(simpleButton);
        //horizontalLayout.addComponent(summaryButton);
        horizontalLayout.addComponent(synchronizeButton);
        return horizontalLayout;
    }

    private void initializeGrids() {
        simpleEventGrid.setItems(simpleEventRepository.findAll());
        summaryEventGrid.setItems(summaryEventRepository.findAll());
    }

    private void addSummaryEventGridFilters(Grid<SummaryEvent> grid) {
        final GridCellFilter filter = new GridCellFilter(grid, SummaryEvent.class);
        filter.setTextFilter("localBic", true, true);
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
