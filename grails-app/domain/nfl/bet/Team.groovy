package nfl.bet

class Team {

	String name

	static belongsTo = [championship: Championship]

    static constraints = {
    }
}
