package ar.com.financial.event.recorder.ui;

import ar.com.financial.event.recorder.EventSynchronizer;
import ar.com.financial.event.recorder.domain.SimpleEvent;
import ar.com.financial.event.recorder.domain.SummaryEvent;
import ar.com.financial.event.recorder.writer.database.SimpleEventRepository;
import ar.com.financial.event.recorder.writer.database.SummaryEventRepository;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringUI
@Theme("valo")
public class Dashboard extends UI {

    @Autowired
    private SimpleEventRepository simpleEventRepository;

    @Autowired
    private SummaryEventRepository summaryEventRepository;

    @Autowired
    private EventSynchronizer eventSynchronizer;

    private Grid<SimpleEvent> simpleEventTabGrid = new Grid<>(SimpleEvent.class);

    private Grid<SimpleEvent> simpleEventGrid = new Grid<>(SimpleEvent.class);

    private Grid<SummaryEvent> summaryEventGrid = new Grid<>(SummaryEvent.class);

    @Override
    protected void init(final VaadinRequest request) {
        initializeGrids();

        simpleEventGrid.setSizeFull();
        simpleEventTabGrid.setSizeFull();
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

        content.addComponent(tabsheet);

        // Add footer controls
        HorizontalLayout horizontalLayout = getButtons();
        content.addComponent(horizontalLayout);
    }

    private VerticalLayout createEventsByCategoryTab() {
        VerticalLayout tab = new VerticalLayout();
        tab.addComponent(createEventsByCategorySearchCriteria());
        addSimpleEventTabGridFooter(this.simpleEventTabGrid);
        tab.addComponent(this.simpleEventTabGrid);
        return tab;
    }

    private void addSimpleEventTabGridFooter(final Grid<SimpleEvent> grid) {
        FooterRow footer = grid.appendFooterRow();
        FooterCell totalLabel = footer.getCell("sequence");
        totalLabel.setHtml("<b>Quantity Results:</b>");
        footer.getCell("originTime").setHtml(calculateTotal(grid.getDataProvider()));
        /*
        grid.getDataProvider().addDataProviderListener(event ->
                footer.getCell("originTime").setHtml(calculateTotal(grid.getDataProvider())));
        */
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

        // Day
        layout.addComponent(new Label("Day:"));

        DateField dateSelect = new DateField();
        dateSelect.setValue(LocalDate.now());
        dateSelect.setDateFormat("dd-MM-yyyy");
        dateSelect.setPlaceholder("dd-MM-yyyy");
        dateSelect.setTextFieldEnabled(false);

        layout.addComponent(dateSelect);

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
            final LocalDate dateSelected = dateSelect.getValue();
            final String ioSelected = ioSelect.getValue();

            System.out.println("Bic Selected [" + bicSelected +
                    "], Date Selected [" + dateSelected + "], IO Selected [" + ioSelected + "]");


            java.util.Date dateSelectedStart = Date.from(dateSelected.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
            java.util.Date dateSelectedEnd = Date.from(dateSelected.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

            /*
            *
            * LocalDateTime ldt = ...
ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
Date output = Date.from(zdt.toInstant());*/

            //System.out.println("dateSelectedStart [" + dateSelectedStart + "]");

            //final String dateSelectedEnd = dateSelected.atTime(23, 59, 59).toString();

            //System.out.println("dateSelectedEnd [" + dateSelectedEnd + "]");

            //DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            List<SimpleEvent> items = this.simpleEventRepository.
                    findByLocalBicAndOriginTimeBetweenAndInputOutput(bicSelected, dateSelectedStart, dateSelectedEnd, ioSelected);
            System.out.println("Quantity items queried [" + items.size() + "]");

            this.simpleEventTabGrid.setItems(items);

            this.simpleEventTabGrid.getFooterRow(0).getCell("originTime").setHtml(calculateTotalItems(items));
        });
        layout.addComponent(searchButton);

        return layout;
    }

    private String calculateTotalItems(List<SimpleEvent> items) {
        return "<b>" + items.size() + "</b>";
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
