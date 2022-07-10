package com.nfccards.android.ui.onboarding

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class SignInViewModel : ViewModel() {
    private var _phoneNumber = ""
    val phoneNumber get() = _phoneNumber
    private var _credentials: PhoneAuthCredential? = null
    val credentials get() = _credentials
    private var _verificationId: String? = null
    val verificationId get() = _verificationId
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    fun setCredentials(credentials: PhoneAuthCredential) { _credentials = credentials }
    fun setVerificationId(verificationId: String) { _verificationId = verificationId }
    fun setPhoneNumber(phoneNum: String) { _phoneNumber = phoneNum }
}