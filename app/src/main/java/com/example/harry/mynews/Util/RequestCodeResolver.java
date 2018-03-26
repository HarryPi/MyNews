package com.example.harry.mynews.Util;

import android.content.res.Resources;

import com.example.harry.mynews.R;

/**
 * Created by harry on 12/03/2018.
 * This class handles request code returns
 */

public class RequestCodeResolver {
    private Resources res;

    public RequestCodeResolver(Resources res) {
        this.res = res;
    }

    public int getFacebookRequestCode() {
        return res.getInteger(R.integer.facebook_request_code);
    }

    public int getNormalRegistrationCode() {
        return res.getInteger(R.integer.normal_registration_code);
    }

    public int getNormalRegistrationSuccessCode() {
        return res.getInteger(R.integer.normal_registration_success_code);
    }

    public int getNormalRegistrationCancelCode() {
        return res.getInteger(R.integer.normal_registration_cancel_code);
    }
}
