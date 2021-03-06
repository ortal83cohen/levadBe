package com.levadbe.berlin.app.ui;

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
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseUser;
import com.levadbe.berlin.app.R;
import com.levadbe.berlin.app.auth.AuthUI;
import com.levadbe.berlin.app.auth.ErrorCodes;
import com.levadbe.berlin.app.auth.IdpResponse;
import com.levadbe.berlin.app.auth.ResultCodes;
import com.levadbe.berlin.app.model.CurrencyConverterWidget;
import com.levadbe.berlin.app.model.LinkWidget;
import com.levadbe.berlin.app.model.Widget;
import com.levadbe.berlin.app.service.Router;
import com.levadbe.berlin.app.viewmodel.UserViewModel;
import com.levadbe.berlin.app.viewmodel.WidgetViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.levadbe.berlin.app.ui.WidgetListAdapter.WIDGET_TYPE_ADD_GROUP_WIDGET;
import static com.levadbe.berlin.app.ui.WidgetListAdapter.WIDGET_TYPE_CURRENCY_CONVERTER;
import static com.levadbe.berlin.app.ui.WidgetListAdapter.WIDGET_TYPE_FORM;
import static com.levadbe.berlin.app.ui.WidgetListAdapter.WIDGET_TYPE_LINK;
import static com.levadbe.berlin.app.ui.WidgetListAdapter.WIDGET_TYPE_MAP;

/**
 * Created by Ortal Cohen on 24/6/2017.
 */

public class MainActivity extends BaseActivity implements QuestionsListFragment.OnListItemListener
        , QuestionsListFragment.OnAddMoreItemButtonListener, AddQuestionFragment.OnFragmentInteractionListener,
        OnWidgetClickListener {


    String QUESTION_LIST_FRAGMENT = "questionsListFragment";
    String WIDGET_LIST_FRAGMENT = "widgetsListFragment";
    String ANSWER_FRAGMENT = "answerFragment";
    String ADD_QUESTION_FRAGMENT = "addQuestionFragment";
    String ADD_GROUP_WIDGET_FRAGMENT = "addGroupWidgetFragment";
    String CURRENCY_WIDGET_FRAGMENT = "CurrencyWidgetFragment";
    QuestionsListFragment questionsListFragment;
    WidgetListFragment widgetListFragment;
    AddQuestionFragment addQuestionFragment;
    AddGroupWidgetsFragment addGroupWidgetsFragment;
    CurrencyWidgetsFragment currencyWidgetsFragment;
    AnswerFragment answerFragment;
    UserViewModel userViewModel;
    WidgetViewModel widgetViewModel;
    private int RC_SIGN_IN = 100;
    private BottomNavigationView navigation;

    static public Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_questions);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.init(this);
        widgetViewModel = ViewModelProviders.of(this).get(WidgetViewModel.class);
        widgetViewModel.init(this);
        userViewModel.getGroups().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {

                widgetViewModel.addGroupWidgets(strings);


            }
        });


        Router.handel(this, getIntent().getData());

        openMainFragment();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_home:
//                        openQuestionListFragment();
                        openMainFragment();
                        return true;
                    case R.id.navigation_dashboard:
                        return true;
                    case R.id.navigation_user:
                        logInActivity();
                        return false;
                    default:
                        return false;
                }

            }
        });


        userViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(@Nullable FirebaseUser firebaseUser) {
                BottomNavigationItemView checkBox = (BottomNavigationItemView) findViewById(R.id.navigation_user);
            }
        });

    }

    private void openMainFragment() {
        widgetListFragment = (WidgetListFragment) getSupportFragmentManager().findFragmentByTag(
                WIDGET_LIST_FRAGMENT);
        if (widgetListFragment == null) {
            widgetListFragment = WidgetListFragment.newInstance();
        }
        if (!widgetListFragment.isAdded()) {
            loadFragment(widgetListFragment, true, false, WIDGET_LIST_FRAGMENT);
        }
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
            loadFragment(questionsListFragment, true, false, QUESTION_LIST_FRAGMENT);
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack, boolean removeOldStack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!fragment.isAdded()) {
            fragmentTransaction.replace(R.id.content_frame, fragment, tag);
        }
        if (removeOldStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

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

    @Override
    public void onBackPressed() {
        if (addQuestionFragment != null && addQuestionFragment.isAdded()) {
            (addQuestionFragment).dismiss(new AnimationUtils.Dismissible.OnDismissedListener() {
                @Override
                public void onDismissed() {
                    if (questionsListFragment == null) {
                        questionsListFragment = QuestionsListFragment.newInstance();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, questionsListFragment).commitAllowingStateLoss();
                }
            });
        } else {
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
        addQuestionFragment = (AddQuestionFragment) getSupportFragmentManager().findFragmentByTag(
                ADD_QUESTION_FRAGMENT);
        if (addQuestionFragment == null) {
            addQuestionFragment = AddQuestionFragment.newInstance(questionsListFragment.constructRevealSettings());
        }
        if (!addQuestionFragment.isAdded()) {
            loadFragment(addQuestionFragment, true, false, ADD_QUESTION_FRAGMENT);
        }
    }

    private void openQuestionItemFragment(QuestionItemRecyclerViewAdapter.ViewHolder holder) {
        answerFragment = (AnswerFragment) getSupportFragmentManager().findFragmentByTag(
                ANSWER_FRAGMENT);
        if (answerFragment == null) {
            answerFragment = AnswerFragment.newInstance(holder.color, "");
        }
        if (!answerFragment.isAdded()) {
            ConstraintLayout root = holder.mRoot;
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (!answerFragment.isAdded()) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                answerFragment.setBackgroundColor(holder.color);
                ViewCompat.setTransitionName(root, "detailsframe");

                fragmentTransaction.addSharedElement(root, ViewCompat.getTransitionName(root));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.content_frame, answerFragment, ANSWER_FRAGMENT);
                fragmentTransaction.commit();
            }
        }
    }

    @Override
    public void onQuestionItemSelected(QuestionItemRecyclerViewAdapter.ViewHolder item) {
        openQuestionItemFragment(item);
    }

    @Override
    public void onAddItemInteraction() {
        openAddQuestionFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        onBackPressed();
    }

    @Override
    public void onWidgetClicked(View v, Widget widget) {
        switch (widget.type) {
            case WIDGET_TYPE_FORM:
                openQuestionListFragment();
                break;
            case WIDGET_TYPE_MAP:
                openMap();
                break;
            case WIDGET_TYPE_ADD_GROUP_WIDGET:
                openAddGroupWidget();
                break;
            case WIDGET_TYPE_CURRENCY_CONVERTER:
                openCurrencyCalculator(Float.valueOf(((CurrencyConverterWidget) widget).text));
                break;
            case WIDGET_TYPE_LINK:
                openLink(((LinkWidget) widget).link);
                break;
        }

    }

    private void openCurrencyCalculator(Float aFloat) {
        currencyWidgetsFragment = (CurrencyWidgetsFragment) getSupportFragmentManager().findFragmentByTag(
                CURRENCY_WIDGET_FRAGMENT);
        if (currencyWidgetsFragment == null) {
            currencyWidgetsFragment = CurrencyWidgetsFragment.newInstance(aFloat);
        }
        if (!currencyWidgetsFragment.isAdded()) {
            loadFragment(currencyWidgetsFragment, true, false, CURRENCY_WIDGET_FRAGMENT);
        }
    }

    private void openAddGroupWidget() {
        addGroupWidgetsFragment = (AddGroupWidgetsFragment) getSupportFragmentManager().findFragmentByTag(
                ADD_GROUP_WIDGET_FRAGMENT);
        if (addGroupWidgetsFragment == null) {
            addGroupWidgetsFragment = AddGroupWidgetsFragment.newInstance();
        }
        if (!addGroupWidgetsFragment.isAdded()) {
            loadFragment(addGroupWidgetsFragment, true, false, ADD_GROUP_WIDGET_FRAGMENT);
        }
    }
    private void openLink(String url) {
        Intent intent= new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);
    }

    private void openMap() {
        startActivity(MapsActivity.createIntent(this));
    }
}
