package com.codding.test.startoverflowuser.network.respond

import com.codding.test.startoverflowuser.modal.Reputation
import com.google.gson.annotations.SerializedName

class ReputationRespond : BaseRespond() {
    @SerializedName("items")
    var repuList : List<Reputation> = emptyList()
}