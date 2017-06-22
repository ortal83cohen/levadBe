package com.query.social.app.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.annotation.StringRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.google.android.gms.common.Scopes
import com.google.firebase.auth.FirebaseUser
import com.query.social.app.R
import com.query.social.app.auth.AuthUI
import com.query.social.app.auth.ErrorCodes
import com.query.social.app.auth.IdpResponse
import com.query.social.app.auth.ResultCodes
import com.query.social.app.ui.dummy.DummyContent
import com.query.social.app.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity() , QuestionFragment.OnListFragmentInteractionListener {
    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {

    }

    private val RC_SIGN_IN = 100

    companion object {
        fun createIntent(
                context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }


    private var userViewModel: UserViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java!!)
        openQuestionListFragment()
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    openQuestionListFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
//                kotlinx.android.synthetic.main.activity_main.message.setText(R.string.title_dashboard)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_user -> {
                    if (userViewModel!!.user.value == null) {
                        startActivityForResult(
                                AuthUI.getInstance().createSignInIntentBuilder()
                                        .setTheme(getSelectedTheme())
                                        .setLogo(getSelectedLogo())
                                        .setAvailableProviders(getSelectedProviders())
                                        .setTosUrl(getSelectedTosUrl())
                                        .setPrivacyPolicyUrl("https://firebase.google.com/terms/analytics/#7_privacy")
                                        .setIsSmartLockEnabled(true, true)
                                        .setAllowNewEmailAccounts(true)
                                        .build(),
                                RC_SIGN_IN)
                    } else {
                        startSignedInActivity(null)
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        userViewModel!!.user.observe(this, android.arch.lifecycle.Observer<FirebaseUser> { firebaseUser ->
            val checkBox = findViewById(R.id.navigation_user) as BottomNavigationItemView
//            checkBox.setTitle(firebaseUser!!.displayName)
        })

    }

    private fun loadFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (!fragment.isAdded) {
            fragmentTransaction.add(R.id.content_frame, fragment, tag)
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
        //}

    }

    val QUESTION_LIST_FRAGMENT = "questionFragment"
     var questionFragment: Fragment? = null

    private fun openQuestionListFragment() {
        questionFragment = supportFragmentManager.findFragmentByTag(
                QUESTION_LIST_FRAGMENT) as QuestionFragment?
        if (questionFragment == null) {
            questionFragment = QuestionFragment.newInstance()
        }
        if(!questionFragment!!.isAdded) {
            loadFragment(questionFragment!!, true, QUESTION_LIST_FRAGMENT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data)
            userViewModel!!.updateUser()
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


}
