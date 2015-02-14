/*
 * Copyright (C) 2013 The Android Open Source Project
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
 * limitations under the License.
 */

package com.android.phone;

public class BluetoothManager {
    public BluetoothManager() {
    }

    /* package */ boolean isBluetoothHeadsetAudioOn() {
        return false;
    }

    /* package */ boolean isBluetoothAvailable() {
        if (VDBG) log("isBluetoothAvailable()...");

        // There's no need to ask the Bluetooth system service if BT is enabled:
        //
        //    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        //    if ((adapter == null) || !adapter.isEnabled()) {
        //        if (DBG) log("  ==> FALSE (BT not enabled)");
        //        return false;
        //    }
        //    if (DBG) log("  - BT enabled!  device name " + adapter.getName()
        //                 + ", address " + adapter.getAddress());
        //
        // ...since we already have a BluetoothHeadset instance.  We can just
        // call isConnected() on that, and assume it'll be false if BT isn't
        // enabled at all.

        // Check if there's a connected headset, using the BluetoothHeadset API.
        boolean isConnected = false;
        if (mBluetoothHeadset != null) {
            List<BluetoothDevice> deviceList = mBluetoothHeadset.getConnectedDevices();

            if (deviceList.size() > 0) {
                isConnected = true;
                if (VDBG) {
                    for (int i = 0; i < deviceList.size(); i++) {
                        BluetoothDevice device = deviceList.get(i);
                        log("state = " + mBluetoothHeadset.getConnectionState(device)
                                + "for headset: " + device);
                    }
                }
            }
        }

        if (VDBG) log("  ==> " + isConnected);
        return isConnected;
    }

    /* package */ boolean isBluetoothAudioConnected() {
        if (mBluetoothHeadset == null) {
            if (VDBG) log("isBluetoothAudioConnected: ==> FALSE (null mBluetoothHeadset)");
            return false;
        }
        List<BluetoothDevice> deviceList = mBluetoothHeadset.getConnectedDevices();

        if (deviceList.isEmpty()) {
            return false;
        }

        for (int i = 0; i < deviceList.size(); i++) {
            BluetoothDevice device = deviceList.get(i);
            boolean isAudioOn = mBluetoothHeadset.isAudioConnected(device);
            if (VDBG) log("isBluetoothAudioConnected: ==> isAudioOn = " + isAudioOn
                    + "for headset: " + device);
            if (isAudioOn) {
                return true;
            }
        }
        return false;
    }

    /* package */ boolean isBluetoothAudioConnectedOrPending() {
        return false;
    }

    /* package */ boolean showBluetoothIndication() {
        return false;
    }

    /* package */ void updateBluetoothIndication() {
    }

    public void addBluetoothIndicatorListener(BluetoothIndicatorListener listener) {
    }

    public void removeBluetoothIndicatorListener(BluetoothIndicatorListener listener) {
    }

    /* package */ void connectBluetoothAudio() {
    }

    /* package */ void disconnectBluetoothAudio() {
    }

    /* package */ interface BluetoothIndicatorListener {
        public void onBluetoothIndicationChange(boolean isConnected, BluetoothManager manager);
    }
}
