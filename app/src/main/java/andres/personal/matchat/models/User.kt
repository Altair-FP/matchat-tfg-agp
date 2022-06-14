package andres.personal.matchat.models


//el modelo que tendran los usuarios
class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var rolid: String? = null

    constructor() {}
    constructor(name: String?, email: String?, uid: String?) {
        this.name = name
        this.email = email
        this.uid = uid
        this.rolid = "3"
    }
}