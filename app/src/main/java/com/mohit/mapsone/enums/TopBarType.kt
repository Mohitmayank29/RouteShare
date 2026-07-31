package com.mohit.mapsone.enums

enum class TopBarType {
    DASHBOARD_LARGE, // For Home / Main Screen with welcome message & big title
    DASHBOARD_SMALL, // Centered Top Bar with menu & icons
    BACK_ONLY,       // Default: Back Arrow + Title (+ Optional Search/Filter)
    TITLE_ONLY       // Plain Title without back navigation
}