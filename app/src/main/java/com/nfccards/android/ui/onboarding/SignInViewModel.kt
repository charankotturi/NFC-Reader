package com.nfccards.android.ui.onboarding

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class SignInViewModel : ViewModel() {

    private var _signedIn = false
    val loggedIn get() = _signedIn
    private var _phoneNumber = 0
    val phoneNumber get() = _phoneNumber
    var credentials : PhoneAuthCredential? = null
    var verificationId: String? = null
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null


    fun setPhoneNumber(phoneNum: Int) { _phoneNumber = phoneNum }
    fun setLoggedIn(isLoggedIn: Boolean) { _signedIn = isLoggedIn }
}