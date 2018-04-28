/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.damdrempe.web.kontrole;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author ddrempetic
 */
@FacesValidator("org.foi.nwtis.damdrempe.web.kontrole.JsonValidator")
public class JsonValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        FacesMessage msg = new FacesMessage("Nije ispravan JSON");
	msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        
        boolean rezultatProvjere = PomocnaKlasa.ValidirajJsonIzStringa(value.toString());
        
        if(rezultatProvjere == false){
            throw new ValidatorException(msg);
        }
    }
    
}
