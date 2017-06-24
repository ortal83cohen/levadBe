package com.query.social.app.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseUser;
import com.query.social.app.R;
import com.query.social.app.auth.AuthUI;
import com.query.social.app.auth.ErrorCodes;
import com.query.social.app.auth.IdpResponse;
import com.query.social.app.auth.ResultCodes;
import com.query.social.app.model.Question;
import com.query.social.app.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class MainActivity extends BaseActivity implements QuestionsListFragment.OnListItemListener
        , QuestionsListFragment.OnAddMoreItemButtonListener, AddQuestionFragment.OnFragmentInteractionListener {


    private int RC_SIGN_IN = 100;
    String QUESTION_LIST_FRAGMENT = "questionsListFragment";
    String ADD_QUESTION_FRAGMENT = "addQuestionFragment";
    QuestionsListFragment questionsListFragment;
    AddQuestionFragment addQuestionFragment;
    UserViewModel userViewModel;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        openQuestionListFragment();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        openQuestionListFragment();
                        break;
                    case R.id.navigation_dashboard:

                        break;
                    case R.id.navigation_user:
                     logInActivity();
                        break;
                }
                return false;
            }
        });


        userViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(@Nullable FirebaseUser firebaseUser) {
                BottomNavigationItemView checkBox = (BottomNavigationItemView) findViewById(R.id.navigation_user);
            }
        });

    }

    public void logInActivity() {
        if (userViewModel.getUser().getValue() == null) {
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
                    RC_SIGN_IN);
        } else {
            startSignedInActivity(null);
        }
    }


    private void openQuestionListFragment() {
        questionsListFragment = (QuestionsListFragment) getSupportFragmentManager().findFragmentByTag(
                QUESTION_LIST_FRAGMENT);
        if (questionsListFragment == null) {
            questionsListFragment = QuestionsListFragment.newInstance();
        }
        if (!questionsListFragment.isAdded()) {
            loadFragment(questionsListFragment, true, QUESTION_LIST_FRAGMENT);
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.content_frame, fragment, tag);
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        //}

    }


//    private fun openAddQuestionFragment() {
//        addQuestionFragment = supportFragmentManager.findFragmentByTag(
//                ADD_QUESTION_FRAGMENT) as AddQuestionFragment?
//        if (addQuestionFragment == null) {
//            addQuestionFragment = AddQuestionFragment.newInstance(questionsListFragment !!.
//            constructRevealSettings())
//        }
//        if (!addQuestionFragment !!.isAdded){
//            loadFragment(addQuestionFragment !!, true, ADD_QUESTION_FRAGMENT)
//        }
//    }


    static public Intent createIntent(Context context ) {
        return new Intent(context, MainActivity.class);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            userViewModel.updateUser();
            return;
        }

        showSnackbar(R.string.unknown_response);
    }


    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Successfully signed in
        if (resultCode == ResultCodes.OK) {
            startSignedInActivity(response);
//            finish()
            return;
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackbar(R.string.unknown_error);
                return;
            }
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }

    @Override public void onBackPressed() {
        if (addQuestionFragment!=null && addQuestionFragment.isAdded()) {
            (addQuestionFragment).dismiss(new AnimationUtils.Dismissible.OnDismissedListener() {
                @Override
                public void onDismissed() {
                    getSupportFragmentManager().beginTransaction().remove(addQuestionFragment).commitAllowingStateLoss();
                }
            });
        }else {
            super.onBackPressed();
        }
    }

    private void startSignedInActivity(IdpResponse response) {
        startActivity(
                SignedInActivity.createIntent(
                        this,
                        response,
                        new SignedInActivity.SignedInConfig(
                                getSelectedLogo(),
                                getSelectedTheme(),
                                getSelectedProviders(),
                                getSelectedTosUrl(),
                                true,
                                true)));
    }

    private String getSelectedTosUrl()

    {
        return "https://firebase.google.com/terms/";
    }

    private int getSelectedTheme()

    {
        return AuthUI.getDefaultTheme();
    }

    private int getSelectedLogo()

    {
        return R.drawable.logo_googleg_color_144dp;
    }


    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(findViewById(R.id.container), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<AuthUI.IdpConfig>();


        selectedProviders.add(new
                AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(getGooglePermissions())
                .build());

//            selectedProviders.add( todo
//                    AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
//                            .setPermissions(getFacebookPermissions())
//                            .build())

//            selectedProviders.add(AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build())

        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build());

        selectedProviders.add(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build());


        return selectedProviders;
    }

    @MainThread
    private List<String> getGooglePermissions() {
        List<String> result = new ArrayList<String>();

        result.add("https://www.googleapis.com/auth/youtube.readonly");

        result.add(Scopes.DRIVE_FILE);

        return result;
    }

    @MainThread
    private List<String> getFacebookPermissions() {
        List<String> result = new ArrayList<String>();

        result.add("user_friends");


        result.add("user_photos");

        return result;
    }

    private void openAddQuestionFragment() {
        addQuestionFragment = (AddQuestionFragment)getSupportFragmentManager().findFragmentByTag(
                ADD_QUESTION_FRAGMENT) ;
        if (addQuestionFragment == null) {
            addQuestionFragment = AddQuestionFragment.newInstance(questionsListFragment.constructRevealSettings());
        }
        if (!addQuestionFragment.isAdded()) {
            loadFragment(addQuestionFragment, true, ADD_QUESTION_FRAGMENT);
        }
    }
    @Override
    public void onListInteraction(Question item) {

    }

    @Override
    public void onAddItemInteraction() {
        openAddQuestionFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        onBackPressed();
    }
}
