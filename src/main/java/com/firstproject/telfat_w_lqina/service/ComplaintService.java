package com.firstproject.telfat_w_lqina.service;

import com.firstproject.telfat_w_lqina.dao.ComplaintDAO;
import com.firstproject.telfat_w_lqina.exception.NotFoundException.ObjectNotFoundException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidEmailException;
import com.firstproject.telfat_w_lqina.exception.validationexception.InvalidPhoneException;
import com.firstproject.telfat_w_lqina.models.Complaint;
import com.firstproject.telfat_w_lqina.util.Alerts;
import com.firstproject.telfat_w_lqina.util.CheckSyntaxe;

import java.util.List;

public class ComplaintService {
    private CheckSyntaxe checkSyntaxe = new CheckSyntaxe();
    private ComplaintDAO complaintDAO = new ComplaintDAO();

    public Boolean createComplaint(Complaint complaint) {
        if (checkSyntaxe.checkSyntaxeEmail(complaint.getPerson().getEmail())==false){
            throw new InvalidEmailException("Invalid email format: " + complaint.getPerson().getEmail());
        }
        if (checkSyntaxe.checkSyntaxeTelephone(complaint.getPerson().getTelephone())==false){
            throw new InvalidPhoneException("Invalid telephone format: " + complaint.getPerson().getTelephone());
        }
        return complaintDAO.create(complaint);}

    public List<Complaint> getAllComplaints(){
        return complaintDAO.getAllComplaints();
    }

    public void removeComplaint(long complaintID){
        Complaint complaint = complaintDAO.getComplaintById(complaintID);
        if(complaint != null){
            boolean deleted = complaintDAO.removeComplaint(complaint);
            if (!deleted) {
                throw new RuntimeException("Failed to delete complaint with ID " + complaintID);
            }
        } else {
            throw new ObjectNotFoundException("Complaint with ID " + complaintID + " not found.");
        }
    }

    public Boolean updateComplaint(Complaint complaint) {
        if (!checkSyntaxe.checkSyntaxeEmail(complaint.getPerson().getEmail())) {
            throw new InvalidEmailException("Invalid email format: " + complaint.getPerson().getEmail());
        }
        if (!checkSyntaxe.checkSyntaxeTelephone(complaint.getPerson().getTelephone())) {
            throw new InvalidPhoneException("Invalid telephone format: " + complaint.getPerson().getTelephone());
        }
        return complaintDAO.update(complaint);
    }
}
