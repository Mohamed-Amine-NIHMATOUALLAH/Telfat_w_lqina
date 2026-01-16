package com.firstproject.telfat_w_lqina.dao;

import com.firstproject.telfat_w_lqina.models.LostObject;
import com.firstproject.telfat_w_lqina.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class LostObjectDAO {
    public void save(LostObject lostObject) {
        // Persistance avec Hibernate
        EntityManager em = HibernateUtil.getEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(lostObject);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la sauvegarde de l'objet perdu: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public List<LostObject> getAllLostObjects() {
        EntityManager em = HibernateUtil.getEntityManager();
        List<LostObject> lostObjects = new ArrayList<>();
        try {
            TypedQuery<LostObject> query = em.createQuery("SELECT l FROM LostObject l ORDER BY l.lostDate DESC", LostObject.class);
            lostObjects = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return lostObjects;
    }


    public LostObject getLostObjectById(Long id) {
        EntityManager em = HibernateUtil.getEntityManager();
        LostObject lostObject = null;
        try {
            lostObject = em.find(LostObject.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return lostObject;
    }
    public boolean remove(LostObject lostObject){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            LostObject toBeRemoved = entityManager.find(LostObject.class, lostObject.getId());
            if (toBeRemoved != null) {
                entityManager.remove(toBeRemoved);
                entityTransaction.commit();
                return true;
            } else {
                entityTransaction.rollback();
                return false;
            }
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            entityManager.close();

        }
    }

    public boolean update(LostObject lostObject){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(lostObject);
            entityTransaction.commit();
            return true;
        } catch (Exception e) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            entityManager.close();
        }
    }

}

