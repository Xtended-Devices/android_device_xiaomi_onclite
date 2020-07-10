/*
 * Copyright (C) 2018 The Xiaomi-SDM660 Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.lineageos.settings.device;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.preference.PreferenceFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import org.lineageos.settings.device.kcal.KCalSettingsActivity;
import org.lineageos.settings.device.preferences.SecureSettingListPreference;
import org.lineageos.settings.device.preferences.SecureSettingSwitchPreference;

public class DeviceSettings extends PreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String CATEGORY_DISPLAY = "display";
    private static final String PREF_DEVICE_KCAL = "device_kcal";

    public static final String PREF_THERMAL = "thermal";
    public static final String THERMAL_PATH = "/sys/devices/virtual/thermal/thermal_message/sconfig";

    private static final String PREF_ENABLE_DIRAC = "dirac_enabled";
    private static final String PREF_HEADSET = "dirac_headset_pref";
    private static final String PREF_PRESET = "dirac_preset_pref";

    private static final String PREF_SPECTRUM = "spectrum";
    private static final String SPECTRUM_SYSTEM_PROPERTY = "persist.spectrum.profile";

    private static final String CATEGORY_HALL_WAKEUP = "hall_wakeup";
    public static final String PREF_HALL_WAKEUP = "hall";
    public static final String HALL_WAKEUP_PATH = "/sys/module/hall/parameters/hall_toggle";

    private static final String DEVICE_DOZE_PACKAGE_NAME = "org.lineageos.settings.doze";

    private SecureSettingListPreference mTHERMAL;
    private SecureSettingListPreference mSPECTRUM;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_xiaomi_parts, rootKey);

        PreferenceCategory displayCategory = (PreferenceCategory) findPreference(CATEGORY_DISPLAY);

        Preference kcal = findPreference(PREF_DEVICE_KCAL);

        kcal.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), KCalSettingsActivity.class);
            startActivity(intent);
            return true;
        });

        mTHERMAL = (SecureSettingListPreference) findPreference(PREF_THERMAL);
        mTHERMAL.setValue(FileUtils.getValue(THERMAL_PATH));
        mTHERMAL.setSummary(mTHERMAL.getEntry());
        mTHERMAL.setOnPreferenceChangeListener(this);

<<<<<<< HEAD
        if (FileUtils.fileWritable(BACKLIGHT_DIMMER_PATH)) {
            mBacklightDimmer = (SecureSettingSwitchPreference) findPreference(PREF_BACKLIGHT_DIMMER);
            mBacklightDimmer.setChecked(FileUtils.getFileValueAsBoolean(BACKLIGHT_DIMMER_PATH, false));
            mBacklightDimmer.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(PREF_BACKLIGHT_DIMMER));
        }

        mSPECTRUM = (SecureSettingListPreference) findPreference(PREF_SPECTRUM);
        mSPECTRUM.setValue(FileUtils.getStringProp(SPECTRUM_SYSTEM_PROPERTY, "0"));
        mSPECTRUM.setSummary(mSPECTRUM.getEntry());
        mSPECTRUM.setOnPreferenceChangeListener(this);
=======
        boolean enhancerEnabled;
        try {
            enhancerEnabled = DiracService.sDiracUtils.isDiracEnabled();
        } catch (java.lang.NullPointerException e) {
            getContext().startService(new Intent(getContext(), DiracService.class));
            try {
                enhancerEnabled = DiracService.sDiracUtils.isDiracEnabled();
            } catch (NullPointerException ne) {
                // Avoid crash
                ne.printStackTrace();
                enhancerEnabled = false;
            }
        }

        SecureSettingSwitchPreference enableDirac = (SecureSettingSwitchPreference) findPreference(PREF_ENABLE_DIRAC);
        enableDirac.setOnPreferenceChangeListener(this);
        enableDirac.setChecked(enhancerEnabled);

        SecureSettingListPreference headsetType = (SecureSettingListPreference) findPreference(PREF_HEADSET);
        headsetType.setOnPreferenceChangeListener(this);

        SecureSettingListPreference preset = (SecureSettingListPreference) findPreference(PREF_PRESET);
        preset.setOnPreferenceChangeListener(this);
>>>>>>> parent of c34fa249e... XiaomiParts: Add Backlight Dimmer toggle

        if (FileUtils.fileWritable(HALL_WAKEUP_PATH)) {
            SecureSettingSwitchPreference hall = (SecureSettingSwitchPreference) findPreference(PREF_HALL_WAKEUP);
            hall.setChecked(FileUtils.getValue(HALL_WAKEUP_PATH).equals("Y"));
            hall.setOnPreferenceChangeListener(this);
        } else {
            getPreferenceScreen().removePreference(findPreference(CATEGORY_HALL_WAKEUP));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        switch (key) {
            case PREF_THERMAL:
                mTHERMAL.setValue((String) value);
                mTHERMAL.setSummary(mTHERMAL.getEntry());
                FileUtils.setValue(THERMAL_PATH, (String) value);
                break;

<<<<<<< HEAD
            case PREF_BACKLIGHT_DIMMER:
                FileUtils.setValue(BACKLIGHT_DIMMER_PATH, (boolean) value);
                break;

            case PREF_SPECTRUM:
                mSPECTRUM.setValue((String) value);
                mSPECTRUM.setSummary(mSPECTRUM.getEntry());
                FileUtils.setStringProp(SPECTRUM_SYSTEM_PROPERTY, (String) value);
=======
            case PREF_ENABLE_DIRAC:
                try {
                    DiracService.sDiracUtils.setEnabled((boolean) value);
                } catch (java.lang.NullPointerException e) {
                    getContext().startService(new Intent(getContext(), DiracService.class));
                    DiracService.sDiracUtils.setEnabled((boolean) value);
                }
                break;

            case PREF_HEADSET:
                try {
                    DiracService.sDiracUtils.setHeadsetType(Integer.parseInt(value.toString()));
                } catch (java.lang.NullPointerException e) {
                    getContext().startService(new Intent(getContext(), DiracService.class));
                    DiracService.sDiracUtils.setHeadsetType(Integer.parseInt(value.toString()));
                }
                break;

            case PREF_PRESET:
                try {
                    DiracService.sDiracUtils.setLevel(String.valueOf(value));
                } catch (java.lang.NullPointerException e) {
                    getContext().startService(new Intent(getContext(), DiracService.class));
                    DiracService.sDiracUtils.setLevel(String.valueOf(value));
                }
>>>>>>> parent of c34fa249e... XiaomiParts: Add Backlight Dimmer toggle
                break;

            case PREF_HALL_WAKEUP:
                FileUtils.setValue(HALL_WAKEUP_PATH, (boolean) value ? "Y" : "N");
                break;

            default:
                break;
        }
        return true;
    }

    private boolean isAppNotInstalled(String uri) {
        PackageManager packageManager = getContext().getPackageManager();
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }
}
