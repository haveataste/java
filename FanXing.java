class Util {
    static <K, V> boolean compare(Pair<K, V> p1, Pair<K, V> p2) {
        return p1.getKey().equals(p2.getKey()) && p1.getValue().equals(p2.getValue());
    }
}
class Pair<K, V> {
    private K key;
    private V value;
    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    void setKey(K key) { this.key = key; }
    void setValue(V value) { this.value = value; }
    K getKey() { return key; }
    V getValue() { return value; }
}
public class FanXing{
    public static void main(String args[]){
        Pair<Integer, String> p1 = new Pair<>(1, "apple");
        Pair<Integer, String> p2 = new Pair<>(2, "pear");
        Pair<Integer, String> p3 = new Pair<>(2, "pear");
        boolean diff = Util.compare(p1, p2);
        boolean same = Util.compare(p2, p3);
        System.out.println(diff);
        System.out.println(same);
    }
}
