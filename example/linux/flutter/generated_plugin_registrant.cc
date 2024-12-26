//
//  Generated file. Do not edit.
//

// clang-format off

#include "generated_plugin_registrant.h"

#include <safesecurelibs/safesecurelibs_plugin.h>

void fl_register_plugins(FlPluginRegistry* registry) {
  g_autoptr(FlPluginRegistrar) safesecurelibs_registrar =
      fl_plugin_registry_get_registrar_for_plugin(registry, "SafesecurelibsPlugin");
  safesecurelibs_plugin_register_with_registrar(safesecurelibs_registrar);
}
