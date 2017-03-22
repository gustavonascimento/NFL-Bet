package nfl.bet

class Bet {

    String type

    static belongsTo = [match: Match, team: Team]

    static constraints = {
    }
}
