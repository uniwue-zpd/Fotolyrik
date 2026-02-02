package de.uniwue.dachs.fotolyrik_backend.utils.mapper;


import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

@Component
public class PublicationDateMapper {
    public String DateToDateWithoutDashes(String date){
        // from Y-m-d to dd.mm.YYYY
        if (date == null || !date.contains("-")){
            return date;
        }
        var dashSplit = Arrays.asList(date.split("-"));
        Collections.reverse(dashSplit);
        return String.join(".", dashSplit);
    }
}
