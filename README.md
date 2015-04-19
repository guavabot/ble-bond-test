# ble-bond-test
Sample of problem BLE bonding Nexus devices on Android 5.0

When connecting to a BLE device that requires bonding through a PIN, the system no longer presents the dialog to 
input the PIN and instead broadcasts the state `BONDED` directly.

After calling BluetoothDevice.createBond(), the system broadcasts `ACTION_BOND_STATE_CHANGED` with 
`EXTRA_BOND_STATE = BOND_BONDING` and `EXTRA_PREVIOUS_BOND_STATE = BOND_NONE` (everything ok until now). 
Then, without asking for a PIN (broadcast the intent `BluetoothDevice.ACTION_PAIRING_REQUEST`), 
it broadcasts `ACTION_BOND_STATE_CHANGED` with `EXTRA_BOND_STATE = BOND_BONDED` and `EXTRA_PREVIOUS_BOND_STATE = BOND_BONDING`.

The Bluetooth settings in the system settings also reports the bonding completed without asking for the pairing PIN. 
