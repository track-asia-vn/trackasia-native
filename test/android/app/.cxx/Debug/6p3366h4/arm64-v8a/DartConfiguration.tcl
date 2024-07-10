# This file is configured by CMake automatically as DartConfiguration.tcl
# If you choose not to use CMake, this file may be hand configured, by
# filling in the required variables.


# Configuration directories and files
SourceDirectory: /Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native
BuildDirectory: /Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/test/android/app/.cxx/Debug/6p3366h4/arm64-v8a

# Where to place the cost data store
CostDataFile: 

# Site is something like machine.domain, i.e. pragmatic.crd
Site: SangNguyens-MBP

# Build name is osname-revision-compiler, i.e. Linux-2.4.2-2smp-c++
BuildName: Android-clang++

# Subprojects
LabelsForSubprojects: 

# Submission information
SubmitURL: http://

# Dashboard start time
NightlyStartTime: 00:00:00 EDT

# Commands for the build/test/submit cycle
ConfigureCommand: "/Users/sangnguyen/Library/Android/sdk/cmake/3.22.1/bin/cmake" "/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native"
MakeCommand: /Users/sangnguyen/Library/Android/sdk/cmake/3.22.1/bin/cmake --build . --config "${CTEST_CONFIGURATION_TYPE}"
DefaultCTestConfigurationType: Release

# version control
UpdateVersionOnly: 

# CVS options
# Default is "-d -P -A"
CVSCommand: 
CVSUpdateOptions: 

# Subversion options
SVNCommand: 
SVNOptions: 
SVNUpdateOptions: 

# Git options
GITCommand: /opt/homebrew/bin/git
GITInitSubmodules: 
GITUpdateOptions: 
GITUpdateCustom: 

# Perforce options
P4Command: 
P4Client: 
P4Options: 
P4UpdateOptions: 
P4UpdateCustom: 

# Generic update command
UpdateCommand: /opt/homebrew/bin/git
UpdateOptions: 
UpdateType: git

# Compiler info
Compiler: /Users/sangnguyen/Library/Android/sdk/ndk/26.1.10909125/toolchains/llvm/prebuilt/darwin-x86_64/bin/clang++
CompilerVersion: 17.0.2

# Dynamic analysis (MemCheck)
PurifyCommand: 
ValgrindCommand: 
ValgrindCommandOptions: 
DrMemoryCommand: 
DrMemoryCommandOptions: 
CudaSanitizerCommand: 
CudaSanitizerCommandOptions: 
MemoryCheckType: 
MemoryCheckSanitizerOptions: 
MemoryCheckCommand: MEMORYCHECK_COMMAND-NOTFOUND
MemoryCheckCommandOptions: --leak-check=full --gen-suppressions=all --error-exitcode=1 --suppressions=/Volumes/DATA/TRACKASIA_NATIVE_V2/trackasia-native/track-asia-native/scripts/valgrind.sup
MemoryCheckSuppressionFile: 

# Coverage
CoverageCommand: /usr/bin/gcov
CoverageExtraFlags: -l

# Testing options
# TimeOut is the amount of time in seconds to wait for processes
# to complete during testing.  After TimeOut seconds, the
# process will be summarily terminated.
# Currently set to 25 minutes
TimeOut: 1500

# During parallel testing CTest will not start a new test if doing
# so would cause the system load to exceed this value.
TestLoad: 

UseLaunchers: 
CurlOptions: 
# warning, if you add new options here that have to do with submit,
# you have to update cmCTestSubmitCommand.cxx

# For CTest submissions that timeout, these options
# specify behavior for retrying the submission
CTestSubmitRetryDelay: 5
CTestSubmitRetryCount: 3
