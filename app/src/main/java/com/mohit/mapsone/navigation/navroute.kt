package com.mohit.mapsone.navigation

sealed class navroute(val route: String, ){
    object Splash : navroute("splash")
    object Login : navroute("login")
    object signup : navroute("register")
    object loginsignup : navroute("loginsignup")
    object Dashboard : navroute("dashboard")
    object Settings : navroute("settings")
    object Help : navroute("help")
    object About : navroute("about")
    object Contact : navroute("contact")
    object Feedback : navroute("feedback")
    object notification : navroute("notification")
    object language : navroute("laguageScreen")
    object history : navroute("History")

}

