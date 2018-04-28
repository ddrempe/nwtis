package org.foi.nwtis.damdrempe.web.kontrole;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * Validator na klijentskoj strani za sadržaj privitka.
 * Provjerava da li je uneseni sadržaj ispravan JSON.
 * @author ddrempetic
 */
@FacesValidator("org.foi.nwtis.damdrempe.web.kontrole.JsonValidator")
public class JsonValidator implements Validator {

    /**
     * Provjerava da li je uneseni sadržaj ispravan JSON.
     * @param context
     * @param component
     * @param value
     * @throws ValidatorException 
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        FacesMessage poruka = new FacesMessage("Nije ispravan JSON");
	poruka.setSeverity(FacesMessage.SEVERITY_ERROR);
        
        boolean rezultatProvjere = PomocnaKlasa.ValidirajJsonIzStringa(value.toString());
        
        if(rezultatProvjere == false){
            throw new ValidatorException(poruka);
        }
    }    
}