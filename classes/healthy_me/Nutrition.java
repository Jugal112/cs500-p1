package healthy_me;

/**
 * Created by jugal on 7/28/2016.
 */
public class Nutrition {

    private int _meal_id;
    private String _name;
    private int _calories;
	
    public Nutrition(String _name, int _calories) {
        this._name = _name;
        this._calories = _calories;
    }

    public int get_meal_id() {
        return _meal_id;
    }

    public void set_meal_id(int _meal_id) {
        this._meal_id = _meal_id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_calories() {
        return _calories;
    }

    public void set_calories(int _calories) {
        this._calories = _calories;
    }

    public String toString() {
        return String.format("%s:%s", _name, _calories);
    }

    public String toHTML() {
        return "<tr><td>" + _name + "</td><td>" + _calories + "</td></tr>";
    }
}
