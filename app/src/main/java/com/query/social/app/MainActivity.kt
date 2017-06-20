package com.query.social.app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.Scopes
import com.query.social.app.auth.*
import com.query.social.app.auth.ui.ExtraConstants
import com.query.social.app.auth.ui.FlowParameters
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100;
    companion object {
        fun createIntent(
                context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                message.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                message.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user -> {

                startActivityForResult(
                        AuthUI.getInstance().createSignInIntentBuilder()
                                .setTheme(getSelectedTheme())
                                .setLogo(getSelectedLogo())
                                .setAvailableProviders(getSelectedProviders())
                                .setTosUrl(getSelectedTosUrl())
                                .setPrivacyPolicyUrl("https://firebase.google.com/terms/analytics/#7_privacy")
                                .setIsSmartLockEnabled(true,true)
                                .setAllowNewEmailAccounts(true)
                                .build(),
                        RC_SIGN_IN)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data)
            return
        }

        showSnackbar(R.string.unknown_response)
    }


    @MainThread
    private fun handleSignInResponse(resultCode: Int, data: Intent) {
        val response = IdpResponse.fromResultIntent(data)

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startSignedInActivity(response)
//            finish()
            return
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled)
                return
            }

            if (response.errorCode == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection)
                return
            }

            if (response.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error)
                return
            }
        }

        showSnackbar(R.string.unknown_sign_in_response)
    }

    private fun startSignedInActivity(response: IdpResponse?) {
        startActivity(
                SignedInActivity.createIntent(
                        this,
                        response,
                        SignedInActivity.SignedInConfig(
                                getSelectedLogo(),
                                getSelectedTheme(),
                                getSelectedProviders(),
                                getSelectedTosUrl(),
                              true,
                              true)))
    }

    private fun getSelectedTosUrl(): String? {
       return "https://firebase.google.com/terms/"
    }

    private fun getSelectedTheme(): Int {
       return AuthUI.getDefaultTheme()
    }

    private fun getSelectedLogo(): Int {
     return R.drawable.logo_googleg_color_144dp
    }


    @MainThread
    private fun showSnackbar(@StringRes errorMessageRes: Int) {
        Snackbar.make(findViewById(R.id.container), errorMessageRes, Snackbar.LENGTH_LONG).show()
    }

    private fun getSelectedProviders(): MutableList<AuthUI.IdpConfig> {
        val selectedProviders = ArrayList<AuthUI.IdpConfig>()


            selectedProviders.add(
                    AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                            .setPermissions(getGooglePermissions())
                            .build())

//            selectedProviders.add( todo
//                    AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
//                            .setPermissions(getFacebookPermissions())
//                            .build())

//            selectedProviders.add(AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build())

            selectedProviders.add(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

            selectedProviders.add(
                    AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build())


        return selectedProviders
    }

    @MainThread
    private fun getGooglePermissions(): List<String> {
        val result = ArrayList<String>()

            result.add("https://www.googleapis.com/auth/youtube.readonly")

            result.add(Scopes.DRIVE_FILE)

        return result
    }

    @MainThread
    private fun getFacebookPermissions(): List<String> {
        val result = ArrayList<String>()

            result.add("user_friends")


            result.add("user_photos")

        return result
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
