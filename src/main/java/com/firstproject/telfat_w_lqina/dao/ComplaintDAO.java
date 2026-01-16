package com.firstproject.telfat_w_lqina.dao;

import com.firstproject.telfat_w_lqina.models.Complaint;
import com.firstproject.telfat_w_lqina.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    public boolean create (Complaint complaint){
        boolean is_created = false;
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(complaint);
            entityTransaction.commit();
            is_created = true;
        } catch (Exception exception) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return is_created;
    }

    public List<Complaint> getAllComplaints(){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        List<Complaint> complaints = new ArrayList<>();
        try {
            entityTransaction.begin();

            // FIXED: Use JOIN FETCH to load all lazy relationships eagerly
            // This prevents LazyInitializationException
            complaints = entityManager.createQuery(
                    "SELECT DISTINCT c FROM Complaint c " +
                            "LEFT JOIN FETCH c.person p " +
                            "LEFT JOIN FETCH p.identityDocument " +
                            "LEFT JOIN FETCH c.proof " +
                            "LEFT JOIN FETCH c.baseObject",
                    Complaint.class
            ).getResultList();

            entityTransaction.commit();
        } catch (Exception exception) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return complaints;
    }

    public Complaint getComplaintById(long complaintID){
        EntityManager entityManager=HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction =entityManager.getTransaction();
        Complaint complaint=null;
        try {
            entityTransaction.begin();
            complaint=entityManager.find(Complaint.class,complaintID);
            entityTransaction.commit();
        } catch (Exception exception) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        return complaint;
    }

    public boolean removeComplaint(Complaint complaint){
        EntityManager entityManager=HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction =entityManager.getTransaction();
        boolean is_deleted=false;
        try {
            entityTransaction.begin();
            entityManager.remove(entityManager.contains(complaint) ? complaint : entityManager.merge(complaint));
            entityTransaction.commit();
            is_deleted=true;
        } catch (Exception exception) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }}finally {
            entityManager.close();
            return is_deleted;
        }

    }

    public boolean update(Complaint complaint) {
        boolean is_updated = false;
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.merge(complaint);
            entityTransaction.commit();
            is_updated = true;
        } catch (Exception exception) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            exception.printStackTrace();
        } finally {
            entityManager.close();
        }
        return is_updated;
    }
}