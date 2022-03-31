import { useState } from "react";
import { UserPanelHeader } from "./UserPanel/UserPanelHeader";
import { ChatList } from "./ChatList";
import { SearchResults } from "./UserPanel/Header/SearchResults";
import { UserSettings } from "./UserPanel/Header/UserSettings";
import { useNavigate } from "react-router";
import { useChatSearch } from "./useChatSearch";

type WhatToShowOptions = {
  showSettings: boolean;
  hasQuery: boolean;
  searchFocused: boolean;
};

function whatToShow({ showSettings, hasQuery, searchFocused }: WhatToShowOptions) {
  if (showSettings) return "settings";
  if (hasQuery || searchFocused) return "search";
  return "chat-list";
}

export function LeftPanel() {
  const [showSettings, setShowSettings] = useState(false);
  const [searchFocused, setSearchFocused] = useState(false);
  const { query, onQueryChange, data } = useChatSearch();

  const navigate = useNavigate();

  function handleLogout() {
    localStorage.clear();
    navigate("/login");
  }

  const showing = whatToShow({ showSettings, hasQuery: !!query, searchFocused });

  return (
    <div>
      <UserPanelHeader
        query={query}
        onQueryChange={onQueryChange}
        onFocusChange={setSearchFocused}
        onSettings={() => setShowSettings(true)}
        onLogout={handleLogout}
      />
      {showing === "settings" && <UserSettings onClose={() => setShowSettings(false)} />}
      {showing === "search" && !data && "Loading search results..."}
      {showing === "search" && data && <SearchResults chats={data} />}
      {showing === "chat-list" && <ChatList />}
    </div>
  );
}
