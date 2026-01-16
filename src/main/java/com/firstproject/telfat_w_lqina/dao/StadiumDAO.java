package com.firstproject.telfat_w_lqina.dao;

import com.firstproject.telfat_w_lqina.models.Stadium;
import jakarta.persistence.EntityManager;

import static com.firstproject.telfat_w_lqina.util.HibernateUtil.getEntityManager;

public class StadiumDAO {
    public void saveStadium(Stadium stadium) {
        //Sauvgarder le stade dans la bd avec Hibernatte presistance
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(stadium);
            em.getTransaction().commit();
        }catch(Exception e){
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void updateStadium(Stadium stadium) {
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            em.merge(stadium);
            em.getTransaction().commit();
        }catch(Exception e){
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void deleteStadium(Long id) {
        EntityManager em = getEntityManager();
        try{
            em.getTransaction().begin();
            Stadium stadium = em.find(Stadium.class, id);
            if (stadium != null) {
                em.remove(stadium);
            }
            em.getTransaction().commit();
        }catch(Exception e){
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        }finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Stadium findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stadium.class, id);
        } finally {
            em.close();
        }
    }

    public boolean existsByName(String name) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(s) FROM Stadium s WHERE s.StadiumName = :name",
                            Long.class
                    ).setParameter("name", name)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    public boolean existsByNameExcludingId(String name, Long excludeId) {
        EntityManager em = getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(s) FROM Stadium s WHERE s.StadiumName = :name AND s.id != :id",
                            Long.class
                    )
                    .setParameter("name", name)
                    .setParameter("id", excludeId)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    public java.util.List<Stadium> getAllStadiums() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT s FROM Stadium s", Stadium.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
