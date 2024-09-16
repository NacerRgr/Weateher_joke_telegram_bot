package fr.ensim.interop.introrest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Joke {

    private Integer id;
    private String setup;
    private String delivery;
    private Integer note;

    @Override
    public String toString() {
        return setup  + "\n" + delivery + "\n Note : " + note + "/10 ";
    }
}
