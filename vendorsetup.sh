MY_ANDROID_DIR=$PWD
cd $MY_ANDROID_DIR/frameworks/av
git reset --hard xtended/xq
git fetch https://github.com/Project-Xtended/frameworks_av xq-anp
git cherry-pick d4b819c79fedc19bda1c565969f2cc39981a5503
git cherry-pick cf5b100a734071d1698564911254783f1eae9ea1
cd MY_ANDROID_DIR/vendor/MiuiCamera/system/priv-app/MiuiCamera/ && rm MiuiCamera.apk
wget https://github.com/Xtended-Devices/vendor_MiuiCamera/raw/xq-onclite/system/priv-app/MiuiCamera/MiuiCamera.apk
cd MY_ANDROID_DIR
