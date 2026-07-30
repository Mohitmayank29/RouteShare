package com.mohit.mapsone

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mohit.mapsone.Language.LocaleHelper
import com.mohit.mapsone.Screens.Login.LoginScreen
import com.mohit.mapsone.Screens.Splash.AnimatedSplashScreen
import com.mohit.mapsone.ui.theme.MapsoneTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prefs = getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        val language = prefs.getString(Constants.savedLanguage, "en") ?: "en"
        setContent {
            val configuration = LocalConfiguration.current
            val updatedConfiguration = Configuration(configuration).apply {
                setLocale(Locale(language))
            }
            MapsoneTheme {
                CompositionLocalProvider(
                    LocalConfiguration provides updatedConfiguration
                ) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        var isSplashScreenVisible by remember { mutableStateOf(true) }

                        if (isSplashScreenVisible) {
                            AnimatedSplashScreen(
                                onSplashFinished = {
                                    isSplashScreenVisible = false
                                },
                                Modifier.padding(innerPadding)
                            )
                        } else {
                            LoginScreen(
                                onLoginSuccess = { /* Handle login success */ },
                                onNavigateToSignUp = { /* Handle navigation to sign up */ },
                                onForgotPasswordClick = { /* Handle forgot password click */ }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences(
            "language_pref",
            Context.MODE_PRIVATE
        )

        val language = prefs.getString(Constants.savedLanguage, "hi") ?: "hi"
        Log.d("LANGUAGE", "Loaded: $language")
        super.attachBaseContext(
            LocaleHelper.setLocale(
                newBase,
                language
            )
        )
    }
}

@Composable
fun maonscreenb(modifier: Modifier = Modifier) {
    val realdatabase = FirebaseDatabase.getInstance()
    val reference = realdatabase.getReference(Constants.usertable)
    val userreferce = realdatabase.getReference(Constants.userlogindata)
    val userList = remember { mutableStateListOf<usersdata>() }
    LaunchedEffect(Unit) {

        userreferce.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                println("Children = ${snapshot.childrenCount}")


                for (child in snapshot.children) {

                    val user = child.getValue(usersdata::class.java)

                    if (user != null) {
                        userList.add(user)
                    }
                    println("List Size = ${userList.size}")

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    Column() {
        userddata(modifier)
        LazyColumn(
            Modifier.fillMaxWidth().padding(10.dp)
        ) {
            items(userList.size){ index ->
                viewdata(userList[index])

            }

        }
    }

}
@Composable
fun userddata(modifier: Modifier) {
    val realdatabase = FirebaseDatabase.getInstance()
    val reference = realdatabase.getReference(Constants.usertable)
    val userreferce = realdatabase.getReference(Constants.userlogindata)
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var loginuser by remember { mutableStateOf("") }
      loginuser = "2"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,  // Fixed Golden Color
                contentColor = Color.White
            ),
            onClick = {
            if(loginuser.equals("1",true)) {
                val id = reference.push().key!!

                val user = usersdata(
                    id = id,
                    name = username,
                    email = phone,
                    username = email
                )

                reference.child(id)
                    .setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Data Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }else
            {
                val id = userreferce.push().key!!

                val user = usersdata(
                    id = id,
                    name = username,
                    email = phone,
                    username = email
                )

                userreferce.child(id)
                    .setValue(user)
                    .addOnSuccessListener {
                        Toast.makeText(
                            context,
                            "Data Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            }
        ) {

            Text(stringResource(R.string.login))

        }

    }

}
data class usersdata(
    var id: String = "",
    var name: String = "",
    var email: String = "",
    var username: String = ""
)

@Composable
fun viewdata(item: usersdata) {
    Card(Modifier
        .fillMaxWidth()
        .padding()

    ) {
        Column() {
            Text(item.id)
            Text(item.username)
            Text(item.email)
            Text(item.name)
        }
    }

}
