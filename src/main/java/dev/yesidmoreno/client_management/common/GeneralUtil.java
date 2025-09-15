package dev.yesidmoreno.client_management.common;

import java.time.LocalDate;
import java.time.Period;

public final class GeneralUtil {

    private GeneralUtil() {

    }

    public static boolean isAdult(LocalDate birthDate) {
        if (birthDate == null) return false;
        return Period.between(birthDate, LocalDate.now()).getYears() >= DomainConstants.LEGAL_ADULT_AGE;
    }

}
