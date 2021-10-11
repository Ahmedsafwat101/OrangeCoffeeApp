package com.orangecoffeeapp.utils.admission

sealed class AdmissionEvents {

    /**
     * Represent the intents of my action
     * and for each one of them I have 3 States
     * Success, Error, and Idle
     **/

    //Login
    object ProceedLogIn: AdmissionEvents()
    //SinIN
    object ProceedSignIn: AdmissionEvents()

}