package com.codding.test.startoverflowuser.network.respond

import com.google.gson.annotations.SerializedName

class SoFUser {
    @SerializedName("display_name")
    var userName = ""
    @SerializedName("profile_image")
    var profileImg = ""
    @SerializedName("location")
    var location = ""
    @SerializedName("last_access_date")
    var accessDate = ""
    @SerializedName("reputation")
    var reputation = 0

}