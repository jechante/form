package com.schinta.busi;

import com.codahale.metrics.annotation.Timed;
import com.schinta.domain.FormSubmit;
import com.schinta.repository.FormSubmitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.List;




//@RestController
public class DealSubmit {

    private final Logger log = LoggerFactory.getLogger(DealSubmit.class);

    private final FormSubmitRepository formSubmitRepository;

    public DealSubmit(FormSubmitRepository formSubmitRepository) {
        this.formSubmitRepository = formSubmitRepository;
    }


    public void LoadSubmitByDealFlag(String DealFlag) throws URISyntaxException, JSONException {
        log.debug("REST request to LoadSubmitByDealFlag : {}", DealFlag);
        List<FormSubmit> unDealSubmits;

        unDealSubmits = formSubmitRepository.findByDealFlag(DealFlag);
        for (int i=0; i < unDealSubmits.size() ; i++){
            System.out.println(unDealSubmits.toString());
        }
        JSONObject jsStr = null;
        for (int k = 0 ; k < unDealSubmits.size(); k ++ ){
            FormSubmit oneItem = unDealSubmits.get(k);

            try {
                jsStr =  new JSONObject(oneItem.getSubmitJosn());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!ObjectUtils.isEmpty(jsStr)){
                String formID = jsStr.getString("form");
                String formName = jsStr.getString("form_name");
                JSONObject formEntry = jsStr.getJSONObject("entry");
                int serialNumber = formEntry.getInt("serial_number");
                JSONArray entryArray = jsStr.getJSONArray("entry");
                for (int j = 0 ; j < entryArray.length()  ; j ++){
                    System.out.println(String.valueOf(j) + " : " + entryArray.get(j).toString());
                }
            }

        }


    }

}
