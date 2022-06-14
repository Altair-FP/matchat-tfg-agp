package andres.personal.matchat.models

open class GameModel {

    var uid: String? = null
    var gameModeid: String? = null
    var fecCreacion: String? = null
    var fecFin: String? = null
    var users: String? = null
    var enable: Boolean = false

    constructor() {}
    constructor(gameModeid: String?, fecCreacion: String?,users: String?) {
        this.gameModeid  = gameModeid
        this.fecCreacion = fecCreacion
        this.users       = users
    }


}