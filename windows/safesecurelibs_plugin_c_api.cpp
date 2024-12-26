#include "include/safesecurelibs/safesecurelibs_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "safesecurelibs_plugin.h"

void SafesecurelibsPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  safesecurelibs::SafesecurelibsPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
