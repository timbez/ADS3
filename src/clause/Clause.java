package clause;

public interface Clause {

    Literal getFirst();

    Literal getSecond();

    void setFirst(Literal lit);

    void setSecond(Literal second);

    Clause clone();
}
