package parser;


public interface Parser<T, K> {

    K parse(T parsed);
}
