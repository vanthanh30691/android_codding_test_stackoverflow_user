package com.codding.test.startoverflowuser.network.respond

import com.codding.test.startoverflowuser.modal.SoFUser
import com.google.gson.annotations.SerializedName


open class BaseRespond {
    @SerializedName("has_more")
    var hasMore : Boolean = true

}