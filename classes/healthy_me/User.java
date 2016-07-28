package healthy_me;

/**
 *
 * An implementation of the Student class.
 *
 * @author Julia Stoyanovich (stoyanovich@drexel.edu)
 *
 */
public class User {

    private int _user_id;
    private String _name;
    private int _age;
    private int _height;
    private int _weight;
    private String _gender;
    
    public User(String _name, int _age, int _height, int _weight, String _gender) {
        this._name = _name;
        this._age = _age;
        this._height = _height;
        this._weight = _weight;
        this._gender = _gender;
    }

    public int get_user_id() {
        return _user_id;
    }

    public void set_user_id(int _user_id) {
        this._user_id = _user_id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_age() {
        return _age;
    }

    public void set_age(int _age) {
        this._age = _age;
    }

    public int get_height() {
        return _height;
    }

    public void set_height(int _height) {
        this._height = _height;
    }

    public int get_weight() {
        return _weight;
    }

    public void set_weight(int _weight) {
        this._weight = _weight;
    }

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public String toString() {
        return String.format("%s:%s,%s,%s,%s,%s", _user_id, _name, _age, _height, _weight, _gender);
    }

    public String toHTML() {
        return "<tr><td>" + _user_id + "</td><td>" + _name + "</td><td>" + _age + "</td><td>" + _height + "</td><td>" + _weight + "</td><td>" + _gender + "</td></tr>";
    }
}