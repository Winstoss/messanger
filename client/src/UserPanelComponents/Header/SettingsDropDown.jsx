import * as DropdownMenu from "@radix-ui/react-dropdown-menu";

export function SettingsDropDown({ onSettings, onLogout }) {

  function handleLogout() {
    onLogout();
  }

  function handleSettings() {
    onSettings();
  }

  return (
    <DropdownMenu.Root>
      <DropdownMenu.Trigger />
      <DropdownMenu.Content>
        <DropdownMenu.Item onSelect={handleSettings}>Settings</DropdownMenu.Item>
        <DropdownMenu.Item onSelect={handleLogout}>Logout</DropdownMenu.Item>
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  );
}
