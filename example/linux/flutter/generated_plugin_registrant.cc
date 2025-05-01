//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <phone_info/phone_info_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) phone_info_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "PhoneInfoPlugin");
  phone_info_plugin_register_with_registrar(phone_info_registrar);
}
