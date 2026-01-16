package com.firstproject.telfat_w_lqina.service;

import com.firstproject.telfat_w_lqina.Enum.TypeState;
import com.firstproject.telfat_w_lqina.dao.LostObjectDAO;
import com.firstproject.telfat_w_lqina.models.LostObject;
import com.firstproject.telfat_w_lqina.models.Person;
import com.firstproject.telfat_w_lqina.models.Proof;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.CheckSyntaxe;
import com.firstproject.telfat_w_lqina.util.ImageConverterUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class LostObjectService {
    private final LostObjectDAO lostObjectDAO = new LostObjectDAO();
    private final LocalDate today = LocalDate.now();

    public void addLostObject(LostObject lostObject , File selectedImageFile) throws IOException {
        // Validation métier ici
        if(lostObject.getLostDate().isAfter(today)){
            Alerts.errorAlert("Date invalide","Impossible d'enregistrer l'objet perdu","La date ne peut pas être dans le futur.");
            return;
        }
        if(lostObject.getAgentName() == null || lostObject.getAgentName().isBlank()){
            Alerts.errorAlert("Nom manquant","Agent non renseigné","Veuillez saisir le nom de l'agent.");
            return;
        }
        if(!CheckSyntaxe.checkSyntaxeEmail(lostObject.getEmail())){
            Alerts.errorAlert("Email invalide","Impossible d'enregistrer l'objet perdu","Veuillez saisir un email valide.");
            return;
        }
        if(!CheckSyntaxe.checkSyntaxeTelephone(lostObject.getPhone())){
            Alerts.errorAlert("Téléphone invalide","Impossible d'enregistrer l'objet perdu","Veuillez saisir un numéro de téléphone valide.");
            return;
        }

        // Toutes les validations sont passées, on peut sauvegarder
        byte[] imageData = ImageConverterUtil.convertImageToByte(selectedImageFile);
        lostObject.setImage(imageData);
        lostObjectDAO.save(lostObject);
    }

    public List<LostObject> getAllLostObjects() {
        return lostObjectDAO.getAllLostObjects();
    }

    public List<LostObject> getLostObjectsByOwnerName(String ownerName , List<LostObject> lostObjects ) {
        List<LostObject> lostObjectsByOwnerName = new ArrayList<>();
        for (LostObject lostObject : lostObjects){
            if (lostObject.getAgentName().equals(ownerName)){
                lostObjectsByOwnerName.add(lostObject);
            }
        }
        return lostObjectsByOwnerName;
    }

    public List<LostObject> getLostObjectsByDate(LocalDate date , List<LostObject> lostObjects) {
        List<LostObject> lostObjectsByDate = new ArrayList<>();
        for (LostObject lostObject : lostObjects){
           if (lostObject.getLostDate().equals(date)){
               lostObjectsByDate.add(lostObject);
           }
        }
        return lostObjectsByDate;
    }

    public List<LostObject> getLostObjectsByStadium(String stadiumName , List<LostObject> lostObjects){
        List<LostObject> lostObjectListByStadium = new ArrayList<>();
        for (LostObject lostObject : lostObjects){
            if (lostObject.getZone().equals(stadiumName)){
                lostObjectListByStadium.add(lostObject);
            }
        }
        return lostObjectListByStadium;
    }


    public void removeLostObject(LostObject lostObject){
        boolean success = lostObjectDAO.remove(lostObject);
        if (success) {
            Alerts.successAlert("Suppression réussie", "L'objet perdu a été supprimé avec succès.", null);
        } else {
            Alerts.errorAlert("Erreur de suppression", "Impossible de supprimer l'objet perdu.", "L'objet n'a pas été trouvé dans la base de données.");
        }
    }

    public void updateLostObject(LostObject lostObject) {
        lostObjectDAO.update(lostObject);
        Alerts.successAlert(
                "Modification réussie",
                "Les informations ont été modifiées avec succès.",
                null
        );
    }

    /**
     * Marque un objet perdu comme rendu/trouvé
     * @param lostObject L'objet perdu à mettre à jour
     * @param owner Le propriétaire qui récupère l'objet
     * @param proof La preuve de présence au stade
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean markAsReturned(LostObject lostObject, Person owner, Proof proof) {
        try {
            // Validation des données du propriétaire
            if (owner.getFirstName() == null || owner.getFirstName().isBlank()) {
                Alerts.errorAlert("Erreur", "Prénom manquant", "Veuillez saisir le prénom du propriétaire.");
                return false;
            }
            if (owner.getLastName() == null || owner.getLastName().isBlank()) {
                Alerts.errorAlert("Erreur", "Nom manquant", "Veuillez saisir le nom du propriétaire.");
                return false;
            }
            if (!CheckSyntaxe.checkSyntaxeEmail(owner.getEmail())) {
                Alerts.errorAlert("Erreur", "Email invalide", "Veuillez saisir un email valide.");
                return false;
            }
            if (!CheckSyntaxe.checkSyntaxeTelephone(owner.getTelephone())) {
                Alerts.errorAlert("Erreur", "Téléphone invalide", "Veuillez saisir un numéro de téléphone valide.");
                return false;
            }
            if (owner.getIdentityDocument() == null ||
                owner.getIdentityDocument().getDocumentNumber() == null ||
                owner.getIdentityDocument().getDocumentNumber().isBlank()) {
                Alerts.errorAlert("Erreur", "Document manquant", "Veuillez saisir le numéro du document d'identité.");
                return false;
            }

            // Validation de la preuve
            if (proof.getProofImage() == null || proof.getProofImage().length == 0) {
                Alerts.errorAlert("Erreur", "Preuve manquante", "Veuillez ajouter une image de preuve de présence.");
                return false;
            }

            // Mettre à jour l'objet
            lostObject.setTypeState(TypeState.RETURNED);
            lostObject.setOwner(owner);
            lostObject.setProof(proof);

            // Sauvegarder
            boolean success = lostObjectDAO.update(lostObject);

            if (success) {
                Alerts.successAlert("Succès", "Objet marqué comme rendu",
                    "L'objet a été marqué comme rendu au propriétaire " + owner.getFirstName() + " " + owner.getLastName() + ".");
            } else {
                Alerts.errorAlert("Erreur", "Échec de la mise à jour", "Impossible de mettre à jour l'objet.");
            }

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            Alerts.errorAlert("Erreur", "Erreur système", "Une erreur s'est produite: " + e.getMessage());
            return false;
        }
    }

    public List<LostObject> getLostObjectsByState(TypeState typeState , List<LostObject> lostObjects){
        List<LostObject> lostObjectListByState = new ArrayList<>();
        for (LostObject lostObject : lostObjects){
            if (lostObject.getTypeState() == typeState){
                lostObjectListByState.add(lostObject);
            }
        }
        return lostObjectListByState;
    }

    public List<LostObject> getLostObjectsByType(String type , List<LostObject> lostObjects){
        List<LostObject> lostObjectListByType = new ArrayList<>();
        for (LostObject lostObject : lostObjects){
            if (lostObject.getType().equals(type)){
                lostObjectListByType.add(lostObject);
            }
        }
        return lostObjectListByType;
    }
}
