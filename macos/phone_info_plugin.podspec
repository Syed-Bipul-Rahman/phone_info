#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint phone_info_plugin.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'phone_info_plugin'
  s.version          = '0.0.1'
  s.summary          = 'A powerful Flutter plugin to fetch detailed phone hardware and network information, including device architecture (CPU, ABI), network status (IP, connection type), and other critical metrics—ideal for debugging, analytics, and dynamic feature handling.'
  s.description      = <<-DESC
A powerful Flutter plugin to fetch detailed phone hardware and network information, including device architecture (CPU, ABI), network status (IP, connection type), and other critical metrics—ideal for debugging, analytics, and dynamic feature handling.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }

  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'

  # If your plugin requires a privacy manifest, for example if it collects user
  # data, update the PrivacyInfo.xcprivacy file to describe your plugin's
  # privacy impact, and then uncomment this line. For more information,
  # see https://developer.apple.com/documentation/bundleresources/privacy_manifest_files
  # s.resource_bundles = {'phone_info_plugin_privacy' => ['Resources/PrivacyInfo.xcprivacy']}

  s.dependency 'FlutterMacOS'

  s.platform = :osx, '10.11'
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }
  s.swift_version = '5.0'
end
