@echo off
REM Set window to fullscreen
powershell -command "& { $sig = '[DllImport(\"user32.dll\")] public static extern bool ShowWindowAsync(IntPtr hWnd, int nCmdShow);'; Add-Type -MemberDefinition $sig -Name 'Win32ShowWindowAsync' -Namespace 'Win32'; [Win32.Win32ShowWindowAsync]::ShowWindowAsync((Get-Process -Id $pid).MainWindowHandle, 3); }"

REM Set font size to a smaller value (e.g., 12x16)
REM Note: This will change the font size for all future command prompts
reg add "HKCU\Console" /v FontSize /t REG_DWORD /d 0x000C0000 /f

setlocal enabledelayedexpansion
set CLASSPATH=
for %%i in (lib\*.jar) do (
    set CLASSPATH=!CLASSPATH!%%i;
)
set MAINCLASS=main
java -cp !CLASSPATH!;classes %MAINCLASS%