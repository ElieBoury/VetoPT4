package pt4p1ae1.veto.DAO.Implementation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import pt4p1ae1.veto.DAO.RaceDAO;
import pt4p1ae1.veto.Entity.Race;
import pt4p1ae1.veto.Entity.Race_;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Implementation of {@link pt4p1ae1.veto.DAO.RaceDAO}.
 */
@Singleton
public class RaceDAOImp implements RaceDAO {

    /**
     * Entity Manager used to perform database operations.
     */
    @Inject
    private Provider<EntityManager> em;

    @Override
    @Transactional
    public void saveOrUpdate(Race race) {
        this.em.get().persist(race);
    }

    @Override
    @Transactional
    public List<Race> findAll() {
        StringBuilder query = new StringBuilder("from ");
        query.append(Race.class.getName());
        List<Race> races =
                em.get().createQuery(query.toString()).getResultList();
        return races;
    }

    @Override
    public Race findByName(String name) {
        StringBuilder query = new StringBuilder()
                .append("from ")
                .append(Race.class.getName())
                .append(" as room")
                .append(" where room.")
                .append(Race_.name.getName())
                .append(" = :name");

        List resultList = em.get()
                .createQuery(query.toString())
                .setParameter("name", name)
                .getResultList();

        if (resultList.size() > 0) {
            return (Race) resultList.get(0);
        }
        return null;
    }
}
