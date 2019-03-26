package pt4p1ae1.veto.AgendaPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda;
import pt4p1ae1.veto.AgendaPage.ModifiedICalendarAgenda.NewICalendarAgenda;
import pt4p1ae1.veto.ControllerSample;
import pt4p1ae1.veto.Entity.AnimalEntity;
import pt4p1ae1.veto.Entity.ClientEntity;
import pt4p1ae1.veto.Entity.RendezVousEntity;
import pt4p1ae1.veto.Entity.VeterinaireEntity;
import pt4p1ae1.veto.Utils;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

public class AgendaPage extends ControllerSample implements Initializable {

    @FXML
    public ComboBox<AnimalEntity> animalBox;
    @FXML
    public ComboBox<ClientEntity> clientBox;
    @FXML
    public ComboBox<VeterinaireEntity> vetoBox;
    @FXML
    public Button modifyButton;
    @FXML
    public VBox modifyEvent;
    @FXML
    private BorderPane BorderPane;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button increase_btn;
    @FXML
    private Button decrease_btn;
    @FXML
    private Button save_btn;
    @FXML
    private Label messageAfterModif;


    private VCalendar vCalendar;
    private static NewICalendarAgenda agendaHome;
    private RendezVousEntity selectedRDV;
    private VEvent selectedVEvent;
    public static HashMap<String, RendezVousEntity> vEventEntity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vCalendar = new VCalendar();
        agendaHome = new NewICalendarAgenda(vCalendar);
        vEventEntity = new HashMap<>();
        datePicker.setValue(agendaHome.getDisplayedLocalDateTime().toLocalDate());
        BorderPane.setCenter(agendaHome);

        super.start();
        setActionButton();
        initializeAgenda();
        addAllEvent();
    }

    private void addAllEvent() {
        List<VEvent> vEventList = new ArrayList<>();
        Utils.RENDEZ_VOUS_DAO.findAll().forEach(entity -> {
            VEvent vEvent = RendezVousEntityOservable.toVEvent(entity);
            if(entity.getIdAnimal() != null && entity.getIdVeterinaire()!=null){
                vEventEntity.put(vEvent.getUniqueIdentifier().getValue(),entity);
            }
            vEventList.add(vEvent);
        });
        agendaHome.getVCalendar().setVEvents(vEventList);
    }

    private void initializeAgenda() {
        agendaHome.setSelectedOneAppointmentCallback(param ->
                {
                    Agenda.Appointment selectedAppointment = (Agenda.Appointment) param;
                    selectedVEvent = (VEvent) agendaHome.getAppointmentVComponentMap()
                            .get(System.identityHashCode(selectedAppointment));
                    if (selectedVEvent == null) {
                        selectedVEvent = (VEvent) agendaHome.getVComponentFactory().createVComponent(selectedAppointment);
                    }
                    selectedRDV = RendezVousEntityOservable.toEntity(selectedVEvent);
                    modifyEvent.setDisable(false);

                    modifyButton.setOnAction(event -> {
                        if (animalBox.getValue() != null && clientBox.getValue() != null) {
                            selectedRDV.setIdAnimal(animalBox.getValue().getId()); //recup l'id de l'animal
                            selectedRDV.setIdVeterinaire(vetoBox.getValue().getId()); //recup l'id du client
                            selectedRDV.setDescription("Rendez vous :\n" +clientBox.getValue()+"\n" +animalBox.getValue());
                            selectedVEvent.setDescription("Rendez vous :\n" +clientBox.getValue()+"\n" +animalBox.getValue());
                            vEventEntity.put(selectedVEvent.getUniqueIdentifier().getValue(), selectedRDV);
                            saveEvents();
                            agendaHome.refresh();
                            messageAfterModif.setTextFill(Color.web("#15e246"));
                            messageAfterModif.setText("Rendez Vous Ajouté");
                        }else{
                            messageAfterModif.setTextFill(Color.web("#e23a15"));
                            messageAfterModif.setText("Champs invalides");
                        }
                    });
                    return null;
                }
        );

        ArrayList<VeterinaireEntity> listVeto = new ArrayList<>(Utils.VETERINAIRE_DAO.findAll());
        ObservableList<VeterinaireEntity> vetoEntities = FXCollections.observableArrayList(listVeto);
        vetoBox.setItems(vetoEntities);

        ArrayList<ClientEntity> listClient = new ArrayList<>(Utils.CLIENT_DAO.findAll());
        ObservableList<ClientEntity> clientEntities = FXCollections.observableArrayList(listClient);
        clientBox.setItems(clientEntities);

        clientBox.setOnAction(event -> {
            ClientEntity client = (ClientEntity) clientBox.getValue();
            ArrayList<AnimalEntity> listAnimal = new ArrayList<>(Utils.getAnimalFromClient(client.getId()));
            ObservableList<AnimalEntity> animalEntities = FXCollections.observableArrayList(listAnimal);
            animalBox.setItems(animalEntities);
        });
    }

    private void setActionButton() {
        increase_btn.setOnAction(event -> {
            LocalDateTime newLocalDateTime = agendaHome.getDisplayedLocalDateTime().plus(Period.ofWeeks(1));
            agendaHome.setDisplayedLocalDateTime(newLocalDateTime);
            datePicker.setValue(newLocalDateTime.toLocalDate());
        });

        decrease_btn.setOnAction(event -> {
            LocalDateTime newLocalDateTime = agendaHome.getDisplayedLocalDateTime().minus(Period.ofWeeks(1));
            agendaHome.setDisplayedLocalDateTime(newLocalDateTime);
            datePicker.setValue(newLocalDateTime.toLocalDate());
        });

        save_btn.setOnAction(event -> {
            saveEvents();
        });

        datePicker.setOnAction(event -> {
            LocalDateTime newLocalDateTime = datePicker.getValue().atStartOfDay();
            agendaHome.setDisplayedLocalDateTime(newLocalDateTime);
        });

    }

    static public void saveEvents() {
        List<VEvent> vEvents = agendaHome.getVCalendar().getVEvents();
        Utils.RENDEZ_VOUS_DAO.removeAll();
        if (vEvents != null) {
            if (!vEvents.isEmpty()) {
                vEvents.forEach(vEvent -> {
                    RendezVousEntity rdv;
                    String UID = vEvent.getUniqueIdentifier().getValue();
                    if (vEventEntity.containsKey(UID)) {
                        rdv = RendezVousEntityOservable.updateEntity(vEvent,vEventEntity.get(UID));
                    } else {
                        rdv = RendezVousEntityOservable.toEntity(vEvent);
                    }
                    Utils.RENDEZ_VOUS_DAO.saveOrUpdate(rdv);
                });
                List<String> eventRemove = new ArrayList<>();
                vEventEntity.keySet().forEach(s -> {
                    boolean exist = false;
                    Iterator<VEvent> iterator = vEvents.iterator();
                    while (!exist && iterator.hasNext()) {
                        exist = iterator.next().getUniqueIdentifier().getValue().equals(s);
                    }
                    if (!exist) {
                        eventRemove.add(s);
                    }
                });
                eventRemove.forEach(s -> {
                    vEventEntity.remove(s);
                });
            }
        }
    }
}
