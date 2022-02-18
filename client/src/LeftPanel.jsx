import { useState, useEffect, useCallback, useMemo } from "react";
import { UserPanelHeader } from "./UserPanelComponents/UserPanelHeader";
import { ChatList } from "./Components/ChatList";
import { SearchResults } from "./UserPanelComponents/Header/SearchResults";
import { UserSettings } from "./UserPanelComponents/Header/UserSettings";
import { useNavigate } from "react-router";

async function searchForChats(query, token) {
  const response = await fetch(`http://localhost:8080/user/find?q=${encodeURIComponent(query)}`, {
    headers: { authorization: "Bearer " + token },
  });
  return response.json();
}

function debounce(func, timeout) {
  let timeoutId;
  return (...args) => {
    const later = () => {
      timeoutId = null;
      func(...args);
    };
    clearTimeout(timeoutId);
    timeoutId = setTimeout(later, timeout);
  };
}

function useChatSearch() {
  const [query, setQuery] = useState("");
  const [data, setData] = useState(null);

  const updateSearch = useCallback(async (query) => {
    if (query) {
      const searchData = await searchForChats(query, localStorage.getItem("token"));
      setData(searchData);
    } else {
      setData(null);
    }
  }, []);

  const debouncedUpdateSearch = useMemo(() => debounce(updateSearch, 600), [updateSearch]);

  useEffect(() => {
    debouncedUpdateSearch(query);
  }, [query]);
  console.log(data);
  return { query, onQueryChange: setQuery, data };
}

export function LeftPanel() {
  const [showingSettings, setShowingSettings] = useState(false);
  const [searchFocused, setSearchFocused] = useState(false);
  const { query, onQueryChange, data } = useChatSearch();

  const navigate = useNavigate()

  function handleLogout(){
    localStorage.clear()
    navigate("/login")
  }

  return (
    <div>
      <UserPanelHeader
        query={query}
        onQueryChange={onQueryChange}
        onFocusChange={setSearchFocused}
        onSettings={() => setShowingSettings(true)}
        onLogout={handleLogout}
      />
      {searchFocused || query || showingSettings ? (
        showingSettings ? (
          <UserSettings onClose={() => setShowingSettings(false)} />
        ) : data ? (
          <SearchResults data={data} />
        ) : (
          "Loading search results..."
        )
      ) : (
        <ChatList />
      )}
    </div>
  );
}
