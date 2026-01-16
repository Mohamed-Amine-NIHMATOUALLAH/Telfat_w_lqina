package com.firstproject.telfat_w_lqina.controllers;

import com.firstproject.telfat_w_lqina.Enum.StatusComplaint;
import com.firstproject.telfat_w_lqina.Enum.TypeState;
import com.firstproject.telfat_w_lqina.models.Complaint;
import com.firstproject.telfat_w_lqina.models.LostObject;
import com.firstproject.telfat_w_lqina.models.Stadium;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.service.ComplaintService;
import com.firstproject.telfat_w_lqina.service.LostObjectService;
import com.firstproject.telfat_w_lqina.service.StadiumService;
import com.firstproject.telfat_w_lqina.util.SessionManager;
import com.firstproject.telfat_w_lqina.util.NavigationUtil;
import com.firstproject.telfat_w_lqina.util.LogoutUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AdminStatisticsController {
    @FXML
    private Label labelAdmin;
    @FXML
    private Label totalObjectsLabel;
    @FXML
    private Label inStorageLabel;
    @FXML
    private Label returnedLabel;
    @FXML
    private Label recoveryRateLabel;
    @FXML
    private Label totalComplaintsLabel;
    @FXML
    private Label inProgressLabel;
    @FXML
    private Label foundLabel;
    @FXML
    private Label notFoundLabel;

    @FXML
    private PieChart objectTypeChart;
    @FXML
    private BarChart<String, Number> stadiumChart;
    @FXML
    private LineChart<String, Number> timelineChart;
    @FXML
    private PieChart zoneChart;

    // Conteneurs pour les légendes personnalisées
    @FXML
    private VBox objectTypeLegendContainer;
    @FXML
    private VBox stadiumLegendContainer;
    @FXML
    private VBox zoneLegendContainer;

    private final LostObjectService lostObjectService = new LostObjectService();
    private final ComplaintService complaintService = new ComplaintService();
    private final StadiumService stadiumService = new StadiumService();
    private List<LostObject> lostObjects;
    private List<Complaint> complaints;
    private List<Stadium> stadiums;

    // Dégradé rouge → vert pour tous les graphiques
    private final String[] RED_GREEN_GRADIENT = {
            "#C1272D", "#D4383E", "#E64950", "#F05A62", "#F97B82",
            "#FFA07A", "#FFB6C1", "#98FB98", "#90EE90", "#32CD32",
            "#228B22", "#006400", "#006233"
    };

    @FXML
    public void initialize(){
        // Récupérer l'utilisateur connecté depuis SessionManager
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            labelAdmin.setText(currentUser.getUsername());
        }

        loadData();
        setBasicStatistics();
        initializeCharts();
    }

    private void loadData() {
        lostObjects = lostObjectService.getAllLostObjects();
        complaints = complaintService.getAllComplaints();
        stadiums = stadiumService.getAllStadiums();
    }

    private void refreshData() {
        loadData();
        setBasicStatistics();
        initializeCharts();
    }

    private void setBasicStatistics() {
        totalObjectsLabel.setText(String.valueOf(lostObjects.size()));

        long inStorageCount = lostObjects.stream()
                .filter(obj -> obj.getTypeState() == TypeState.IN_STORAGE)
                .count();
        inStorageLabel.setText(String.valueOf(inStorageCount));

        long returnedCount = lostObjects.stream()
                .filter(obj -> obj.getTypeState() == TypeState.RETURNED)
                .count();
        returnedLabel.setText(String.valueOf(returnedCount));

        double recoveryRate = lostObjects.isEmpty() ? 0 : ((double) returnedCount / lostObjects.size()) * 100;
        recoveryRateLabel.setText(String.format("%.1f%%", recoveryRate));

        totalComplaintsLabel.setText(String.valueOf(complaints.size()));

        long inProgressCount = complaints.stream()
                .filter(c -> c.getStatus() == StatusComplaint.SEARCH_IN_PROGRESS)
                .count();
        inProgressLabel.setText(String.valueOf(inProgressCount));

        long foundCount = complaints.stream()
                .filter(c -> c.getStatus() == StatusComplaint.FOUND)
                .count();
        foundLabel.setText(String.valueOf(foundCount));

        long notFoundCount = complaints.stream()
                .filter(c -> c.getStatus() == StatusComplaint.CLOSED_NOT_FOUND)
                .count();
        notFoundLabel.setText(String.valueOf(notFoundCount));
    }

    private void initializeCharts() {
        initializeObjectTypeChart();
        initializeStadiumChart();
        initializeTimelineChart();
        initializeZoneChart();

        applyColorsAfterDelay();
    }

    private void initializeObjectTypeChart() {
        Map<String, Long> typeCounts = lostObjects.stream()
                .filter(obj -> obj.getType() != null && !obj.getType().isEmpty())
                .collect(Collectors.groupingBy(
                        LostObject::getType,
                        Collectors.counting()
                ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if (typeCounts.isEmpty()) {
            PieChart.Data noData = new PieChart.Data("Aucune donnée", 1);
            pieChartData.add(noData);
        } else {
            List<Map.Entry<String, Long>> sortedTypes = typeCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            for (Map.Entry<String, Long> entry : sortedTypes) {
                PieChart.Data data = new PieChart.Data(
                        entry.getKey() + " (" + entry.getValue() + ")",
                        entry.getValue()
                );
                pieChartData.add(data);
            }
        }

        objectTypeChart.setData(pieChartData);
        objectTypeChart.setTitle("Types d'Objets Perdus");
        objectTypeChart.setLegendVisible(false); // Désactiver la légende par défaut
    }

    private void initializeStadiumChart() {
        Map<String, Long> stadiumCounts = lostObjects.stream()
                .filter(obj -> obj.getZone() != null && !obj.getZone().isEmpty())
                .collect(Collectors.groupingBy(
                        LostObject::getZone,
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Objets Perdus");

        if (stadiumCounts.isEmpty()) {
            series.getData().add(new XYChart.Data<>("Aucune donnée", 0));
        } else {
            List<Map.Entry<String, Long>> sortedStadiums = stadiumCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toList());

            for (Map.Entry<String, Long> entry : sortedStadiums) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(data);
            }
        }

        stadiumChart.getData().clear();
        stadiumChart.getData().add(series);
        stadiumChart.setTitle("Objets Perdus par Zone/Stade");
        stadiumChart.setLegendVisible(false);
    }

    private void initializeTimelineChart() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(5);

        Map<YearMonth, Long> monthlyCounts = lostObjects.stream()
                .filter(obj -> obj.getLostDate() != null)
                .filter(obj -> !obj.getLostDate().isBefore(sixMonthsAgo))
                .collect(Collectors.groupingBy(
                        obj -> YearMonth.from(obj.getLostDate()),
                        Collectors.counting()
                ));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Objets Perdus");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH);

        List<YearMonth> allMonths = new ArrayList<>();
        YearMonth current = YearMonth.from(sixMonthsAgo);
        YearMonth now = YearMonth.now();

        while (!current.isAfter(now)) {
            allMonths.add(current);
            current = current.plusMonths(1);
        }

        for (YearMonth month : allMonths) {
            Long count = monthlyCounts.getOrDefault(month, 0L);
            String label = month.format(formatter);
            XYChart.Data<String, Number> data = new XYChart.Data<>(label, count);
            series.getData().add(data);
        }

        timelineChart.getData().clear();
        timelineChart.getData().add(series);
        timelineChart.setTitle("Évolution des Objets Perdus (6 derniers mois)");
        timelineChart.setLegendVisible(false);
    }

    private void initializeZoneChart() {
        Map<String, Long> zoneCounts = lostObjects.stream()
                .filter(obj -> obj.getZone() != null && !obj.getZone().isEmpty())
                .collect(Collectors.groupingBy(
                        LostObject::getZone,
                        Collectors.counting()
                ));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if (zoneCounts.isEmpty()) {
            PieChart.Data noData = new PieChart.Data("Aucune donnée", 1);
            pieChartData.add(noData);
        } else {
            List<Map.Entry<String, Long>> sortedZones = zoneCounts.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .limit(8)
                    .collect(Collectors.toList());

            for (Map.Entry<String, Long> entry : sortedZones) {
                PieChart.Data data = new PieChart.Data(
                        entry.getKey() + " (" + entry.getValue() + ")",
                        entry.getValue()
                );
                pieChartData.add(data);
            }
        }

        zoneChart.setData(pieChartData);
        zoneChart.setTitle("Répartition par Zone");
        zoneChart.setLegendVisible(false); // Désactiver la légende par défaut
    }

    private void applyColorsAfterDelay() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> {
                    applyRedGreenGradientColors();
                    createCustomLegends();
                });
            }
        }, 300);
    }

    private void applyRedGreenGradientColors() {
        applyGradientToPieChart(objectTypeChart);
        applyGradientToPieChart(zoneChart);
        applyGradientToBarChart(stadiumChart);
        applyGradientToLineChart(timelineChart);
    }

    private void applyGradientToPieChart(PieChart chart) {
        ObservableList<PieChart.Data> chartData = chart.getData();

        for (int i = 0; i < chartData.size(); i++) {
            PieChart.Data data = chartData.get(i);
            if (data.getNode() != null) {
                int colorIndex = (i * RED_GREEN_GRADIENT.length) / Math.max(1, chartData.size());
                String color = RED_GREEN_GRADIENT[Math.min(colorIndex, RED_GREEN_GRADIENT.length - 1)];
                data.getNode().setStyle("-fx-pie-color: " + color + ";");
            }
        }
    }

    private void applyGradientToBarChart(BarChart<String, Number> chart) {
        if (!chart.getData().isEmpty()) {
            XYChart.Series<String, Number> series = chart.getData().get(0);

            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                if (data.getNode() != null) {
                    int colorIndex = (i * RED_GREEN_GRADIENT.length) / Math.max(1, series.getData().size());
                    String color = RED_GREEN_GRADIENT[Math.min(colorIndex, RED_GREEN_GRADIENT.length - 1)];
                    data.getNode().setStyle("-fx-bar-fill: " + color + ";");
                }
            }
        }
    }

    private void applyGradientToLineChart(LineChart<String, Number> chart) {
        if (!chart.getData().isEmpty()) {
            XYChart.Series<String, Number> series = chart.getData().get(0);

            if (series.getNode() != null) {
                series.getNode().setStyle("-fx-stroke: #C1272D; -fx-stroke-width: 2px;");
            }

            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle(
                            "-fx-background-color: #006233, white; " +
                                    "-fx-background-radius: 4px; " +
                                    "-fx-padding: 4px; " +
                                    "-fx-scale-x: 1.2; " +
                                    "-fx-scale-y: 1.2;"
                    );
                }
            }
        }
    }

    // Créer les légendes personnalisées
    private void createCustomLegends() {
        createPieChartLegend(objectTypeChart, objectTypeLegendContainer);
        createBarChartLegend(stadiumChart, stadiumLegendContainer);
        createPieChartLegend(zoneChart, zoneLegendContainer);
    }

    private void createPieChartLegend(PieChart chart, VBox container) {
        if (container == null) return;

        container.getChildren().clear();
        container.setSpacing(8);
        container.setStyle("-fx-padding: 10;");

        ObservableList<PieChart.Data> chartData = chart.getData();

        for (int i = 0; i < chartData.size(); i++) {
            PieChart.Data data = chartData.get(i);
            int colorIndex = (i * RED_GREEN_GRADIENT.length) / Math.max(1, chartData.size());
            String color = RED_GREEN_GRADIENT[Math.min(colorIndex, RED_GREEN_GRADIENT.length - 1)];

            HBox legendItem = createLegendItem(data.getName(), color);
            container.getChildren().add(legendItem);
        }
    }

    private void createBarChartLegend(BarChart<String, Number> chart, VBox container) {
        if (container == null) return;

        container.getChildren().clear();
        container.setSpacing(8);
        container.setStyle("-fx-padding: 10;");

        if (!chart.getData().isEmpty()) {
            XYChart.Series<String, Number> series = chart.getData().get(0);

            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                int colorIndex = (i * RED_GREEN_GRADIENT.length) / Math.max(1, series.getData().size());
                String color = RED_GREEN_GRADIENT[Math.min(colorIndex, RED_GREEN_GRADIENT.length - 1)];

                String label = data.getXValue() + " (" + data.getYValue() + ")";
                HBox legendItem = createLegendItem(label, color);
                container.getChildren().add(legendItem);
            }
        }
    }

    private HBox createLegendItem(String text, String color) {
        HBox item = new HBox(10);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setStyle("-fx-padding: 5; -fx-background-color: white; -fx-background-radius: 4;");

        // Rectangle de couleur
        Rectangle colorBox = new Rectangle(20, 15);
        colorBox.setFill(Color.web(color));
        colorBox.setStyle("-fx-arc-width: 3; -fx-arc-height: 3;");

        // Label du texte
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #495057; -fx-font-size: 12px;");

        item.getChildren().addAll(colorBox, label);
        return item;
    }

    @FXML
    private void refreshStatistics(ActionEvent event) {
        refreshData();
    }

    // Navigation methods
    @FXML
    public void goToDashboard(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/Admin.fxml");
    }

    @FXML
    public void createUser(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddUsers.fxml");
    }

    @FXML
    public void viewLostObjects(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewLostObjectsAdmin.fxml");
    }

    @FXML
    public void addStadium(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/AddStadium.fxml");
    }

    @FXML
    public void goToStadiumList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewStadiums.fxml");
    }

    @FXML
    public void goToComplaintList(ActionEvent event) throws IOException {
        NavigationUtil.navigate(event, "/fxml/ViewComplaintAdmin.fxml");
    }

    @FXML
    public void logout(ActionEvent event) throws IOException {
        LogoutUtil.logout(event);
    }
}