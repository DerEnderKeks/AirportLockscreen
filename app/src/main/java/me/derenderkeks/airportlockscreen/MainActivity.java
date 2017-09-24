package me.derenderkeks.airportlockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;

    private ComponentName deviceAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceAdmin = new ComponentName(this, DeviceAdminReceiver.class);

        requestDeviceAdministrator();

        sharedPreferences = getSharedPreferences(this.getClass().getPackage().getName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ((Switch) findViewById(R.id.enableSwitch)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_enabled), false));
        ((Switch) findViewById(R.id.switchKAll)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_features_all), false));
        ((Switch) findViewById(R.id.switchKFingerprint)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_fingerprint), false));
        ((Switch) findViewById(R.id.switchKRemoteInput)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_remote_input), false));
        ((Switch) findViewById(R.id.switchKSecureCamera)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_secure_camera), false));
        ((Switch) findViewById(R.id.switchKSecureNotifications)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_secure_notifications), false));
        ((Switch) findViewById(R.id.switchKTrustAgents)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_trust_agents), false));
        ((Switch) findViewById(R.id.switchKUnredactedNotifications)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_unredacted_notifications), false));
        ((Switch) findViewById(R.id.switchKWidgetsAll)).setChecked(sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_widgets_all), false));

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            }
        });
        final Switch enableSwitch = (Switch) findViewById(R.id.enableSwitch);
        enableSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean(getString(R.string.preference_enabled), enableSwitch.isChecked());
                editor.apply();
                if (!setKeyguardFeatures()) {
                    enableSwitch.setChecked(!enableSwitch.isChecked());
                    editor.putBoolean(getString(R.string.preference_enabled), !enableSwitch.isChecked());
                    editor.apply();
                } else {
                    Snackbar.make(findViewById(R.id.mainLayout), getString(sharedPreferences.getBoolean(getString(R.string.preference_enabled), false) ? R.string.message_enabled : R.string.message_disabled), (int) java.util.concurrent.TimeUnit.SECONDS.toMillis(2)).show();
                }
            }
        });
        ((Switch) findViewById(R.id.switchKAll)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_features_all), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKFingerprint)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_fingerprint), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKRemoteInput)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_remote_input), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKSecureCamera)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_secure_camera), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKSecureNotifications)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_secure_notifications), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKTrustAgents)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_trust_agents), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKUnredactedNotifications)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_unredacted_notifications), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
        ((Switch) findViewById(R.id.switchKWidgetsAll)).setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(getString(R.string.preference_keyguard_disable_widgets_all), isChecked);
                editor.apply();
                setKeyguardFeatures();
            }
        });
    }

    private boolean setKeyguardFeatures() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        if (!isDeviceAdmin()) {
            Snackbar.make(findViewById(R.id.mainLayout), getString(R.string.message_no_permission), (int) java.util.concurrent.TimeUnit.SECONDS.toMillis(3)).show();
            requestDeviceAdministrator();
            return false;
        }

        if (sharedPreferences.getBoolean(getString(R.string.preference_enabled), false)) {
            int keyguardFeatures = (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_features_all), false) ? DevicePolicyManager.KEYGUARD_DISABLE_FEATURES_ALL : 0) |
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_fingerprint), false) ? DevicePolicyManager.KEYGUARD_DISABLE_FINGERPRINT : 0) |
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_remote_input), false) ? 64 : 0) | // 64 is the value of DevicePolicyManager.KEYGUARD_DISABLE_REMOTE_INPUT but that requires API level 24 and this project has a minimum of 23
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_secure_camera), false) ? DevicePolicyManager.KEYGUARD_DISABLE_SECURE_CAMERA : 0) |
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_secure_notifications), false) ? DevicePolicyManager.KEYGUARD_DISABLE_SECURE_NOTIFICATIONS : 0) |
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_trust_agents), false) ? DevicePolicyManager.KEYGUARD_DISABLE_TRUST_AGENTS : 0) |
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_unredacted_notifications), false) ? DevicePolicyManager.KEYGUARD_DISABLE_UNREDACTED_NOTIFICATIONS : 0) |
                    (sharedPreferences.getBoolean(getString(R.string.preference_keyguard_disable_widgets_all), false) ? DevicePolicyManager.KEYGUARD_DISABLE_WIDGETS_ALL : 0);
            devicePolicyManager.setKeyguardDisabledFeatures(deviceAdmin, keyguardFeatures);
        } else {
            devicePolicyManager.setKeyguardDisabledFeatures(deviceAdmin, DevicePolicyManager.KEYGUARD_DISABLE_FEATURES_NONE);
        }

        return true;
    }

    private void requestDeviceAdministrator() {
        if (isDeviceAdmin()) return;
        Intent dialogIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        dialogIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
        startActivity(dialogIntent);
    }

    private boolean isDeviceAdmin() {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        return devicePolicyManager != null && devicePolicyManager.isAdminActive(deviceAdmin);
    }
}
