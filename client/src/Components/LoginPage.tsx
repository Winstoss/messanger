import { useState } from "react";
import Login from "./Login";
import { Registration } from "./Registration";

export function LoginPage() {
  const [registration, setRegistration] = useState(false);

  if (registration) {
    return <Registration onTransitionToLogin={() => setRegistration(false)} />;
  }

  return <Login onTransferToRegistration={() => setRegistration(true)} />;
}
