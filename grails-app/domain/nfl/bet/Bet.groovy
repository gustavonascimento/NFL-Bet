package nfl.bet

class Bet {

    String type

    static belongsTo = [match: Match, team: Team]
    static hasOne = [result: Result]

    static constraints = {
    }
}
