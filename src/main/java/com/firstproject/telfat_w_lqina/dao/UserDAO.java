package com.firstproject.telfat_w_lqina.dao;

import com.firstproject.telfat_w_lqina.models.Agent;
import com.firstproject.telfat_w_lqina.models.User;
import com.firstproject.telfat_w_lqina.util.HibernateUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    //Insert user
    public boolean save(User user){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        boolean transaction = false;
        try {
            entityTransaction.begin();
            entityManager.persist(user);
            entityTransaction.commit();
            transaction = true;
        }
        catch (Exception exception){
            System.out.println("Erreur lors de la sauvegarde : " + exception.getMessage());
            exception.printStackTrace(); // Affiche la trace complète de l'erreur
            if (entityTransaction.isActive())
                entityTransaction.rollback();
        }
        finally {
            entityManager.close();
        }
        return transaction;
    }

    //SELECT User By UserName
    public User findByUsername(String username){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        User user = null;
        try {
            entityTransaction.begin();
            user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :u",User.class)
                    .setParameter("u", username).getSingleResult();
            entityTransaction.commit();
        }
        catch (Exception exception){
            if (entityTransaction.isActive())
                entityTransaction.rollback();
        }
        finally {
            entityManager.close();
        }
        return user;
    }

    //SELECT User By ID
    public User findById(long id){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        User user = null;
        try {
            entityTransaction.begin();
            user = entityManager.find(User.class,id);
            entityTransaction.commit();
        }
        catch (Exception exception){
            if (entityTransaction.isActive())
                entityTransaction.rollback();
        }
        finally {
            entityManager.close();
        }
        return user;
    }

    //REMOVE User
    public boolean remove (User user){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        boolean transaction = false;
        try {
            entityTransaction.begin();
            entityManager.remove(user);
            entityTransaction.commit();
            transaction = true;
        }
        catch (Exception exception){
            if (entityTransaction.isActive())
                entityTransaction.rollback();
        }
        finally {
            entityManager.close();
        }
        return transaction;
    }

    //Email already exists
    public long emailExists(String email){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        long isExists = 0L;
        try {
            entityTransaction.begin();
            isExists = entityManager.createQuery("SELECT count(u.id)from user u WHERE u.email = u",Long.class).setParameter("u" , email).getSingleResult();
            entityTransaction.commit();
        }catch (Exception exception){
            if (entityTransaction.isActive()){
                entityTransaction.rollback();
            }
        }finally {
            entityManager.close();
        }
        return isExists;
    }

    //COUNT NB USERS
    public int nbUsers(){
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        Long count = 0L;
        try {
            entityTransaction.begin();
            count = entityManager.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                    .getSingleResult();
        entityTransaction.commit();
        }catch (Exception exception){
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }        }
        finally {
            entityManager.close();
        }
        return count.intValue();
    }

    public List<Agent> getAllUsers(){
        EntityManager entityManager =HibernateUtil.getEntityManager();
        List<Agent> agents = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();
            agents = entityManager.createQuery("SELECT a FROM Agent a", Agent.class).getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }finally {
            entityManager.close();
            return agents;
        }
    }

    // UPDATE User
    public boolean update(User user) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        boolean transaction = false;
        try {
            entityTransaction.begin();
            entityManager.merge(user);
            entityTransaction.commit();
            transaction = true;
        } catch (Exception exception) {
            System.out.println("Erreur lors de la mise à jour : " + exception.getMessage());
            exception.printStackTrace();
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
        } finally {
            entityManager.close();
        }
        return transaction;
    }
}
