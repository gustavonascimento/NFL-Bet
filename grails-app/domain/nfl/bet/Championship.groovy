package nfl.bet

class Championship {

    String championshipNumber
    Integer year

    static hasMany = [rounds: Round, teans: Team]

    static constraints = {
    }
}
