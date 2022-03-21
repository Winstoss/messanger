import { useCallback, useEffect, useMemo, useState } from "react";
import { searchForChats, UserSearchEntry } from "../api";
import { debounce } from "../Util/debounce";

export function useChatSearch() {
  const [query, setQuery] = useState("");
  const [data, setData] = useState<UserSearchEntry[] | null>(null);

  const updateSearch = useCallback(async (query: string) => {
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
