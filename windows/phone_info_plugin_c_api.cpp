#include "include/phone_info_plugin/phone_info_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "phone_info_plugin.h"

void PhoneInfoPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  phone_info_plugin::PhoneInfoPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
