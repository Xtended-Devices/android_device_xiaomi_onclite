/*
   Copyright (c) 2016, The CyanogenMod Project
   Copyright (C) 2019 The LineageOS Project.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
   THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
   WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
   ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
   BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
   CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
   SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
   BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
   WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
   OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
   IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <sys/sysinfo.h>

#include "vendor_init.h"
#include "property_service.h"
#include "android/log.h"
#include <android-base/properties.h>

#include <string>
#define _REALLY_INCLUDE_SYS__SYSTEM_PROPERTIES_H_
#include <sys/_system_properties.h>

char const *heapgrowthlimit;
char const *heapminfree;

using android::init::property_set;
using std::string;

void property_override(string prop, string value) {
    auto pi = (prop_info*) __system_property_find(prop.c_str());

    if (pi != nullptr)
        __system_property_update(pi, value.c_str(), value.size());
    else
        __system_property_add(prop.c_str(), prop.size(), value.c_str(), value.size());
}

void load_props(string device, string model) {
    string RO_PROP_SOURCES[] = { "", "odm.", "system.", "vendor." };

    for (const string &source : RO_PROP_SOURCES) {
        property_override(string("ro.product.") + source + string("name"), device);
        property_override(string("ro.product.") + source + string("device"), device);
        property_override(string("ro.product.") + source + string("model"), model);
    }
}

void set_dalvik_properties() {
    struct sysinfo sys;
    sysinfo(&sys);

    if (sys.totalram > 3072ull * 1024 * 1024) {
        // Set for 4GB RAM
        property_set("dalvik.vm.heapstartsize", "8m");
        property_set("dalvik.vm.heapgrowthlimit", "192m");
        property_set("dalvik.vm.heapsize", "512m");
        property_set("dalvik.vm.heaptargetutilization", "0.6");
        property_set("dalvik.vm.heapmaxfree", "16m");
        property_set("dalvik.vm.heapminfree", "8m");
    } else {
        // Set for 2/3GB RAM
        property_set("dalvik.vm.heapstartsize", "8m");
        property_set("dalvik.vm.heapgrowthlimit", "192m");
        property_set("dalvik.vm.heapsize", "512m");
        property_set("dalvik.vm.heaptargetutilization", "0.75");
        property_set("dalvik.vm.heapmaxfree", "8m");
        property_set("dalvik.vm.heapminfree", "512k");
    }
}

void vendor_load_properties()
{
    string boot_cert = android::base::GetProperty("ro.boot.product.cert", "");

    if (boot_cert == "M1810F6LG" || boot_cert == "M1810F6LH" || boot_cert == "M1810F6LI")
        load_props("onclite", "Redmi 7");
    else
        load_props("onc", "Redmi Y3");

    set_dalvik_properties();
}
