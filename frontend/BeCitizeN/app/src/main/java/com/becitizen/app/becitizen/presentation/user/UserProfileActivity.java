package com.becitizen.app.becitizen.presentation.user;

import android.accounts.NetworkErrorException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.becitizen.app.becitizen.R;
import com.becitizen.app.becitizen.domain.entities.Conversation;
import com.becitizen.app.becitizen.exceptions.ServerException;
import com.becitizen.app.becitizen.exceptions.SharedPreferencesException;
import com.becitizen.app.becitizen.presentation.controllers.ControllerUserPresentation;
import com.becitizen.app.becitizen.presentation.controllers.ControllerMsgPresentation;
import com.becitizen.app.becitizen.presentation.msg.OneConversationActivity;

import org.json.JSONException;

import static com.facebook.FacebookSdk.getApplicationContext;

public class UserProfileActivity extends Fragment implements View.OnClickListener {

    private View rootView;
    private ControllerUserPresentation controllerUserPresentation;
    private TextView tvUsername;
    private TextView tvName;
    private TextView tvBirthDate;
    private TextView tvCountry;
    private TextView tvBiography;
    private TextView tvRank;
    private ImageView ivUserImage;
    private FloatingActionButton fbPrivateMessage;
    private CardView ibEditProfile;
    private CardView ibSignOut;
    private ImageView rankIcon;
    private ImageView ivBlock;
    private TextView tvBlock;
    private CardView blockButton;


    private String userMail;
    private boolean loggedUser;
    private String username;
    private boolean blockedUser;
    Bundle userData;

    public UserProfileActivity() {
        blockedUser = false;
        username = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_user_profile, container, false);
        controllerUserPresentation = ControllerUserPresentation.getUniqueInstance();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            loggedUser = bundle.getBoolean("loggeduser");
            if (!loggedUser) {
                username = bundle.getString("username");
                if (controllerUserPresentation.checkUsername(username)) loggedUser = true;
            }
        }

        tvUsername = rootView.findViewById(R.id.tvUsername);
        tvRank = rootView.findViewById(R.id.tvRank);
        rankIcon = rootView.findViewById(R.id.rankIcon);
        tvName = rootView.findViewById(R.id.tvName);
        tvBirthDate = rootView.findViewById(R.id.tvBirthday);
        tvCountry = rootView.findViewById(R.id.tvCountry);
        tvBiography = rootView.findViewById(R.id.tvBiography);
        ivUserImage = rootView.findViewById(R.id.ivUserImage);
        ivBlock = rootView.findViewById(R.id.ivBlock);
        tvBlock = rootView.findViewById(R.id.tvBlock);

        ibEditProfile = rootView.findViewById(R.id.ibEditProfile);
        ibEditProfile.setOnClickListener(this);

        ibSignOut = rootView.findViewById(R.id.ibSignOut);
        ibSignOut.setOnClickListener(this);

        blockButton = rootView.findViewById(R.id.blockButton);
        blockButton.setOnClickListener(this);

        fbPrivateMessage = rootView.findViewById(R.id.fbPrivateMessage);
        fbPrivateMessage.setOnClickListener(this);

        setValues();

        return rootView;
    }

    private void setValues() {

        if (loggedUser) {
            fbPrivateMessage.setVisibility(View.GONE);
            try {
                if(controllerUserPresentation.isLoggedWithMail()) {
                    ivBlock.setImageResource(R.drawable.reset_password);
                    tvBlock.setVisibility(View.GONE);
                }
                else blockButton.setVisibility(View.INVISIBLE);
            } catch (SharedPreferencesException e) {
                blockButton.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "SharedPreferencesError", Toast.LENGTH_LONG).show();
                Log.e("SharedPreferencesE", e.getMessage());
            }
        }
        else {
            try {
                if (ControllerUserPresentation.getUniqueInstance().isLoggedAsGuest()) {
                    fbPrivateMessage.setVisibility(View.GONE);
                    blockButton.setVisibility(View.INVISIBLE);
                }
            } catch (SharedPreferencesException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), R.string.sharedPreferencesError, Toast.LENGTH_LONG).show();
            }
            ibEditProfile.setVisibility(View.INVISIBLE);
            ibSignOut.setVisibility(View.INVISIBLE);
        }

        try {
            userData = controllerUserPresentation.viewProfile(username);

            setTextView("username", tvUsername);

            String name = userData.get("firstName").toString();
            name += " " + userData.get("lastName").toString();
            tvName.setText(name);

            setTextView("birthDate", tvBirthDate);
            setTextView("country", tvCountry);
            setTextView("biography", tvBiography);
            setTextView("rank", tvRank);
            setRankColor();

            setImage(userData.getInt("image"));

            if (userData.get("email") != null) {
                userMail = userData.get("email").toString();
            }

            if (userData.get("blocked") != null) {
                blockedUser = userData.getBoolean("blocked");
            }

            if (!loggedUser) {
                if (blockedUser) {
                    ivBlock.setImageResource(R.drawable.unblock_user);
                    tvBlock.setText(R.string.unblockUser);
                }

                else {
                    ivBlock.setImageResource(R.drawable.block_user);
                    tvBlock.setText(R.string.blockUser);
                }
            }
        }

        catch (ServerException e) {
            ibEditProfile.setVisibility(View.GONE);
            ibSignOut.setVisibility(View.GONE);
            fbPrivateMessage.setVisibility(View.GONE);

            if (e.getMessage().equals("User deactivated")) {

                blockButton.setVisibility(View.INVISIBLE);
                tvUsername.setText(username);
                tvName.setText(getResources().getString(R.string.deactived));
                tvName.setTextColor(Color.RED);

                tvRank.setVisibility(View.GONE);
                tvBirthDate.setVisibility(View.GONE);
                tvBiography.setVisibility(View.GONE);
                tvCountry.setVisibility(View.GONE);

                rootView.findViewById(R.id.labelRank).setVisibility(View.GONE);
                rootView.findViewById(R.id.labelBirthdate).setVisibility(View.GONE);
                rootView.findViewById(R.id.labelBiography).setVisibility(View.GONE);
                rootView.findViewById(R.id.labelCountry).setVisibility(View.GONE);

                rootView.findViewById(R.id.rankIcon).setVisibility(View.GONE);


            }

            else {
                Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                finishFragment();
            }
        }

        catch (JSONException e) {
            Toast.makeText(rootView.getContext(), getResources().getString(R.string.JSONerror), Toast.LENGTH_LONG).show();
            finishFragment();
        } catch (NetworkErrorException e) {
            Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
            toast.show();
            finishFragment();
        }
    }

    private void finishFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        getActivity().getSupportFragmentManager().popBackStack();
        transaction.remove(this);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaction.commit();
    }

    private void setTextView(String text, TextView tv) {
        if (userData.get(text) != null) {
            tv.setText(userData.get(text).toString());
        }
        else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setRankColor() {
        if (userData.get("rank") != null) {
            switch (userData.get("rank").toString()) {
                case "coal":
                    rankIcon.setImageResource(R.drawable.rank_coal);
                    break;
                case "bronze":
                    rankIcon.setImageResource(R.drawable.rank_bronze);
                    break;
                case "silver":
                    rankIcon.setImageResource(R.drawable.rank_silver);
                    break;
                case "gold":
                    rankIcon.setImageResource(R.drawable.rank_gold);
                    break;
                case "platinum":
                    rankIcon.setImageResource(R.drawable.rank_platinum);
                    break;
                case "diamond":
                    rankIcon.setImageResource(R.drawable.rank_diamond);
                    break;
            }
        } else rankIcon.setVisibility(View.GONE);
    }

    private void setImage(int number) {
        switch (number) {
            case 1:
                ivUserImage.setImageResource(R.drawable.userprofile1);
                break;

            case 2:
                ivUserImage.setImageResource(R.drawable.userprofile2);
                break;

            case 3:
                ivUserImage.setImageResource(R.drawable.userprofile3);
                break;

            case 4:
                ivUserImage.setImageResource(R.drawable.userprofile4);
                break;

            case 5:
                ivUserImage.setImageResource(R.drawable.userprofile5);
                break;

            case 6:
                ivUserImage.setImageResource(R.drawable.userprofile6);
                break;

            case 7:
                ivUserImage.setImageResource(R.drawable.userprofile7);
                break;

            case 8:
                ivUserImage.setImageResource(R.drawable.userprofile8);
                break;

            default:
                ivUserImage.setImageResource(R.drawable.userprofile1);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibEditProfile:
                editProfile();
                break;
            case R.id.ibSignOut:
                signOut();
                break;
            case R.id.blockButton:
                try {
                    if (loggedUser && controllerUserPresentation.isLoggedWithMail()) changePass();
                    else askConfirmation();
                } catch (SharedPreferencesException e) {
                    Toast.makeText(getContext(), getResources().getString(R.string.sharedPreferencesError), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.fbPrivateMessage:
                startConversation();
                break;
        }
    }

    private void askConfirmation() {
        if(!blockedUser) {
            new AlertDialog.Builder(rootView.getContext())
                    .setTitle(getResources().getString(R.string.blockUser))
                    .setMessage(getResources().getString(R.string.blockUserMsg))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            blockUser();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }

        else {
            new AlertDialog.Builder(rootView.getContext())
                    .setTitle(getResources().getString(R.string.unblockUser))
                    .setMessage(getResources().getString(R.string.unblockUserMsg))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            blockUser();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    private void startConversation() {
        if (userMail != null) {
            Conversation c = null;
            try {
                c = ControllerMsgPresentation.getInstance().getConversation(userMail);
                Intent intent = new Intent(getContext(), OneConversationActivity.class);
                intent.putExtra("id", c.getId());
                intent.putExtra("name", c.getName());
                intent.putExtra("username", c.getUserName());
                intent.putExtra("profilePicture", c.getUserImage());
                startActivity(intent);
            } catch (ServerException e) {
                e.printStackTrace();
                Toast.makeText(rootView.getContext(), getResources().getString(R.string.errorMessaging), Toast.LENGTH_LONG).show();
            }
        } else Toast.makeText(rootView.getContext(), getResources().getString(R.string.errorMessaging), Toast.LENGTH_LONG).show();
    }

    private void blockUser() {
        if (userMail == null) {
            Toast.makeText(rootView.getContext(), getResources().getString(R.string.errorBlock), Toast.LENGTH_LONG).show();
        }
        if (!blockedUser) {
            try {
                controllerUserPresentation.blockUser(userMail);
                ivBlock.setImageResource(R.drawable.unblock_user);
                tvBlock.setText(R.string.unblockUser);
                blockedUser = !blockedUser;
            }
            catch (ServerException e) {
                Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            catch (JSONException e) {
                Toast.makeText(rootView.getContext(), getResources().getString(R.string.JSONerror) , Toast.LENGTH_LONG).show();
            } catch (NetworkErrorException e) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        else {
            try {
                controllerUserPresentation.unblockUser(userMail);
                ivBlock.setImageResource(R.drawable.block_user);
                tvBlock.setText(R.string.blockUser);
                blockedUser = !blockedUser;
            }
            catch (ServerException e) {
                Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

                catch (JSONException e) {
                Toast.makeText(rootView.getContext(), getResources().getString(R.string.JSONerror) , Toast.LENGTH_LONG).show();
            } catch (NetworkErrorException e) {
                Toast toast = Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.networkError), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void signOut() {
        new AlertDialog.Builder(rootView.getContext())
                .setTitle(getResources().getString(R.string.logout))
                .setMessage(getResources().getString(R.string.logoutMsg))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        controllerUserPresentation.logout();
                        Toast.makeText(rootView.getContext(), getResources().getString(R.string.logout), Toast.LENGTH_LONG).show();
                        goToLogin();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void goToLogin() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public void editProfile() {
        Fragment fragment = new UserProfileEditActivity();
        fragmentTransaction(fragment, "USER_EDIT_PROFILE");
    }

    private void changePass() {
        Fragment fragment = new ChangePasswordActivity();
        fragmentTransaction(fragment, "CHANGE_PASSWORD");
    }

    private void fragmentTransaction(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
}
