package pt4p1ae1.veto.AgendaPage;

import jfxtras.icalendarfx.components.VEvent;
import jfxtras.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.icalendarfx.properties.component.descriptive.Categories;
import pt4p1ae1.veto.Entity.RendezVousEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe contenant différentes fonctions utilitaires pour les RendezVousEntity
 */
public class RendezVousEntityOservable {

    /**
     * met à jour un RendezVousEntity lié à son VEvent
     * @param vEvent le VEvent lié
     * @param rdv le RendezVousEntity présent dans la base de données
     * @return retour le RendezVousEntity mit à jour
     */
    public static RendezVousEntity updateEntity(VEvent vEvent, RendezVousEntity rdv){
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

    /**
     * Transforme un VEvent en RendezVousEntity
     * @param vEvent le VEvent
     * @return le RendezVousEntity résultant
     */
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

    /**
     * Transforme un RendezVousEntity en VEvent
     * @param entity le RendezVousEntity
     * @return le VEvent résultant
     */
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

        ZoneId zoneId = ZoneId.of("Z");
        LocalDateTime nowTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = nowTime.atZone(zoneId);
        DateTimeStamp stamped = new DateTimeStamp(zonedDateTime);
        event.setDateTimeStamp(stamped);
        return event;
    }
}
