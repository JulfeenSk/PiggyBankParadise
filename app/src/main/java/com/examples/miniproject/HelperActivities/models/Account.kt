package com.examples.miniproject.HelperActivities.models

class Account {
    var accountAmount: Double = 0.0
    var accountName: String? = null


    constructor()

    constructor(accountAmount: Double, accountName: String) {
        this.accountAmount = accountAmount
        this.accountName = accountName
    }
}

