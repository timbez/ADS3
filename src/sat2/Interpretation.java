package sat2;

import clause.Atom;

import java.util.HashMap;
import java.util.Map;

public class Interpretation {

    private Map<Atom, Boolean> mapping = new HashMap<>();

    public Interpretation() {
    }

    public Interpretation(Map<Atom, Boolean> mapping) {
        this.mapping = mapping;
    }

    @Override
    public String toString() {
        return "Interpretation: " + mapping;
    }
}
