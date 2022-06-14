package andres.personal.matchat.models

public class ChatModel {
    var name: String? = null
    var users: String? = null
    var uid: String? = null
    var fCreate: String? = null

    constructor() {}
    constructor(name: String?, users: String?, fCreate: String?) {
        this.name = name
        this.users = users
        this.fCreate = fCreate
    }

}