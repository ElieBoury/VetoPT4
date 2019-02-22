package pt4p1ae1.veto.DAO.Implementation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import pt4p1ae1.veto.DAO.EmployeDAO;
import pt4p1ae1.veto.Entity.Employe;
import pt4p1ae1.veto.Entity.Employe_;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Implementation of {@link pt4p1ae1.veto.DAO.EmployeDAO}.
 */
@Singleton
public class EmployeDAOImp implements EmployeDAO {

    /**
     * Entity Manager used to perform database operations.
     */
    @Inject
    private Provider<EntityManager> em;

    @Override
    @Transactional
    public void saveOrUpdate(Employe employe) {
        this.em.get().persist(employe);
    }

    @Override
    @Transactional
    public List<Employe> findAll() {
        StringBuilder query = new StringBuilder("from ");
        query.append(Employe.class.getName());
        List<Employe> employes =
                em.get().createQuery(query.toString()).getResultList();
        return employes;
    }

    @Override
    public Employe findByName(String name) {
        StringBuilder query = new StringBuilder()
                .append("from ")
                .append(Employe.class.getName())
                .append(" as room")
                .append(" where room.")
                .append(Employe_.name.getName())
                .append(" = :name");

        List resultList = em.get()
                .createQuery(query.toString())
                .setParameter("name", name)
                .getResultList();

        if (resultList.size() > 0) {
            return (Employe) resultList.get(0);
        }
        return null;
    }
}
