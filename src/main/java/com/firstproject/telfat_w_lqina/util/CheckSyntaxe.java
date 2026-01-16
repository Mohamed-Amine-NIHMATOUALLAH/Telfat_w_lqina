package com.firstproject.telfat_w_lqina.util;

public class CheckSyntaxe {

    public static boolean checkSyntaxeEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z.-]+\\.(com|ma|org|net|edu)$";
        return email != null && email.matches(regex);
    }

    public static boolean checkSyntaxeTelephone(String telephone) {
        if (telephone == null) return false;

        telephone = telephone.trim();

        // Cas 1 : numéro marocain commençant par 0 (10 chiffres)
        if (telephone.startsWith("0")) {
            return telephone.matches("0[0-9]{9}");
        }

        // Cas 2 : numéro international +212 (9 chiffres après)
        if (telephone.startsWith("+212")) {
            String rest = telephone.substring(4).trim();
            return rest.matches("[0-9]{9}");
        }

        return false;
    }

    public boolean checkSyntaxePassword(String password) {
        if (password == null) return false;
        return password.trim().length() >= 8;
    }



}
