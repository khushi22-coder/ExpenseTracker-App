package com.apexplanet.expensetracker.utils;

import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class BiometricHelper {

    private FragmentActivity activity;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public interface BiometricCallback {
        void onSuccess();
        void onFailure();
        void onError(String error);
    }

    public BiometricHelper(FragmentActivity activity) {
        this.activity = activity;
    }

    // Check if biometric is available
    public boolean isBiometricAvailable() {
        BiometricManager biometricManager =
                BiometricManager.from(activity);
        return biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_WEAK
        ) == BiometricManager.BIOMETRIC_SUCCESS;
    }

    // Show biometric prompt
    public void showBiometricPrompt(BiometricCallback callback) {
        Executor executor = ContextCompat.getMainExecutor(activity);

        biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        callback.onSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        callback.onFailure();
                    }

                    @Override
                    public void onAuthenticationError(
                            int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        callback.onError(errString.toString());
                    }
                });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use fingerprint to login")
                .setNegativeButtonText("Use Password")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}