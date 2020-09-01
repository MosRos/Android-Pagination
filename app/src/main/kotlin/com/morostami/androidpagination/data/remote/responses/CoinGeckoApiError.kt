/*
 * *
 *  * Created by Moslem Rostami on 3/19/20 2:55 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 3/19/20 2:55 PM
 *
 */

package com.morostami.androidpagination.data.remote.responses


import com.google.gson.annotations.SerializedName

data class CoinGeckoApiError(
    @SerializedName("error")
    var error: String =  "invalid vs_currency"
)