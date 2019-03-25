package pt4p1ae1.veto.AgendaPage;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import pt4p1ae1.veto.Entity.AnimalEntity;
import pt4p1ae1.veto.Entity.RendezVousEntity;
import pt4p1ae1.veto.Entity.VeterinaireEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

public class RendezVousEntityOservable {
    private RendezVousEntity rendezVousEntity;
    private long id;
    private long idAnimal;
    private long idVeterinaire;
    private Timestamp start;
    private Timestamp end;
    private String message;
    private AnimalEntity animalByIdAnimal;
    private VeterinaireEntity veterinaireByIdVeterinaire;


    public static RendezVousEntity toEntity(VEvent vEvent){
        RendezVousEntity rdv = new RendezVousEntity();
        Temporal temporalStart = vEvent.getDateTimeStart().getValue();
        rdv.setDateHeureDebut(Timestamp.valueOf(LocalDateTime.from(temporalStart)));
        Temporal temporalEnd = vEvent.getDateTimeEnd().getValue();
        rdv.setDateHeureFin(Timestamp.valueOf(LocalDateTime.from(temporalEnd)));
        rdv.setResume(vEvent.getSummary().getValue());
        if(vEvent.getDescription()!=null){
            rdv.setDescription(vEvent.getDescription().getValue());
        }
        rdv.setCategorie(vEvent.getCategories().get(0).getValue().get(0));
        return rdv;
    }

    public static VEvent toVEvent(RendezVousEntity entity){
        VEvent event = new VEvent();
        Temporal start = (Temporal) entity.getDateHeureDebut().toLocalDateTime();
        event.setDateTimeStart(start);
        Temporal end = (Temporal) entity.getDateHeureFin().toLocalDateTime();
        event.setDateTimeEnd(end);
        event.setSummary(entity.getResume());
        event.setDescription(entity.getDescription());
        event.setUniqueIdentifier();
        List<String> stringList = new ArrayList<>();
        List<Categories> categories = new ArrayList<>();
        stringList.add(entity.getCategorie());
        categories.add(new Categories(stringList));
        event.setCategories(categories);
        return event;
    }
}