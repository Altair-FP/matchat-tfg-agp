package andres.personal.matchat.models

class UserDTO {
    var name: String? = null

    var uid: String? = null

    constructor() {}
    constructor(name: String?, uid: String?) {
        this.name = name
        this.uid = uid
    }
}