#ifndef FLUTTER_PLUGIN_PHONE_INFO_PLUGIN_H_
#define FLUTTER_PLUGIN_PHONE_INFO_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace phone_info_plugin {

class PhoneInfoPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  PhoneInfoPlugin();

  virtual ~PhoneInfoPlugin();

  // Disallow copy and assign.
  PhoneInfoPlugin(const PhoneInfoPlugin&) = delete;
  PhoneInfoPlugin& operator=(const PhoneInfoPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace phone_info_plugin

#endif  // FLUTTER_PLUGIN_PHONE_INFO_PLUGIN_H_
