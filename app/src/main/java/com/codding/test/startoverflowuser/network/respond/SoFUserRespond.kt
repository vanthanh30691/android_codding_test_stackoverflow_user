package com.codding.test.startoverflowuser.network.respond

import com.google.gson.annotations.SerializedName

class SoFUserRespond {
    @SerializedName("items")
    var sofUserList : List<SoFUser> = emptyList()
}